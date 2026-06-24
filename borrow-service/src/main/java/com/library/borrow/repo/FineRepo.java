package com.library.borrow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.borrow.model.Fine;

public interface FineRepo extends JpaRepository<Fine, Long>{
	
	List<Fine> findByMemberIdAndStatus(String memberId, String status);
	List<Fine> findByMemberId(String memberId);

}
