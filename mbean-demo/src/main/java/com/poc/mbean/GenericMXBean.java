package com.poc.mbean;

import java.util.Map;

public interface GenericMXBean {

    Map<String, String> getProperties(Map<String, String[]> properties);
    Map<String, String> setProperties(Map<String, String> properties);
}
