package com.van.mngr.bt.controllers;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    private Gson gson;

    BaseController(Gson gson) {
        this.gson = gson;
    }

    /**
     *
     * @param object
     * @return OK ResponseEntity.
     */
    ResponseEntity<Map> buildMapSuccessResponse(Object object) {
        Map<String, Object> result = new HashMap<>();
        JsonElement jsonElement = gson.toJsonTree(object);
        Object data = null;
        if (jsonElement.isJsonArray()) {
            data = gson.fromJson(jsonElement, List.class);
        } else {
            data = gson.fromJson(jsonElement, Map.class);
        }
        result.put("data", data);
        result.put("status", "SUCCESS");
        return ResponseEntity.ok(result);
    }
}
