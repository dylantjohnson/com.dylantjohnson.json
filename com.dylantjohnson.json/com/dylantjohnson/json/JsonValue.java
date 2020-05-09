package com.dylantjohnson.json;

import java.math.*;
import java.util.*;

public class JsonValue {
    private String stringValue = null;
    private BigDecimal numberValue = null;
    private JsonObject objectValue = null;
    private List<JsonValue> listValue = null;

    public JsonValue() {}

    public JsonValue(String value) {
        Utils.assertNotNull(value);
        stringValue = value;
    }

    public JsonValue(BigDecimal value) {
        Utils.assertNotNull(value);
        numberValue = value;
    }

    public JsonValue(JsonObject value) {
        Utils.assertNotNull(value);
        objectValue = value;
    }

    public JsonValue(List<JsonValue> value) {
        Utils.assertNotNull(value);
        listValue = Collections.unmodifiableList(value);
    }

    public boolean isNull() {
        return stringValue == null && numberValue == null && objectValue == null && listValue == null;
    }

    public Optional<String> getStringValue() {
        return Optional.ofNullable(stringValue);
    }

    public Optional<BigDecimal> getNumberValue() {
        return Optional.ofNullable(numberValue);
    }

    public Optional<JsonObject> getObjectValue() {
        return Optional.ofNullable(objectValue);
    }

    public Optional<List<JsonValue>> getListValue() {
        return Optional.ofNullable(listValue);
    }

    @Override
    public String toString() {
        if (stringValue != null) {
            return String.format("\"%s\"", stringValue);
        } else if (numberValue != null) {
            return numberValue.toString();
        } else if (objectValue != null) {
            return objectValue.toString();
        } else if (listValue != null) {
            return toListString();
        } else {
            return "null";
        }
    }

    private String toListString() {
        var stringJoiner = new StringJoiner(",", "[", "]");
        for (var jsonValue : listValue) {
            stringJoiner.add(jsonValue.toString());
        }
        return stringJoiner.toString();
    }
}
