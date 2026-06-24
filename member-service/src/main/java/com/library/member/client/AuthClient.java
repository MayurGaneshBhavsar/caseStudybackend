package com.library.member.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authService")
public interface AuthClient {

	@GetMapping("/auth/users/{id}/exists")
	boolean userExists(@PathVariable String id);
}
