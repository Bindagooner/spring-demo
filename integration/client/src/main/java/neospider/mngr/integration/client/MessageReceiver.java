package neospider.mngr.integration.client;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class MessageReceiver {

    public static void handle(Map receivedData) {
        log.info("data received: {}", receivedData);
    }
}
