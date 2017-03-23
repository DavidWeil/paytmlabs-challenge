package com.example.data.deserializers;

import com.example.data.ListResponse;
import com.example.data.Pager;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes a ListResponse object, handling the special error case.
 */
public class ListResponseDeserializer<T> implements JsonDeserializer<ListResponse<T>> {

    @Override
    public ListResponse<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = (JsonObject) json;
        int status = obj.get("status").getAsInt();
        if (status == 200) {
            Pager pager = context.deserialize(obj.getAsJsonObject("pager"), Pager.class);
            JsonArray resultObjs = obj.getAsJsonArray("result");
            Type tType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            List<T> result = new ArrayList<T>(resultObjs.size());
            for (int i = 0; i < resultObjs.size(); i++) {
                JsonObject entry = (JsonObject) resultObjs.get(i);
                T value = context.deserialize(entry, tType);
                result.add(value);
            }

            return new ListResponse<T>(
                    status,
                    getNullableString(obj.get("message")),
                    pager,
                    result,
                    null
            );

        }
        else {
            return new ListResponse<T>(
                    status,
                    getNullableString(obj.get("message")),
                    null,
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
