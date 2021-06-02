package neospider.mngr.mvc.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.mvc.services.LegacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    private LegacyService legacyService;

    @GetMapping("/")
    public String main(Model model, HttpSession httpSession) {

        Gson gson = new Gson();
        List<String> books = legacyService.listAll();
        List<LegacyController.Book> bookList = books.stream().map(str -> gson.fromJson(str, LegacyController.Book.class))
                .collect(Collectors.toList());

        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", bookList);

        log.info("httpSession id: {}", httpSession.getId());

        return "welcome"; //view
    }

    @GetMapping("/hello")
    public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name,
                                Model model, HttpSession httpSession) {
        List<String> books = legacyService.listAll();

        model.addAttribute("message", name);
        model.addAttribute("tasks", tasks);
        model.addAttribute("books", books);

        log.info("httpSession id: {}", httpSession.getId());

        return "welcome"; //view
    }

}
