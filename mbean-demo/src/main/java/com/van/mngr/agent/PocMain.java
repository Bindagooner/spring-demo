package com.van.mngr.agent;

import com.van.mngr.agent.mbean.PropertyMXBean;
import org.jolokia.jvmagent.JolokiaServer;
import org.jolokia.jvmagent.JvmAgentConfig;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class PocMain {

    private JolokiaServer jolokiaServer;

    public static void main(String[] args) throws Exception {

    }

    public void initServer() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.van.mngr.agent.mbean:name=PropertyMXBean");
        PropertyMXBean mBean = new PropertyMXBean();
        mbs.registerMBean(mBean, name);

        jolokiaServer = new JolokiaServer(new JvmAgentConfig("host=*,port=8080"), false);
    }

    public void startServer() {
        jolokiaServer.start();
    }

    public void stopServer() {
        jolokiaServer.stop();
    }
}
