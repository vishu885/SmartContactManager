package com.smartContactManager.smartContact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartContactManager.smartContact.dao.UserRepository;
import com.smartContactManager.smartContact.entities.Contact;
import com.smartContactManager.smartContact.entities.User;
import com.smartContactManager.smartContact.helper.Message;

import jakarta.servlet.http.HttpSession;

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
	@PostMapping("/do_addContact")
	public String doAddContact(@Valid @ModelAttribute Contact contact,BindingResult result,
			@RequestParam("image") MultipartFile file, Principal p, HttpSession session
			,Model m)
	{
	try {
		String name=p.getName();
		User user=this.userRepository.getUserByUserName(name);
		
		if(result.hasErrors()) 
		{
			System.out.println("Error"+result.toString());
			m.addAttribute("contact",contact);
			return "normal/add-contactForm";
		}
		
		//Processing file
		if(file.isEmpty())
		{
			System.out.println("File is empty");
		}
		else {
			contact.setImageurl(file.getOriginalFilename());
			File savefile=new ClassPathResource("static/image").getFile();
			Path path =Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
		}
		contact.setUser(user);
		user.getContact().add(contact);
		
		userRepository.save(user);
		session.setAttribute("message", new Message("Successfully added","alert-success"));
		
	} 
	catch (Exception e) {
		System.out.println("Error"+e.getMessage());
		e.printStackTrace();
		session.setAttribute("message", new Message("Error adding data","alert-danger"));
	}
		return "normal/add-contactForm";
	}
}
