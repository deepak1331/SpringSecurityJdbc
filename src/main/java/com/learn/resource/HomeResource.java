package com.learn.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

	@GetMapping("/")
	public String getAll() {
		return "<h1>Welcome ! </h1>";
	}
	
	@GetMapping("/user/{name}")
	public String getUser(@PathVariable("name") final String name) {
		return String.format("<h1>Welcome, %s ! </h1></p>Role: User </p>",name);
	}
	
	@GetMapping("/admin/{name}")
	public String getAdmin(@PathVariable("name") final String name) {
		return String.format("<h1>Welcome, %s ! </h1></p>Role: Admin </p>",name);
	}
}
