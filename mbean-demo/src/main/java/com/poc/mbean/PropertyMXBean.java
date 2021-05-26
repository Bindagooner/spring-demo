package com.poc.mbean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PropertyMXBean implements GenericMXBean {

    private Map<String, String> properties = new HashMap<>();

    public PropertyMXBean() {
        properties.put("prop1", "aaaaa");
        properties.put("prop2", "bbbbb");
        properties.put("prop3", "12345");
    }

    /**
     *
     * @param input properties = ["xxx", "yyy"]
     * @return
     */
    @Override
    public Map<String, String> getProperties(Map<String, String[]> input) {
        String[] names = input.get("properties");
        Map<String, String> result = new HashMap<>();
        Arrays.stream(names).forEach(prop -> {
            String v = this.properties.get(prop);
            if (v != null) {
                result.put(prop, v);
            }
        });
        return result;
    }

    @Override
    public Map<String, String> setProperties(Map<String, String> input) {
        this.properties.putAll(input);
        return this.properties;
    }
}
