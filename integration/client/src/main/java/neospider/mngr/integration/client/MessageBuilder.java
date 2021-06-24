package neospider.mngr.integration.client;

import java.util.HashMap;
import java.util.Map;

public class MessageBuilder {

    public static Map<String, Object> buildMapMessage(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static Map<String, Object> appendMapMessage(Map<String, Object> message, String key, Object value) {
        message.put(key, value);
        return message;
    }
}
