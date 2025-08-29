package com.mysite.review.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.review.dto.SiteUserDTO;
import com.mysite.review.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/signup")
	public String signup(SiteUserDTO siteUserDTO) {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid SiteUserDTO siteUserDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}
		
		if (!siteUserDTO.getPassword1().equals(siteUserDTO.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		
		try {
			this.userService.create(siteUserDTO.getUsername(), 
					siteUserDTO.getEmail(), siteUserDTO.getPassword1());
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}

}
