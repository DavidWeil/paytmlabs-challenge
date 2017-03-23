package com.example.data;

import com.example.data.deserializers.StoreDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Store} class.
 */
public class StoreTest {

    private static final String STORE_JSON = "{" +
        "\"id\": 511," +
        "\"is_dead\": false," +
        "\"name\": \"King \u0026 Spadina\"," +
        "\"tags\": \"king spadina 415 street west toronto central toronto-central torontocentral m5v1k1\"," +
        "\"address_line_1\": \"415 King Street West\"," +
        "\"address_line_2\": null," +
        "\"city\": \"Toronto\"," +
        "\"postal_code\": \"M5V1K1\"," +
        "\"telephone\": \"(416) 598-1482\"," +
        "\"fax\": \"(416) 598-3716\"," +
        "\"latitude\": 43.6453," +
        "\"longitude\": -79.3946," +
        "\"products_count\": 3150," +
        "\"inventory_count\": 77271," +
        "\"inventory_price_in_cents\": 132179550," +
        "\"inventory_volume_in_milliliters\": 54393028," +
        "\"has_wheelchair_accessability\": true," +
        "\"has_bilingual_services\": true," +
        "\"has_product_consultant\": true," +
        "\"has_tasting_bar\": true," +
        "\"has_beer_cold_room\": false," +
        "\"has_special_occasion_permits\": false," +
        "\"has_vintages_corner\": true," +
        "\"has_parking\": false," +
        "\"has_transit_access\": false," +
        "\"sunday_open\": 660," +
        "\"sunday_close\": 1080," +
        "\"monday_open\": 570," +
        "\"monday_close\": 1380," +
        "\"tuesday_open\": 570," +
        "\"tuesday_close\": 1380," +
        "\"wednesday_open\": 570," +
        "\"wednesday_close\": 1380," +
        "\"thursday_open\": 570," +
        "\"thursday_close\": 1380," +
        "\"friday_open\": 570," +
        "\"friday_close\": 1380," +
        "\"saturday_open\": 570," +
        "\"saturday_close\": 1380," +
        "\"updated_at\": \"2017-03-22T14:15:20.360Z\"," +
        "\"store_no\": 511" +
    "}";

    @Test
    public void testDeserializeStore() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Store.class, new StoreDeserializer()).create();
        Store store = gson.fromJson(STORE_JSON, Store.class);
        assertEquals(511, store.getId());
        assertEquals("Toronto", store.getCity());
        assertNull(store.getAddress2());
        assertTrue(store.isAccessible());
    }

    private static final String RESPONSE_JSON = "{\"status\":200,\"message\":null,\"result\":{\"id\":511,\"is_dead\":false," +
            "\"name\":\"King \u0026 Spadina\",\"tags\":\"king spadina 415 street west toronto central toronto-central torontocentral m5v1k1\"," +
            "\"address_line_1\":\"415 King Street West\",\"address_line_2\":null,\"city\":\"Toronto\",\"postal_code\":\"M5V1K1\"," +
            "\"telephone\":\"(416) 598-1482\",\"fax\":\"(416) 598-3716\",\"latitude\":43.6453,\"longitude\":-79.3946," +
            "\"products_count\":3150,\"inventory_count\":77271,\"inventory_price_in_cents\":132179550,\"inventory_volume_in_milliliters\":54393028," +
            "\"has_wheelchair_accessability\":true,\"has_bilingual_services\":true,\"has_product_consultant\":true," +
            "\"has_tasting_bar\":true,\"has_beer_cold_room\":false,\"has_special_occasion_permits\":false,\"has_vintages_corner\":true," +
            "\"has_parking\":false,\"has_transit_access\":false,\"sunday_open\":660,\"sunday_close\":1080,\"monday_open\":570," +
            "\"monday_close\":1380,\"tuesday_open\":570,\"tuesday_close\":1380,\"wednesday_open\":570,\"wednesday_close\":1380," +
            "\"thursday_open\":570,\"thursday_close\":1380,\"friday_open\":570,\"friday_close\":1380,\"saturday_open\":570," +
            "\"saturday_close\":1380,\"updated_at\":\"2017-03-22T14:15:20.360Z\",\"store_no\":511}}";

    @Test
    public void testDeserializeResponse() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Store.class, new StoreDeserializer()).create();
        Type responseType = new TypeToken<SimpleResponse<Store>>(){}.getType();
        SimpleResponse<Store> resp = gson.fromJson(RESPONSE_JSON, responseType);
        assertEquals(200, resp.getStatus());

        Store store = resp.getResult();
        assertEquals(511, store.getId());
        assertEquals("Toronto", store.getCity());
        assertNull(store.getAddress2());
        assertTrue(store.isAccessible());
    }

    private static final String ERROR_RESPONSE_JSON = "{\"status\":404,\"message\":\"No store exists with id: 00000.\"," +
            "\"result\":null,\"error\":\"not_found_error\"}";

    @Test
    public void testDeserializeErrorResponse() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Store.class, new StoreDeserializer()).create();
        Type responseType = new TypeToken<SimpleResponse<Store>>(){}.getType();
        SimpleResponse<Store> response = gson.fromJson(ERROR_RESPONSE_JSON, responseType);
        assertEquals(404, response.getStatus());
        assertNotNull(response.getMessage());
        assertNotNull(response.getError());
        assertNull(response.getResult());
    }
}