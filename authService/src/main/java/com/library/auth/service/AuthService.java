package com.library.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.auth.DTO.AuthResponse;
import com.library.auth.DTO.UserLogin;
import com.library.auth.DTO.UserRegister;
import com.library.auth.model.User;
import com.library.auth.repo.UserRepo;
import com.library.auth.security.JwtUtil;

@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;	

	private String generateId() {
		long count = userRepo.count() + 1;
		return String.format("LIB%03d", count);
	}

	private String generateUserName(String firstName, String mobile) {
		String name;
		if (firstName.length() < 4) {
			name = firstName;
		} else {
			name = firstName.substring(0, 4).toLowerCase();
		}
		int count = Integer.parseInt(mobile.substring(0, 4));
		String username = name + count;
		while (userRepo.findByUserName(username).isPresent()) {
			count++;
			username = name + count;
		}
		return username;
	}

	public AuthResponse register(UserRegister rg) {
		log.info("Registering new user with email: {}", rg.getEmail());
		String username = generateUserName(rg.getFirstName(), rg.getMobNo());
		User user = new User();
		user.setId(generateId());
		user.setFirstName(rg.getFirstName());
		user.setLastName(rg.getLastName());
		user.setMobNo(rg.getMobNo());
		user.setEmail(rg.getEmail());
		user.setAddr(rg.getAddr());
		user.setPassword(passwordEncoder.encode(rg.getPassword()));
		user.setRole("USER");
		user.setUserName(username);
		userRepo.save(user);
		log.info("User registered successfully with id: {} and username: {}", user.getId(), username);
		return new AuthResponse("User Registered Successfully!!", "Your username is: " + username);
	}

	public AuthResponse login(UserLogin lg) {
		log.info("Login attempt for username: {}", lg.getUserName());
		User user = userRepo.findByUserName(lg.getUserName())
				.orElseThrow(() -> {
					log.warn("Login failed - username not found: {}", lg.getUserName());
					return new RuntimeException("Invalid Username");
				});

		if (!passwordEncoder.matches(lg.getPassword(), user.getPassword())) {
			log.warn("Login failed - invalid password for username: {}", lg.getUserName());
			throw new RuntimeException("Invalid Password");
		}

		String token = jwtUtil.generateToken(user.getUserName(), user.getRole());
		log.info("Login successful for username: {} with role: {}", user.getUserName(), user.getRole());
		
		return new AuthResponse("Login Successfully!!", "Welcome: " + user.getFirstName(), token, user.getRole(), user.getId());
	}

	public boolean userExists(String id) {
		boolean exists = userRepo.existsById(id);
		log.debug("User existence check for id: {} -> {}", id, exists);
		return exists;
	}
}
