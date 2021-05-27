package neospider.mngr.agent;

import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMBean {

    J4pClient j4pClient;
    PocMain main;

    @BeforeAll
    public void setUp() throws Exception {
        main = new PocMain();
        main.initServer();
        main.startServer();
        j4pClient = new J4pClient("http://localhost:8080/jolokia/");
    }

    @AfterAll
    public void tearDown() {
        main.stopServer();
    }

    @Test
    public void testSetProperties() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("prop123", "55555");
        input.put("prop1234", "12345");
        J4pExecRequest req = new J4pExecRequest("neospider.mngr.agent.mbean:name=PropertyMXBean",
                "setProperties", input);
        J4pExecResponse resp = j4pClient.execute(req, "POST");
        Map<String, String> output = resp.getValue();
        assertTrue(output.get("prop123").equals("55555"));
        assertTrue(output.get("prop1234").equals("12345"));
    }

    @Test
    public void testGetProperties() throws Exception {
        Map<String, String[]> input = new HashMap<>();
        input.put("properties", new String[] {"prop1", "prop2"});

        J4pExecRequest req = new J4pExecRequest("neospider.mngr.agent.mbean:name=PropertyMXBean",
                "getProperties", input);
        J4pExecResponse resp = j4pClient.execute(req, "POST");
        Map<String, String> output = resp.getValue();
        assertTrue(output.get("prop1").equals("aaaaa"));
        assertTrue(output.get("prop2").equals("bbbbb"));
    }
}
