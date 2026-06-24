package com.library.borrow.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.library.borrow.DTO.BorrowRequest;
import com.library.borrow.DTO.BorrowResponse;
import com.library.borrow.client.BookClient;
import com.library.borrow.client.BookResponse;
import com.library.borrow.client.MemberClient;
import com.library.borrow.client.MemberResponse;
import com.library.borrow.model.Borrow;
import com.library.borrow.model.Fine;
import com.library.borrow.repo.BorrowRepo;
import com.library.borrow.repo.FineRepo;

@Service
public class BorrowService {

	private static final Logger log = LoggerFactory.getLogger(BorrowService.class);

	@Autowired
	private BorrowRepo borrowRepo;
	@Autowired
	private MemberClient memberClient;
	@Autowired
	private BookClient bookClient;
	@Autowired
	private FineRepo fineRepo;

	// issue 
	public BorrowResponse issue(BorrowRequest req) {
		log.info("Issuing book '{}' for userId: {}", req.getTitle(), req.getUserId());

		MemberResponse member = memberClient.getMemberByUserId(req.getUserId());
		if (member.getStatus().equals("INACTIVE")) {
			log.warn("Issue failed - membership inactive for userId: {}", req.getUserId());
			throw new RuntimeException("Membership Inactive");
		}

		if (!fineRepo.findByMemberIdAndStatus(member.getId(), "PENDING").isEmpty()) {
			log.warn("Issue failed - pending fine exists for memberId: {}", member.getId());
			throw new RuntimeException("Please clear pending fine first!");
		}

		List<Borrow> overdue = borrowRepo.findByMemberIdAndStatus(member.getId(), "OVERDUE");
		if (!overdue.isEmpty()) {
			log.warn("Issue failed - overdue borrows exist for memberId: {}", member.getId());
			throw new RuntimeException("Member has overdue");
		}

		BookResponse book = bookClient.getBookByTitle(req.getTitle())
				.stream().findFirst().orElseThrow(() -> {
					log.warn("Issue failed - book not found: {}", req.getTitle());
					return new RuntimeException("Book not found");
				});

		if (!borrowRepo.findByMemberIdAndBookIsbnAndStatus(member.getId(), book.getBookIsbn(), "ISSUED").isEmpty()) {
			log.warn("Issue failed - book already borrowed by memberId: {}", member.getId());
			throw new RuntimeException("Book already borrowed");
		}

		if (book.getAvailableCopies() <= 0) {
			log.warn("Issue failed - no available copies for book: {}", req.getTitle());
			throw new RuntimeException("Book not available");
		}

		Borrow b = new Borrow();
		b.setBookIsbn(book.getBookIsbn());
		b.setMemberId(member.getId());
		LocalDate today = LocalDate.now();
		b.setIssueDate(today);
		b.setDueDate(today.plusDays(15));
		b.setStatus("ISSUED");

		bookClient.decreaseCopies(b.getBookIsbn());
		borrowRepo.save(b);
		log.info("Book issued successfully - borrowId: {}, memberId: {}, isbn: {}, dueDate: {}",
				b.getId(), member.getId(), b.getBookIsbn(), b.getDueDate());
		return map(b);
	}

	public String returnBook(Long borrowId) {
		log.info("Processing return for borrowId: {}", borrowId);
		Borrow b = borrowRepo.findById(borrowId).orElseThrow(() -> new RuntimeException("Not found"));

		if (!b.getStatus().equals("ISSUED")) {
			log.warn("Return failed - borrow not in ISSUED state, borrowId: {}, status: {}", borrowId, b.getStatus());
			throw new RuntimeException("Book is already returned " + b.getStatus());
		}

		b.setReturnDate(LocalDate.now());

		if (LocalDate.now().isAfter(b.getDueDate())) {
			long days = ChronoUnit.DAYS.between(b.getDueDate(), LocalDate.now());
			double fineAmount = days * 10;
			b.setStatus("OVERDUE");

			Fine f = new Fine();
			f.setMemberId(b.getMemberId());
			f.setBorrowId(b.getId().toString());
			f.setAmount(fineAmount);
			f.setStatus("PENDING");
			f.setPaymentDate(null);

			fineRepo.save(f);
			borrowRepo.save(b);
			bookClient.increaseCopies(b.getBookIsbn());
			log.warn("Book returned late - borrowId: {}, overdue days: {}, fine: Rs.{}", borrowId, days, fineAmount);
			return "Book returned late! Fine of Rs." + fineAmount + " is pending. you need to it ASPAP.";
		} else {
			b.setStatus("RETURNED");
			borrowRepo.save(b);
			bookClient.increaseCopies(b.getBookIsbn());
			log.info("Book returned successfully - borrowId: {}", borrowId);
			return "Book Returned Successfully!!";
		}
	}

	public BorrowResponse extendDueDate(Long borrowId) {
		log.info("Extending due date for borrowId: {}", borrowId);
		Borrow b = borrowRepo.findById(borrowId).orElseThrow(() -> new RuntimeException("Borrow not found"));

		if (!b.getStatus().equals("ISSUED"))
			throw new RuntimeException("Can only extend an ISSUED borrow, current status: " + b.getStatus());

		if (LocalDate.now().isAfter(b.getDueDate()))
			throw new RuntimeException("Due date already passed, cannot extend");

		if (b.isExtended())
			throw new RuntimeException("Due date already extended once, no further extensions allowed");

		b.setDueDate(b.getDueDate().plusDays(7));
		b.setExtended(true);
		borrowRepo.save(b);
		log.info("Due date extended for borrowId: {} -> new due date: {}", borrowId, b.getDueDate());
		return map(b);
	}

	public String payFine(Long fineId) {
		log.info("Processing fine payment for fineId: {}", fineId);
		Fine f = fineRepo.findById(fineId).orElseThrow(() -> new RuntimeException("Fine not found"));

		if (!f.getStatus().equals("PENDING"))
			throw new RuntimeException("Fine is already " + f.getStatus());

		f.setStatus("PAID");
		f.setPaymentDate(LocalDate.now());
		fineRepo.save(f);

		Borrow b = borrowRepo.findById(Long.parseLong(f.getBorrowId()))
				.orElseThrow(() -> new RuntimeException("Borrow not found"));
		b.setStatus("RETURNED");
		borrowRepo.save(b);
		log.info("Fine paid and borrow marked RETURNED - fineId: {}, borrowId: {}", fineId, b.getId());
		return "Fine paid and book is RETURNED successfully!.";
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateOverdue() {
		log.info("Running overdue update job");
		List<Borrow> list = borrowRepo.findAll();
		int count = 0;
		for (Borrow b : list) {
			if (b.getDueDate().isBefore(LocalDate.now()) && b.getReturnDate() == null) {
				b.setStatus("OVERDUE");
				count++;
			}
		}
		borrowRepo.saveAll(list);
		log.info("Overdue update complete - {} records marked as OVERDUE", count);
	}

	public BorrowResponse map(Borrow b) {
		return new BorrowResponse(b.getId(), b.getBookIsbn(), b.getIssueDate(), b.getDueDate(), b.getStatus());
	}

	public List<BorrowResponse> getAllBorrows() {
		log.info("Fetching all borrows");
		return borrowRepo.findAll().stream().map(this::map).toList();
	}

	public List<BorrowResponse> getByMember(String memberId) {
		log.info("Fetching borrows for memberId: {}", memberId);
		return borrowRepo.findByMemberId(memberId).stream().map(this::map).toList();
	}

	public List<Fine> getAllFines() {
		log.info("Fetching all fines");
		return fineRepo.findAll();
	}

	public List<Fine> getFinesByMember(String memberId) {
		log.info("Fetching fines for memberId: {}", memberId);
		return fineRepo.findByMemberId(memberId);
	}
}
