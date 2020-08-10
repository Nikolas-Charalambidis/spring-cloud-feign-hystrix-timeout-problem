# spring-cloud-feign-hystrix-timeout-problem

StackOverflow question: [Spring Cloud Feign client with Hystrix circuit-breaker timeout defaults in 2 seconds](https://stackoverflow.com/q/63299493/3764965).

## Problem

The configured Feign client and Hystrix on unavailable endpoint falls correctly to fallback, however too early, after **2** seconds although the configuration (`application.yml`) says different:

```
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
  hystrix:
    enabled: true
    
hystrix:
  command:
    default:
      execution:
        timeout:
          enabled: true
        isolation:
          thread:
            timeoutInMilliseconds: 60000
```

## How to reproduce

1. Make sure nothing runs on `localhost:8080` (this application) and `localhost:9090` ( used as a "unavailable service")
2. Run the Spring Boot application `org.example.Application`. It starts on the default port `8080`.
3. Call the following CURL:
    ```
    curl --request GET --url http://localhost:8080/feign/api/dto
    ```

The result might be the following log which clearly shows that the fallback was triggered too way early in **2** seconds however it is configured to **5**.

 - `Calling feign client` is logged right after calling the CURL invoking `MyRestController::get`.
 - `Fallback` is logged right after the request falls into fallback `MyFallbackFactory::create`.

```
2020-08-10 19:19:04.254  INFO 15368 --- [nio-8080-exec-2] org.example.MyRestController             : Calling feign client
2020-08-10 19:19:06.276  INFO 15368 --- [trix-my-feign-2] org.example.MyFallbackFactory            : Fallback
2020-08-10 19:19:06.277  INFO 15368 --- [trix-my-feign-2] org.example.MyFallbackFactory            : Fallback caught FeignException, no status
2020-08-10 19:19:06.287 ERROR 15368 --- [nio-8080-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is com.netflix.hystrix.exception.HystrixRuntimeException: MyFeignClient#getDto() failed and fallback failed.] with root cause

java.net.ConnectException: Connection refused: connect
	at java.base/java.net.PlainSocketImpl.waitForConnect(Native Method) ~[na:na]
	at java.base/java.net.PlainSocketImpl.socketConnect(PlainSocketImpl.java:107) ~[na:na]
	at java.base/java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:399) ~[na:na]
    ...
```