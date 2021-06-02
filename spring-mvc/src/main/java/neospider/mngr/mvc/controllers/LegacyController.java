package neospider.mngr.mvc.controllers;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock controller for legacy service.
 * this endpoint should belong to legacy system.
 */
@RestController
@RequestMapping("/legacy")
public class LegacyController {

    @Data
    @Builder
    public static class Book {
        String name;
        String author;
    }

    @GetMapping("/books")
    public Map<String, List<String>> listAll() {
        Book book1 = Book.builder().name("aaa").author("bbbb").build();
        Book book2 = Book.builder().name("aaa2").author("bbbb3").build();
        Book book3 = Book.builder().name("aaa3").author("bbbb3").build();

        Map<String, List<String>> resp = new HashMap<>();
        Gson gson = new Gson();
        resp.put("data", Arrays.asList(gson.toJson(book1), gson.toJson(book2), gson.toJson(book3)));
        return resp;
    }
}
