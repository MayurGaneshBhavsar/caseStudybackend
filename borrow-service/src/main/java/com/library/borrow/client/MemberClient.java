package com.library.borrow.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberClient {

	@GetMapping("/members/user/{userId}")
	MemberResponse getMemberByUserId(@PathVariable String userId);
}
