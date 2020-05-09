package com.dylantjohnson.json;

import java.util.*;

public class JsonObject {
    private Map<String, JsonValue> mapValue;

    public JsonObject(Map<String, JsonValue> value) {
        Utils.assertNotNull(value);
        mapValue = Collections.unmodifiableMap(value);
    }

    public Optional<JsonValue> getValue(String key) {
        return Optional.ofNullable(mapValue.get(key));
    }

    public Set<String> getKeys() {
        return mapValue.keySet();
    }

    @Override
    public String toString() {
        var stringJoiner = new StringJoiner(",", "{", "}");
        for (var key : mapValue.keySet()) {
            stringJoiner.add(String.format("\"%s\":%s", key, mapValue.get(key)));
        }
        return stringJoiner.toString();
    }
}
