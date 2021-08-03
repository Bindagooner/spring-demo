package neospider.mngr.integration.server.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@EnableScheduling
public class DataGenerator {

    private AtomicInteger count = new AtomicInteger(0);

    @Autowired
    DataService dataService;
    Random r=new Random();

    String[] stockSymbol = new String[] {"VIC", "HDB", "VHM", "BID", "CTG"};

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void scheduledSupplier() {
        log.info("scheduled running");
        Map<String, String> map = new HashMap<>();
        int randomNumber=r.nextInt(stockSymbol.length);
        map.put("symbol", stockSymbol[randomNumber]);
        map.put("price", String.valueOf(r.nextInt(1000)));
        dataService.onNext(map);
    }
}
