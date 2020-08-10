package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@Component
public class MyFallbackFactory implements FallbackFactory<MyFeignClient> {

	private static final  Logger LOGGER = LoggerFactory.getLogger(MyFallbackFactory.class);

	@Override
	public MyFeignClient create(final Throwable cause) {

		return () -> {
			LOGGER.info("Fallback");
			if (cause instanceof FeignException) {
				FeignException feignException = (FeignException) cause;
				HttpStatus status = HttpStatus.resolve(feignException.status());
				if (status != null) {
					LOGGER.info("Fallback caught FeignException, {}", status);
				} else LOGGER.info("Fallback caught FeignException, no status");

			} else {
				LOGGER.info("Fallback caught {}, no status", cause.getClass().getSimpleName());
			}
			throw new RuntimeException("I want to throw an exception", cause);
		};
	}
}
