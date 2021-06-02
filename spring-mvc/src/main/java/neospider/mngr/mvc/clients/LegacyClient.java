package neospider.mngr.mvc.clients;

import feign.Headers;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * this is for communication with legacy service.
 * use <a href="https://www.baeldung.com/intro-to-feign">Feign Client</a>.
 */
public interface LegacyClient {

    @RequestLine("POST /book")
    @Headers("Content-Type: application/json")
    Map<String, String> saveBook(Map<String, String> request);

    @RequestLine("GET /books")
    Map<String, List<String>> listAll();
}
