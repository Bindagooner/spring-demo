package com.van.mngr.bt.services;

import com.google.gson.Gson;
import com.van.mngr.bt.persistence.repository.book.MyBatisBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BookService {

    @Autowired
    private MyBatisBookRepository bookRepository;

    Gson gson = new Gson();

    @PostConstruct
    public void init() {
        Map book1 = new HashMap<String, String>();
        book1.put("name", "aaa");
        book1.put("author", "bbbb");
        book1.put("id", UUID.randomUUID().toString());

        Map book2 = new HashMap<String, String>();
        book2.put("name", "aaa2");
        book2.put("author", "bbbb2");
        book2.put("id", UUID.randomUUID().toString());

        Map book3 = new HashMap<String, String>();
        book3.put("name", "aaa3");
        book3.put("author", "bbbb3");
        book3.put("id", UUID.randomUUID().toString());

        bookRepository.deleteAll(); // TODO refresh data
        bookRepository.insert(book1);
        bookRepository.insert(book2);
        bookRepository.insert(book3);

        log.info("init data done");
    }

    public Map save(Map<String, String> map) {
        map.put("id",UUID.randomUUID().toString());
        bookRepository.insert(map);
        log.info("saved new book: {}", map.get("id"));
        return map;
    }

    public Map findById(String id) {
        return bookRepository.findById(id);
    }

    public List<Map> listAll() {
        return bookRepository.listAll();
    }
}
