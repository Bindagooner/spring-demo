package neospider.mngr.mvc.services;

import com.google.gson.Gson;
import neospider.mngr.mvc.clients.BusinessTierClient;
import neospider.mngr.mvc.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Autowired
    private BusinessTierClient client;
    @Autowired private Gson gson;
    public List<Book> listAll() {
        Map<String, Object> map = client.listAllBooks();
        List<Book> data = (List) map.get("data");
        if (data == null && data.size() == 0) {
            return Collections.emptyList();
        }
        return data;
    }

    public Book saveBook(Book book) {
        Map<String, String> req = new HashMap<>();
        req.put("name", book.getName());
        req.put("author", book.getAuthor());
        Map<String, Object> response = client.saveBook(req);
        Map<String, String> data = (Map) response.get("data");
        return Book.builder().id(data.get("id")).name(data.get("name")).author(data.get("author")).build();
    }
}
