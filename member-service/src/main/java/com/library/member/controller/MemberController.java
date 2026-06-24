package com.library.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.member.DTO.MemberRequest;
import com.library.member.DTO.MemberResponse;
import com.library.member.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/members")
public class MemberController {

	@Autowired
	private MemberService service;

	@GetMapping
	public List<MemberResponse> getAll() {
		return service.getAll();
	}

	@GetMapping("/{id}")
	public MemberResponse getById(@PathVariable String id) {
		return service.getById(id);
	}

	@GetMapping("/user/{userId}")
	public MemberResponse getByUserId(@PathVariable String userId) {
		return service.getByUserId(userId);
	}

	@PostMapping
	public MemberResponse create(@Valid @RequestBody MemberRequest req) {
		return service.create(req);
	}

	@PutMapping("/renew/{id}")
	public MemberResponse renew(@PathVariable String id) {
		return service.renew(id);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable String id) {
		return service.delete(id);
	}
}
