package neospider.mngr.mvc.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.mvc.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class WelcomeController {

    private String message = "Hello ";
    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @Autowired
    private BookService bookService;

    @Autowired
    private ServerProperties serverProperties;

    List<Map> bookList;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model, HttpSession httpSession) {

        bookList = bookService.listAll();

        Integer hits = (Integer) httpSession.getAttribute("hits");
        if (hits == null) {
            hits = 0;
        }
        httpSession.setAttribute("hits", ++hits);

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);
        model.addAttribute("name", "");
        model.addAttribute("author", "");
        log.info("httpSession id: {}", httpSession.getId());
        log.info("hits : {}", httpSession.getAttribute("hits"));
        model.addAttribute("port", serverProperties.getPort());

        return "welcome"; //view
    }

    @GetMapping("/hello")
    public String mainWithParam(@RequestParam(name = "message", required = false, defaultValue = "") String message,
                                Model model, HttpSession httpSession) {

        Integer hits = (Integer) httpSession.getAttribute("hits");
        if (hits == null) {
            hits = 0;
        }
        httpSession.setAttribute("hits", ++hits);

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);
        log.info("httpSession id: {}", httpSession.getId());
        log.info("hits : {}", httpSession.getAttribute("hits"));
        model.addAttribute("port", serverProperties.getPort());

        return "welcome"; //view
    }

    // form for CSRF testing
    @PostMapping("/saveBook")
    public String addBook(HttpServletRequest request, Model model) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> book = new HashMap<>();
        book.put("name", parameterMap.get("name")[0]);
        book.put("author", parameterMap.get("author")[0]);

        book = bookService.saveBook(book);
        bookList.add(book);

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);
        return "welcome";
    }

    @GetMapping("/version")
    public @ResponseBody ResponseEntity<?> getVersion() {
        return ResponseEntity.ok("1.1.1.2222");
    }

}
