package com.van.mngr.mvc.services;

import com.google.gson.Gson;
import com.van.mngr.mvc.clients.BusinessTierClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Autowired
    private BusinessTierClient client;
    @Autowired private Gson gson;
    public List<Map> listAll() {
        Map<String, Object> map = client.listAllBooks();
        List<Map> data = (List) map.get("data");
        if (data == null && data.size() == 0) {
            return Collections.emptyList();
        }
        return data;
    }

    public Map saveBook(Map book) {
//        Map<String, String> req = new HashMap<>();
//        req.put("name", book.get("name"));
//        req.put("author", book.get("auth"));
        Map<String, Object> response = client.saveBook(book);
        Map<String, String> data = (Map) response.get("data");
        return data;
    }
}
