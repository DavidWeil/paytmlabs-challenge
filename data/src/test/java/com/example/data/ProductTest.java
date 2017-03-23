package com.example.data;

import com.example.data.deserializers.ProductDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Product} class.
 */
public class ProductTest {

    private static final String JSON = "{" +
        "\"id\": 279307," +
        "\"is_dead\": false," +
        "\"name\": \"Somersby Apple Cider\"," +
        "\"tags\": \"somersby apple cider ciders denmark region not specified carlsberg canada inc can\"," +
        "\"is_discontinued\": false," +
        "\"price_in_cents\": 310," +
        "\"regular_price_in_cents\": 310," +
        "\"limited_time_offer_savings_in_cents\": 0," +
        "\"limited_time_offer_ends_on\": null," +
        "\"bonus_reward_miles\": 0," +
        "\"bonus_reward_miles_ends_on\": null," +
        "\"stock_type\": \"LCBO\"," +
        "\"primary_category\": \"Ciders\"," +
        "\"secondary_category\": null," +
        "\"origin\": \"Denmark, Region Not Specified\"," +
        "\"package\": \"500 mL can\"," +
        "\"package_unit_type\": \"can\"," +
        "\"package_unit_volume_in_milliliters\": 500," +
        "\"total_package_units\": 1," +
        "\"volume_in_milliliters\": 500," +
        "\"alcohol_content\": 450," +
        "\"price_per_liter_of_alcohol_in_cents\": 1377," +
        "\"price_per_liter_in_cents\": 620," +
        "\"inventory_count\": 88077," +
        "\"inventory_volume_in_milliliters\": 44038500," +
        "\"inventory_price_in_cents\": 27303870," +
        "\"sugar_content\": null," +
        "\"producer_name\": \"Carlsberg Canada Inc.\"," +
        "\"released_on\": null," +
        "\"has_value_added_promotion\": false," +
        "\"has_limited_time_offer\": false," +
        "\"has_bonus_reward_miles\": false," +
        "\"is_seasonal\": false," +
        "\"is_vqa\": false," +
        "\"is_ocb\": false," +
        "\"is_kosher\": false," +
        "\"value_added_promotion_description\": null," +
        "\"description\": null," +
        "\"serving_suggestion\": null," +
        "\"tasting_note\": null," +
        "\"updated_at\": \"2017-03-21T14:33:15.683Z\"," +
        "\"image_thumb_url\": \"https://dx5vpyka4lqst.cloudfront.net/products/279307/images/thumb.png\"," +
        "\"image_url\": \"https://dx5vpyka4lqst.cloudfront.net/products/279307/images/full.jpeg\"," +
        "\"varietal\": null," +
        "\"style\": \"Sweet \u0026 Fruity\"," +
        "\"tertiary_category\": null," +
        "\"sugar_in_grams_per_liter\": null," +
        "\"clearance_sale_savings_in_cents\": 0," +
        "\"has_clearance_sale\": false," +
        "\"product_no\": 279307" +
      "}";

    @Test
    public void testDeserialize() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductDeserializer()).create();
        Product prod = gson.fromJson(JSON, Product.class);
        assertEquals(310, prod.getPrice());
        assertEquals("Ciders", prod.getCategory());
        assertNull(prod.getDescription());
    }
}