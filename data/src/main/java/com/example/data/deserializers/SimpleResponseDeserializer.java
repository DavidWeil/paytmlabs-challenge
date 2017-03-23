package com.example.data.deserializers;

import com.example.data.SimpleResponse;
import com.google.gson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Deserializes a SimpleResponse object, handling the special error case.
 */
public class SimpleResponseDeserializer<T> implements JsonDeserializer<SimpleResponse<T>> {

    @Override
    public SimpleResponse<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = (JsonObject) json;
        int status = obj.get("status").getAsInt();
        if (status == 200) {
            Type tType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            T result = context.deserialize(obj.getAsJsonObject("result"), tType);
            return new SimpleResponse<T>(
                    status,
                    getNullableString(obj.get("message")),
                    result,
                    null
            );

        }
        else {
            return new SimpleResponse<T>(
                    status,
                    getNullableString(obj.get("message")),
                    null,
                    getNullableString(obj.get("error"))
            );
        }
    }

    private String getNullableString(JsonElement element) {
        if (element instanceof JsonNull) {
            return null;
        }
        return element.getAsString();
    }
}
