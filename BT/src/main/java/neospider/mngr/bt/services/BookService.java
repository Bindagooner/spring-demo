package neospider.mngr.bt.services;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.bt.persistence.entities.BookEntity;
import neospider.mngr.bt.persistence.repositories.MyBatisBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
        BookEntity book1 = BookEntity.builder().name("aaa").author("bbbb").id(UUID.randomUUID().toString()).build();
        BookEntity book2 = BookEntity.builder().name("aaa2").author("bbbb3").id(UUID.randomUUID().toString()).build();
        BookEntity book3 = BookEntity.builder().name("aaa3").author("bbbb3").id(UUID.randomUUID().toString()).build();
        bookRepository.insert(book1);
        bookRepository.insert(book2);
        bookRepository.insert(book3);

        log.info("init data done");
    }

    public BookEntity save(Map<String, String> map) {
        BookEntity book = BookEntity.builder().author(map.get("author"))
                .id(UUID.randomUUID().toString()).name(map.get("name")).build();

        bookRepository.insert(book);
        log.info("saved new book: {}", book.toString());
        return book;
    }

    public BookEntity findById(String id) {
        return bookRepository.findById(id);
    }

    public List<BookEntity> listAll() {
        return bookRepository.listAll();
    }
}
