package com.van.mngr.mvc.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * this is for communication with legacy service.
 * use <a href="https://www.baeldung.com/intro-to-feign">Feign Client</a>.
 */
public interface BusinessTierClient {

    @RequestLine("POST /book")
    @Headers("Content-Type: application/json")
    Map<String, Object> saveBook(Map<String, String> request);

    @RequestLine("GET /book/list-all")
    Map<String, Object> listAllBooks();

    @RequestLine("GET /user/get-user-by-username?username={username}")
    Map<String, Object> getUserByUsername(@Param("username") String username);
}
