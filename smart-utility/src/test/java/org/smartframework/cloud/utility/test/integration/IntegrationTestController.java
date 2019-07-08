package org.smartframework.cloud.utility.test.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class IntegrationTestController {

	@GetMapping
	public String get(String str) {
		return str;
	}
	
	@GetMapping("page")
	public List<String> page(String str) {
		List<String> list = new ArrayList<>();
		list.add(str);
		return list;
	}

	@PostMapping
	public String post(@RequestBody String str) {
		return str;
	}

	@PostMapping("list")
	public List<String> list(@RequestBody String str) {
		List<String> list = new ArrayList<>();
		list.add(str);
		return list;
	}
	
}