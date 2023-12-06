package com.smartContactManager.smartContact.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartContactManager.smartContact.dao.UserRepository;
import com.smartContactManager.smartContact.entities.Contact;
import com.smartContactManager.smartContact.entities.User;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@ModelAttribute
	public void fetchUser(Model m, Principal p)
	{
		String username=p.getName();
		System.out.println("Usernamr"+username);
		User user=userRepository.getUserByUserName(username);
		System.out.println("user"+user);
		m.addAttribute("user", user);
	}

	@GetMapping(value = "/index")
	public String dashboard(Model m)
	{
		/*
		 * String username=p.getName(); System.out.println("Usernamr"+username); User
		 * user=userRepository.getUserByUserName(username);
		 * System.out.println("user"+user);
		 */
		
		m.addAttribute("title","user-dashboard");
		return "normal/user_dashboard";
	}
	@GetMapping("/add-contact")
	public String addContact(Model m)
	{
		m.addAttribute("title", "Add-Contact");
		m.addAttribute("contact", new Contact());
		
		return "normal/add-contactForm";
	}
}
