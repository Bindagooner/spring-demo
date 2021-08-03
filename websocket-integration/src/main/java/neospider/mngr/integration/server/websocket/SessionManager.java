package neospider.mngr.integration.server.websocket;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SessionManager {

    public static Set<String> sessions = new HashSet<>();

}
