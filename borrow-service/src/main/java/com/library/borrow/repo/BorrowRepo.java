package com.library.borrow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.borrow.model.Borrow;

public interface BorrowRepo extends JpaRepository<Borrow, Long>{

	List<Borrow> findByMemberIdAndStatus(String memberId, String status);
	List<Borrow> findByMemberIdAndBookIsbnAndStatus(String memberId, String bookIsbn, String status);
	java.util.Optional<Borrow> findFirstByMemberIdAndBookIsbnAndStatus(String memberId, String bookIsbn, String status);
	List<Borrow> findByMemberId(String memberId);
}
