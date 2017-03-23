package com.example.data.deserializers;

import com.example.data.Store;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializes a Store object.
 */
public class StoreDeserializer implements JsonDeserializer<Store> {

    @Override
    public Store deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = (JsonObject) json;
        return new Store(
            obj.get("id").getAsInt(),
            obj.get("name").getAsString(),
            getNullableString(obj.get("address_line_1")),
            getNullableString(obj.get("address_line_2")),
            obj.get("city").getAsString(),
            obj.get("postal_code").getAsString(),
            obj.get("telephone").getAsString(),
            obj.get("has_wheelchair_accessability").getAsBoolean(),
            obj.get("has_vintages_corner").getAsBoolean()
        );
    }

    private String getNullableString(JsonElement element) {
        if (element instanceof JsonNull) {
            return null;
        }
        return element.getAsString();
    }
}
