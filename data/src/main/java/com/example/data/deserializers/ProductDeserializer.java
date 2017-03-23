package com.example.data.deserializers;

import com.example.data.Product;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializes a Product object.
 */
public class ProductDeserializer implements JsonDeserializer<Product> {

    @Override
    public Product deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = (JsonObject) json;
        return new Product(
                obj.get("id").getAsInt(),
                obj.get("name").getAsString(),
                obj.get("price_in_cents").getAsInt(),
                obj.get("limited_time_offer_savings_in_cents").getAsInt(),
                obj.get("primary_category").getAsString(),
                getNullableString(obj.get("origin")),
                getNullableString(obj.get("package")),
                obj.get("volume_in_milliliters").getAsInt(),
                obj.get("inventory_count").getAsInt(),
                getNullableString(obj.get("producer_name")),
                getNullableString(obj.get("description")),
                getNullableString(obj.get("image_url")),
                getNullableString(obj.get("image_thumb_url"))
        );
    }

    private String getNullableString(JsonElement element) {
        if (element instanceof JsonNull) {
            return null;
        }
        return element.getAsString();
    }
}
