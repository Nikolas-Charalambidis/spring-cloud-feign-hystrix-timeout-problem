package org.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Nothing runs at localhost:9000, therefore the feign client results in timeout
 */
@FeignClient(name = "my-feign", url = "localhost:9000", fallbackFactory = MyFallbackFactory.class)
public interface MyFeignClient {

	@GetMapping(value = "/api/dto")
	MyDto getDto();
}
