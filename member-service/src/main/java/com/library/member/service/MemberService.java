package com.library.member.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.member.DTO.MemberRequest;
import com.library.member.DTO.MemberResponse;
import com.library.member.client.AuthClient;
import com.library.member.exception.DuplicateMemberException;
import com.library.member.exception.MemberNotFoundException;
import com.library.member.model.Member;
import com.library.member.repo.MemberRepo;

@Service
public class MemberService {

	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberRepo memberRepo;
	@Autowired
	private AuthClient authClient;

	private String generateId() {
		long count = memberRepo.count() + 1;
		return String.format("MEM%03d", count);
	}

	public MemberResponse create(MemberRequest req) {
		log.info("Creating membership for userId: {}", req.getUserId());
		if (!authClient.userExists(req.getUserId())) {
			log.warn("User not found in auth-service for userId: {}", req.getUserId());
			throw new RuntimeException("User with id '" + req.getUserId() + "' does not exist");
		}
		if (memberRepo.findByUserId(req.getUserId()).isPresent()) {
			log.warn("Membership already exists for userId: {}", req.getUserId());
			throw new DuplicateMemberException("Membership Already Exists");
		}
		Member m = new Member();
		m.setId(generateId());
		m.setUserId(req.getUserId());
		LocalDate today = LocalDate.now();
		m.setMembershipStart(today);
		m.setMembershipEnd(today.plusMonths(3));
		m.setStatus("ACTIVE");
		memberRepo.save(m);
		log.info("Membership created successfully with id: {} for userId: {}", m.getId(), req.getUserId());
		return map(m);
	}

	private void updateStatus(Member m) {
		if (m.getMembershipEnd().isAfter(LocalDate.now()))
			m.setStatus("ACTIVE");
		else
			m.setStatus("INACTIVE");
	}

	public MemberResponse getById(String id) {
		log.info("Fetching member by id: {}", id);
		Member m = memberRepo.findById(id).orElseThrow(() -> {
			log.warn("Member not found with id: {}", id);
			return new MemberNotFoundException("Member not found");
		});
		updateStatus(m);
		memberRepo.save(m);
		return map(m);
	}

	public MemberResponse getByUserId(String userId) {
		log.info("Fetching member by userId: {}", userId);
		Member m = memberRepo.findByUserId(userId).orElseThrow(() -> {
			log.warn("Member not found with userId: {}", userId);
			return new MemberNotFoundException("Member not found");
		});
		updateStatus(m);
		memberRepo.save(m);
		return map(m);
	}

	public MemberResponse renew(String id) {
		log.info("Renewing membership for id: {}", id);
		Member m = memberRepo.findById(id).orElseThrow(() -> new MemberNotFoundException("Member not found"));
		m.setMembershipEnd(LocalDate.now().plusMonths(3));
		m.setStatus("ACTIVE");
		memberRepo.save(m);
		log.info("Membership renewed for id: {} until: {}", id, m.getMembershipEnd());
		return map(m);
	}

	public List<MemberResponse> getAll() {
		log.info("Fetching all members");
		List<Member> members = memberRepo.findAll();
		log.info("Total members found: {}", members.size());
		return members.stream().map(this::map).toList();
	}

	public String delete(String id) {
		log.info("Deleting member with id: {}", id);
		memberRepo.findById(id).orElseThrow(() -> new MemberNotFoundException("Member not found"));
		memberRepo.deleteById(id);
		log.info("Member deleted successfully with id: {}", id);
		return "Member deleted successfully";
	}

	private MemberResponse map(Member m) {
		return new MemberResponse(m.getId(), m.getUserId(), m.getMembershipEnd(), m.getStatus());
	}
}
