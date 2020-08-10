package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/feign")
public class MyRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyRestController.class);

	private final MyFeignClient myFeignClient;

	public MyRestController(final MyFeignClient myFeignClient) {
		this.myFeignClient = myFeignClient;
	}

	@GetMapping("/api/dto")
	public MyDto get() {
		LOGGER.info("Calling feign client");
		return myFeignClient.getDto();
	}
}
