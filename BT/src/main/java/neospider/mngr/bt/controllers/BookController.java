package neospider.mngr.bt.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.bt.persistence.entities.BookEntity;
import neospider.mngr.bt.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("book")
@Slf4j
public class BookController extends BaseController {

    private BookService bookService;

    @Autowired
    BookController(Gson gson, BookService bookService) {
        super(gson);
        this.bookService = bookService;
    }

    @GetMapping("list-all")
    public ResponseEntity<?> listAll() {
        log.info("list all books");
        return buildMapSuccessResponse(bookService.listAll());
    }

    @PostMapping("")
    public ResponseEntity<?> saveBook(@RequestBody Map<String, String> map) {
        log.info("save book.");
        BookEntity saved = bookService.save(map);
        return buildMapSuccessResponse(saved);
    }

}
