package com.example.data.deserializers;

import com.example.data.Pager;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializes a SimpleResponse object, handling the special error case.
 */
public class PagerDeserializer implements JsonDeserializer<Pager> {

    @Override
    public Pager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = (JsonObject) json;
        return new Pager(
            obj.get("records_per_page").getAsInt(),
            obj.get("total_record_count").getAsInt(),
            obj.get("current_page").getAsInt(),
            obj.get("total_pages").getAsInt()
        );
    }
}
