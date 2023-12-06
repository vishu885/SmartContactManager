package com.smartContactManager.smartContact.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactManager.smartContact.dao.UserRepository;
import com.smartContactManager.smartContact.entities.User;
import com.smartContactManager.smartContact.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/home")
	public String home(Model m)
	{
		m.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model m)
	{
		m.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model m)
	{
		m.addAttribute("title","Register-Smart Contact Manager");
		m.addAttribute("user",new User() );
		return "signup";
	}
	//handler after user submit form
	@PostMapping("/do_register")
	public String onformSubmit(@Valid @ModelAttribute("user") User user ,BindingResult result1, Model m ,
			@RequestParam (value="agreement", defaultValue = "false") boolean agreement,
			HttpSession session)
	{
		
		try {
		if(!agreement)
		{
			System.out.println("Please accept terms");
			throw new Exception("You have not accepted terms and conditions!!");
		}
		
		if(result1.hasErrors()) 
		{
			System.out.println("Error"+result1.toString());
			m.addAttribute("user",user);
			return "signup";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageurl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User result=this.userRepository.save(user);
		System.out.println(result);
		m.addAttribute("user",new User());
		//session.setAttribute("message",new Message("Successfully registered", "alert-success"));
		//Message message=(Message)session.getAttribute("message");
		m.addAttribute("message", new Message("Successfully registered", "alert-success"));
		//session.removeAttribute("message");
		return "signup";

		}
		catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			//session.setAttribute("message", new Message("Something went wrong"+e.getMessage(), "alert-danger"));
			//Message message=(Message)session.getAttribute("message");
			m.addAttribute("message",new Message("Something went wrong"+e.getMessage(), "alert-danger"));
			//session.removeAttribute("message");
			return "signup";

		}

	}
	@GetMapping("/signin")
	public String customLogin() {
		return "login";
	}
}
