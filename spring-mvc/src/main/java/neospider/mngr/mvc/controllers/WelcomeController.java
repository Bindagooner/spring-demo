package neospider.mngr.mvc.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.mvc.models.Book;
import neospider.mngr.mvc.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class WelcomeController {

    private String message = "Hello ";
    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @Autowired
    private BookService bookService;

    List<Book> bookList;

    @GetMapping("/")
    public String home(Model model, HttpSession httpSession) {

        Gson gson = new Gson();
        bookList = bookService.listAll();

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);
        model.addAttribute("book", Book.builder().build());
        log.info("httpSession id: {}", httpSession.getId());

        return "welcome"; //view
    }

    @GetMapping("/hello")
    public String mainWithParam(@RequestParam(name = "message", required = false, defaultValue = "") String message,
                                @ModelAttribute Book book,
                                Model model, HttpSession httpSession) {

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);
        model.addAttribute("book", book);
        log.info("httpSession id: {}", httpSession.getId());

        return "welcome"; //view
    }

    // form for CSRF testing
    @PostMapping("/saveBook")
    public String addBook(@ModelAttribute Book book, BindingResult bindingResult, Model model,
                          HttpSession httpSession, BindingResult errors) {
        log.info("received book: {}", book.toString());
        book = bookService.saveBook(book);
        bookList.add(book);

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);
        model.addAttribute("book", book);
        return "welcome";
    }

}
