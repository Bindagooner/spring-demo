package com.van.mngr.integration.server;

import org.springframework.web.server.adapter.AbstractReactiveWebInitializer;

/**
 * This class is needed when you run reactive webapp as deployable war
 */
public class AppInitializer extends AbstractReactiveWebInitializer {

    @Override
    protected Class<?>[] getConfigClasses() {
        return new Class[]{
                AppConfig.class,
                WebConfig.class
        };
    }

}
