package com.example.data;

import com.example.data.deserializers.ListResponseDeserializer;
import com.example.data.deserializers.PagerDeserializer;
import com.example.data.deserializers.ProductDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ListResponse} class.
 */
public class ListResponseTest {

    private static final String PAGER_JSON = "{" +
        "\"records_per_page\": 20," +
        "\"total_record_count\": 654," +
        "\"current_page_record_count\": 20," +
        "\"is_first_page\": true," +
        "\"is_final_page\": false," +
        "\"current_page\": 1," +
        "\"current_page_path\": \"/stores?page=1\"," +
        "\"next_page\": 2," +
        "\"next_page_path\": \"/stores?page=2\"," +
        "\"previous_page\": null," +
        "\"previous_page_path\": null," +
        "\"total_pages\": 33," +
        "\"total_pages_path\": \"/stores?page=33\"" +
    "}";

    @Test
    public void testDeserializePager() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Pager.class, new PagerDeserializer()).create();
        Pager pager = gson.fromJson(PAGER_JSON, Pager.class);
        assertEquals(20, pager.getPageSize());
        assertEquals(1, pager.getCurrentPage());
        assertEquals(33, pager.getLastPage());
        assertEquals(654, pager.getTotal());
    }

    private static final String ERROR_JSON = "{\"status\":400,\"message\":\"The value suppled for the page parameter(0) " +
            "is not valid. It must be a number greater than zero.\",\"result\":null,\"error\":\"bad_query_error\"}";

    @Test
    public void testDeserializeError() {
        Gson gson = new GsonBuilder().registerTypeAdapter(ListResponse.class, new ListResponseDeserializer()).create();
        Type responseType = new TypeToken<ListResponse<Store>>(){}.getType();
        ListResponse<Store> response = gson.fromJson(ERROR_JSON, responseType);
        assertEquals(400, response.getStatus());
        assertNotNull(response.getMessage());
        assertEquals("bad_query_error", response.getError());
        assertNull(response.getPager());
        assertNull(response.getResults());
    }

    private static final String PRODUCTS_JSON =
        "{\"status\":200,\"message\":null,\"pager\":{\"records_per_page\":20,\"total_record_count\":11455,\"current_page_record_count\":20," +
        "\"is_first_page\":true,\"is_final_page\":false,\"current_page\":1,\"current_page_path\":\"/products?page=1\",\"next_page\":2," +
        "\"next_page_path\":\"/products?page=2\",\"previous_page\":null,\"previous_page_path\":null,\"total_pages\":573," +
        "\"total_pages_path\":\"/products?page=573\"},\"result\":[{\"id\":300681,\"is_dead\":false,\"name\":\"Coors Light\"," +
        "\"tags\":\"coors light beer lager canada ontario molson's molsons brewery of limited can\",\"is_discontinued\":false," +
        "\"price_in_cents\":1450,\"regular_price_in_cents\":1450,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"6x473 mL can\",\"package_unit_type\":\"can\"," +
        "\"package_unit_volume_in_milliliters\":473,\"total_package_units\":6,\"volume_in_milliliters\":2838,\"alcohol_content\":400," +
        "\"price_per_liter_of_alcohol_in_cents\":1277,\"price_per_liter_in_cents\":510,\"inventory_count\":59746," +
        "\"inventory_volume_in_milliliters\":169559148,\"inventory_price_in_cents\":86631700,\"sugar_content\":null," +
        "\"producer_name\":\"Molson's Brewery of Canada Limited\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:28:33.278Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/300681/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/300681/images/full.jpeg\",\"varietal\":\"Light Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Light Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":300681},{\"id\":311787,\"is_dead\":false," +
        "\"name\":\"Budweiser\",\"tags\":\"budweiser beer lager canada ontario labatt breweries can\",\"is_discontinued\":false," +
        "\"price_in_cents\":1425,\"regular_price_in_cents\":1425,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"6x473 mL can\",\"package_unit_type\":\"can\"," +
        "\"package_unit_volume_in_milliliters\":473,\"total_package_units\":6,\"volume_in_milliliters\":2838,\"alcohol_content\":500," +
        "\"price_per_liter_of_alcohol_in_cents\":1004,\"price_per_liter_in_cents\":502,\"inventory_count\":45882," +
        "\"inventory_volume_in_milliliters\":130213116,\"inventory_price_in_cents\":65381850,\"sugar_content\":null," +
        "\"producer_name\":\"Labatt Breweries Ontario\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":false," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:23:18.383Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/311787/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/311787/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":311787},{\"id\":300699,\"is_dead\":false," +
        "\"name\":\"Molson Canadian\",\"tags\":\"molson canadian beer lager canada ontario molson's molsons brewery of limited can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1425,\"regular_price_in_cents\":1425,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":5,\"bonus_reward_miles_ends_on\":\"2017-03-25\",\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"6x473 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":473,\"total_package_units\":6,\"volume_in_milliliters\":2838," +
        "\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":1004,\"price_per_liter_in_cents\":502,\"inventory_count\":45390," +
        "\"inventory_volume_in_milliliters\":128816820,\"inventory_price_in_cents\":64680750,\"sugar_content\":null," +
        "\"producer_name\":\"Molson's Brewery of Canada Limited\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":true,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:28:52.933Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/300699/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/300699/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":300699},{\"id\":696161,\"is_dead\":false," +
        "\"name\":\"Busch\",\"tags\":\"busch beer lager canada ontario labatt breweries can\",\"is_discontinued\":false,\"price_in_cents\":1225," +
        "\"regular_price_in_cents\":1225,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0," +
        "\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\"," +
        "\"origin\":\"Canada, Ontario\",\"package\":\"6x473 mL can\",\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":473," +
        "\"total_package_units\":6,\"volume_in_milliliters\":2838,\"alcohol_content\":470,\"price_per_liter_of_alcohol_in_cents\":918," +
        "\"price_per_liter_in_cents\":431,\"inventory_count\":34786,\"inventory_volume_in_milliliters\":98722668," +
        "\"inventory_price_in_cents\":42612850,\"sugar_content\":null,\"producer_name\":\"Labatt Breweries Ontario\",\"released_on\":null," +
        "\"has_value_added_promotion\":false,\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false," +
        "\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null," +
        "\"serving_suggestion\":null,\"tasting_note\":null,\"updated_at\":\"2017-03-22T14:23:30.988Z\"," +
        "\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/696161/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/696161/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":696161},{\"id\":311779,\"is_dead\":false," +
        "\"name\":\"Bud Light\",\"tags\":\"bud light beer lager canada ontario labatt breweries can\",\"is_discontinued\":false," +
        "\"price_in_cents\":1450,\"regular_price_in_cents\":1450,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"6x473 mL can\",\"package_unit_type\":\"can\"," +
        "\"package_unit_volume_in_milliliters\":473,\"total_package_units\":6,\"volume_in_milliliters\":2838,\"alcohol_content\":400," +
        "\"price_per_liter_of_alcohol_in_cents\":1277,\"price_per_liter_in_cents\":510,\"inventory_count\":30944," +
        "\"inventory_volume_in_milliliters\":87819072,\"inventory_price_in_cents\":44868800,\"sugar_content\":null," +
        "\"producer_name\":\"Labatt Breweries Ontario\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:23:13.513Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/311779/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/311779/images/full.jpeg\",\"varietal\":\"Light Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Light Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":311779},{\"id\":696468,\"is_dead\":false," +
        "\"name\":\"Labatt Blue\",\"tags\":\"labatt blue beer lager canada ontario breweries can\",\"is_discontinued\":false," +
        "\"price_in_cents\":210,\"regular_price_in_cents\":210,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"473 mL can\",\"package_unit_type\":\"can\"," +
        "\"package_unit_volume_in_milliliters\":473,\"total_package_units\":1,\"volume_in_milliliters\":473,\"alcohol_content\":500," +
        "\"price_per_liter_of_alcohol_in_cents\":887,\"price_per_liter_in_cents\":443,\"inventory_count\":158505," +
        "\"inventory_volume_in_milliliters\":74972865,\"inventory_price_in_cents\":33286050,\"sugar_content\":null," +
        "\"producer_name\":\"Labatt Breweries Ontario\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":false," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:26:50.228Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/696468/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/696468/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":696468},{\"id\":186510,\"is_dead\":false," +
        "\"name\":\"Corona Extra\",\"tags\":\"corona extra beer lager mexico region not specified cerveceria modelo sa de cv bottle\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1450,\"regular_price_in_cents\":1450,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Mexico, Region Not Specified\",\"package\":\"6x330 mL bottle\"," +
        "\"package_unit_type\":\"bottle\",\"package_unit_volume_in_milliliters\":330,\"total_package_units\":6,\"volume_in_milliliters\":1980," +
        "\"alcohol_content\":459,\"price_per_liter_of_alcohol_in_cents\":1595,\"price_per_liter_in_cents\":732,\"inventory_count\":36817," +
        "\"inventory_volume_in_milliliters\":72897660,\"inventory_price_in_cents\":53384650,\"sugar_content\":null," +
        "\"producer_name\":\"Cerveceria Modelo Sa de Cv\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:28:39.954Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/186510/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/186510/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":186510},{\"id\":586677,\"is_dead\":false," +
        "\"name\":\"Old Milwaukee Ice\",\"tags\":\"old milwaukee ice beer lager canada ontario sleeman brewing malting co can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1225,\"regular_price_in_cents\":1225,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"6x473 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":473,\"total_package_units\":6,\"volume_in_milliliters\":2838," +
        "\"alcohol_content\":550,\"price_per_liter_of_alcohol_in_cents\":784,\"price_per_liter_in_cents\":431,\"inventory_count\":22332," +
        "\"inventory_volume_in_milliliters\":63378216,\"inventory_price_in_cents\":27356700,\"sugar_content\":null," +
        "\"producer_name\":\"Sleeman Brewing \u0026 Malting Co\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:29:39.474Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/586677/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/586677/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":586677},{\"id\":302810,\"is_dead\":false," +
        "\"name\":\"Heineken\",\"tags\":\"heineken beer lager netherlands region not specified heineken's heinekens brouwerijen nederland bv can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1475,\"regular_price_in_cents\":1575,\"limited_time_offer_savings_in_cents\":100," +
        "\"limited_time_offer_ends_on\":\"2017-03-26\",\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Netherlands, Region Not Specified\"," +
        "\"package\":\"6x500 mL can\",\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":500,\"total_package_units\":6," +
        "\"volume_in_milliliters\":3000,\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":983,\"price_per_liter_in_cents\":491," +
        "\"inventory_count\":20734,\"inventory_volume_in_milliliters\":62202000,\"inventory_price_in_cents\":30582650,\"sugar_content\":null," +
        "\"producer_name\":\"Heineken's Brouwerijen Nederland BV\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":true,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:37:01.780Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/302810/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/302810/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Light \u0026 Hoppy\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":302810},{\"id\":367938,\"is_dead\":false," +
        "\"name\":\"Stella Artois\",\"tags\":\"stella artois beer lager belgium region not specified labatt breweries ontario can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1595,\"regular_price_in_cents\":1595,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Belgium, Region Not Specified\",\"package\":\"6x500 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":500,\"total_package_units\":6,\"volume_in_milliliters\":3000," +
        "\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":1063,\"price_per_liter_in_cents\":531,\"inventory_count\":20035," +
        "\"inventory_volume_in_milliliters\":60105000,\"inventory_price_in_cents\":31955825,\"sugar_content\":null," +
        "\"producer_name\":\"Labatt Breweries Ontario\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":false," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:33:35.653Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/367938/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/367938/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":367938},{\"id\":18,\"is_dead\":false," +
        "\"name\":\"Heineken\",\"tags\":\"heineken beer lager netherlands region not specified heineken's heinekens brouwerijen nederland bv bottle\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1450,\"regular_price_in_cents\":1450,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Netherlands, Region Not Specified\"," +
        "\"package\":\"6x330 mL bottle\",\"package_unit_type\":\"bottle\",\"package_unit_volume_in_milliliters\":330,\"total_package_units\":6," +
        "\"volume_in_milliliters\":1980,\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":1464,\"price_per_liter_in_cents\":732," +
        "\"inventory_count\":28009,\"inventory_volume_in_milliliters\":55457820,\"inventory_price_in_cents\":40613050,\"sugar_content\":null," +
        "\"producer_name\":\"Heineken's Brouwerijen Nederland BV\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:37:01.341Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/18/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/18/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Light \u0026 Hoppy\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":18},{\"id\":254946,\"is_dead\":false," +
        "\"name\":\"Dab Original\",\"tags\":\"dab original beer lager germany region not specified dortmunder actien brauerei kg can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":204,\"regular_price_in_cents\":204,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Germany, Region Not Specified\",\"package\":\"500 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":500,\"total_package_units\":1,\"volume_in_milliliters\":500," +
        "\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":816,\"price_per_liter_in_cents\":408,\"inventory_count\":108488," +
        "\"inventory_volume_in_milliliters\":54244000,\"inventory_price_in_cents\":22131552,\"sugar_content\":null," +
        "\"producer_name\":\"Dortmunder Actien Brauerei Kg\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:29:32.200Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/254946/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/254946/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Light \u0026 Floral\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":254946},{\"id\":142620,\"is_dead\":false," +
        "\"name\":\"Laker Ice\",\"tags\":\"laker ice beer lager canada ontario the brick brewing co can\",\"is_discontinued\":false," +
        "\"price_in_cents\":195,\"regular_price_in_cents\":195,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"473 mL can\",\"package_unit_type\":\"can\"," +
        "\"package_unit_volume_in_milliliters\":473,\"total_package_units\":1,\"volume_in_milliliters\":473,\"alcohol_content\":550," +
        "\"price_per_liter_of_alcohol_in_cents\":749,\"price_per_liter_in_cents\":412,\"inventory_count\":109184," +
        "\"inventory_volume_in_milliliters\":51644032,\"inventory_price_in_cents\":21290880,\"sugar_content\":null," +
        "\"producer_name\":\"The Brick Brewing Co\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":false," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:26:56.427Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/142620/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/142620/images/full.jpeg\",\"varietal\":\"Strong Lager\"," +
        "\"style\":\"Light \u0026 Hoppy\",\"tertiary_category\":\"Strong Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":142620},{\"id\":487256,\"is_dead\":false," +
        "\"name\":\"Stella Artois\",\"tags\":\"stella artois beer lager belgium region not specified nvinterbrew bottle\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1325,\"regular_price_in_cents\":1425,\"limited_time_offer_savings_in_cents\":100," +
        "\"limited_time_offer_ends_on\":\"2017-03-26\",\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Belgium, Region Not Specified\"," +
        "\"package\":\"6x330 mL bottle\",\"package_unit_type\":\"bottle\",\"package_unit_volume_in_milliliters\":330,\"total_package_units\":6," +
        "\"volume_in_milliliters\":1980,\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":1338,\"price_per_liter_in_cents\":669," +
        "\"inventory_count\":24000,\"inventory_volume_in_milliliters\":47520000,\"inventory_price_in_cents\":31800000,\"sugar_content\":null," +
        "\"producer_name\":\"N.V.INTERBREW. Belgium\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":true," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:33:35.441Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/487256/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/487256/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":487256},{\"id\":397638,\"is_dead\":false," +
        "\"name\":\"Lowenbrau Original\",\"tags\":\"lowenbrau original beer lager canada ontario labatt breweries can\",\"is_discontinued\":false," +
        "\"price_in_cents\":204,\"regular_price_in_cents\":204,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"473 mL can\",\"package_unit_type\":\"can\"," +
        "\"package_unit_volume_in_milliliters\":473,\"total_package_units\":1,\"volume_in_milliliters\":473,\"alcohol_content\":520," +
        "\"price_per_liter_of_alcohol_in_cents\":829,\"price_per_liter_in_cents\":431,\"inventory_count\":98914," +
        "\"inventory_volume_in_milliliters\":46786322,\"inventory_price_in_cents\":20178456,\"sugar_content\":null," +
        "\"producer_name\":\"Labatt Breweries Ontario\",\"released_on\":\"2016-04-18\",\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null," +
        "\"serving_suggestion\":\"Pair with grilled sausage or burgers\"," +
        "\"tasting_note\":\"Brewed to the original Lowenbrau recipe that dates back to 1383; it pours a pale straw colour with a thin head. The nose is fragrant with Hallertau hops providing citrus/grassy notes while the palate is light with average carbonation, balanced citrus bitterness and a touch of malt sweetness followed by a crisp finish.\"," +
        "\"updated_at\":\"2017-03-21T14:27:39.508Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/397638/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/397638/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Light \u0026 Hoppy\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":397638},{\"id\":279307,\"is_dead\":false," +
        "\"name\":\"Somersby Apple Cider\",\"tags\":\"somersby apple cider ciders denmark region not specified carlsberg canada inc can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":310,\"regular_price_in_cents\":310,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Ciders\",\"secondary_category\":null,\"origin\":\"Denmark, Region Not Specified\",\"package\":\"500 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":500,\"total_package_units\":1,\"volume_in_milliliters\":500," +
        "\"alcohol_content\":450,\"price_per_liter_of_alcohol_in_cents\":1377,\"price_per_liter_in_cents\":620,\"inventory_count\":88077," +
        "\"inventory_volume_in_milliliters\":44038500,\"inventory_price_in_cents\":27303870,\"sugar_content\":null," +
        "\"producer_name\":\"Carlsberg Canada Inc.\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":false," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:33:15.683Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/279307/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/279307/images/full.jpeg\",\"varietal\":null," +
        "\"style\":\"Sweet \u0026 Fruity\",\"tertiary_category\":null,\"sugar_in_grams_per_liter\":null,\"clearance_sale_savings_in_cents\":0," +
        "\"has_clearance_sale\":false,\"product_no\":279307},{\"id\":535294,\"is_dead\":false,\"name\":\"Grolsch Premium Lager\"," +
        "\"tags\":\"grolsch premium lager beer netherlands region not specified grolsche bierbrouwerij bv can\",\"is_discontinued\":false," +
        "\"price_in_cents\":235,\"regular_price_in_cents\":235,\"limited_time_offer_savings_in_cents\":0,\"limited_time_offer_ends_on\":null," +
        "\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\",\"primary_category\":\"Beer\"," +
        "\"secondary_category\":\"Lager\",\"origin\":\"Netherlands, Region Not Specified\",\"package\":\"500 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":500,\"total_package_units\":1,\"volume_in_milliliters\":500," +
        "\"alcohol_content\":500,\"price_per_liter_of_alcohol_in_cents\":940,\"price_per_liter_in_cents\":470,\"inventory_count\":87909," +
        "\"inventory_volume_in_milliliters\":43954500,\"inventory_price_in_cents\":20658615,\"sugar_content\":null," +
        "\"producer_name\":\"Grolsche Bierbrouwerij B.V.\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":false,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:36:17.164Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/535294/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/535294/images/full.jpeg\",\"varietal\":\"European Lager\"," +
        "\"style\":\"Medium \u0026 Hoppy\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":535294},{\"id\":906669,\"is_dead\":false," +
        "\"name\":\"Coors Light\",\"tags\":\"coors light beer lager canada ontario molson's molsons brewery of limited can\"," +
        "\"is_discontinued\":false,\"price_in_cents\":229,\"regular_price_in_cents\":250,\"limited_time_offer_savings_in_cents\":21," +
        "\"limited_time_offer_ends_on\":\"2017-03-26\",\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"Canada, Ontario\",\"package\":\"473 mL can\"," +
        "\"package_unit_type\":\"can\",\"package_unit_volume_in_milliliters\":473,\"total_package_units\":1,\"volume_in_milliliters\":473," +
        "\"alcohol_content\":400,\"price_per_liter_of_alcohol_in_cents\":1210,\"price_per_liter_in_cents\":484,\"inventory_count\":89655," +
        "\"inventory_volume_in_milliliters\":42406815,\"inventory_price_in_cents\":20530995,\"sugar_content\":null," +
        "\"producer_name\":\"Molson's Brewery of Canada Limited\",\"released_on\":null,\"has_value_added_promotion\":false," +
        "\"has_limited_time_offer\":true,\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false," +
        "\"is_kosher\":false,\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-22T14:28:32.602Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/906669/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/906669/images/full.jpeg\",\"varietal\":\"Light Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Light Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":906669},{\"id\":693093,\"is_dead\":false," +
        "\"name\":\"Miller Genuine Draft\",\"tags\":\"miller genuine draft beer lager usa wisconsin brewing company bottle\"," +
        "\"is_discontinued\":false,\"price_in_cents\":1295,\"regular_price_in_cents\":1295,\"limited_time_offer_savings_in_cents\":0," +
        "\"limited_time_offer_ends_on\":null,\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Beer\",\"secondary_category\":\"Lager\",\"origin\":\"USA, Wisconsin\",\"package\":\"6x355 mL bottle\"," +
        "\"package_unit_type\":\"bottle\",\"package_unit_volume_in_milliliters\":355,\"total_package_units\":6,\"volume_in_milliliters\":2130," +
        "\"alcohol_content\":470,\"price_per_liter_of_alcohol_in_cents\":1293,\"price_per_liter_in_cents\":607,\"inventory_count\":19263," +
        "\"inventory_volume_in_milliliters\":41030190,\"inventory_price_in_cents\":24945585,\"sugar_content\":null," +
        "\"producer_name\":\"Miller Brewing Company\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":false," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:28:44.942Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/693093/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/693093/images/full.jpeg\",\"varietal\":\"North American Lager\"," +
        "\"style\":\"Light \u0026 Malty\",\"tertiary_category\":\"Pale Lager\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":693093},{\"id\":38505,\"is_dead\":false," +
        "\"name\":\"Smirnoff Vodka\",\"tags\":\"smirnoff vodka spirits canada region not specified schenley diageo bottle\"," +
        "\"is_discontinued\":false,\"price_in_cents\":5905,\"regular_price_in_cents\":6005,\"limited_time_offer_savings_in_cents\":100," +
        "\"limited_time_offer_ends_on\":\"2017-03-26\",\"bonus_reward_miles\":0,\"bonus_reward_miles_ends_on\":null,\"stock_type\":\"LCBO\"," +
        "\"primary_category\":\"Spirits\",\"secondary_category\":\"Vodka\",\"origin\":\"Canada, Region Not Specified\"," +
        "\"package\":\"1750 mL bottle\",\"package_unit_type\":\"bottle\",\"package_unit_volume_in_milliliters\":1750,\"total_package_units\":1," +
        "\"volume_in_milliliters\":1750,\"alcohol_content\":4000,\"price_per_liter_of_alcohol_in_cents\":843,\"price_per_liter_in_cents\":3374," +
        "\"inventory_count\":23309,\"inventory_volume_in_milliliters\":40790750,\"inventory_price_in_cents\":137639645,\"sugar_content\":null," +
        "\"producer_name\":\"Schenley / Diageo\",\"released_on\":null,\"has_value_added_promotion\":false,\"has_limited_time_offer\":true," +
        "\"has_bonus_reward_miles\":false,\"is_seasonal\":false,\"is_vqa\":false,\"is_ocb\":false,\"is_kosher\":false," +
        "\"value_added_promotion_description\":null,\"description\":null,\"serving_suggestion\":null,\"tasting_note\":null," +
        "\"updated_at\":\"2017-03-21T14:33:10.421Z\",\"image_thumb_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/38505/images/thumb.png\"," +
        "\"image_url\":\"https://dx5vpyka4lqst.cloudfront.net/products/38505/images/full.jpeg\",\"varietal\":\"Corn\"," +
        "\"style\":\"Clean \u0026 Classic\",\"tertiary_category\":\"Unflavoured Vodka\",\"sugar_in_grams_per_liter\":null," +
        "\"clearance_sale_savings_in_cents\":0,\"has_clearance_sale\":false,\"product_no\":38505}],\"suggestion\":null}";


    @Test
    public void testDeserializeListResponse() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Pager.class, new PagerDeserializer())
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .registerTypeAdapter(ListResponse.class, new ListResponseDeserializer())
                .create();
        Type responseType = new TypeToken<ListResponse<Product>>(){}.getType();
        ListResponse<Product> response = gson.fromJson(PRODUCTS_JSON, responseType);
        assertEquals(200, response.getStatus());

        Pager pager = response.getPager();
        assertNotNull(pager);
        assertEquals(11455, pager.getTotal());

        List<Product> products = response.getResults();
        assertNotNull(products);
        assertEquals(20, products.size());

        Product product = products.get(0);
        assertEquals(300681, product.getId());
        assertEquals("Coors Light", product.getName());
    }
}