package com.library.member.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.member.model.Member;

public interface MemberRepo extends JpaRepository<Member, String>{
	
	//List<Member> findActive();
	
	Optional<Member> findByUserId(String userId);
	
	

}
