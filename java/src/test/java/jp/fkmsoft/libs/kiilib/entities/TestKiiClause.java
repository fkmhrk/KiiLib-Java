package jp.fkmsoft.libs.kiilib.entities;

import org.junit.Assert;
import org.junit.Test;

import org.json.JSONObject;

/**
 * Testcase for {@link jp.fkmsoft.libs.kiilib.entities.KiiClause}
 */
public class TestKiiClause {
    @Test
    public void test_0000_geoBox() {
        KiiClause clause = KiiClause.geoBox("myLocation", 30, 100, 31, 110);
        // Assertion
        JSONObject json = clause.toJson();
        Assert.assertEquals(3, json.length());
        Assert.assertEquals("geobox", json.optString("type"));
        Assert.assertEquals("myLocation", json.optString("field"));
        Assert.assertTrue(json.has("box"));
        JSONObject boxJson = json.optJSONObject("box");
        Assert.assertNotNull(boxJson);
        Assert.assertEquals(2, boxJson.length());
        JSONObject nwJson = boxJson.optJSONObject("nw");
        assertPoint(30, 100, nwJson);
        JSONObject seJson = boxJson.optJSONObject("se");
        assertPoint(31, 110, seJson);
    }

    @Test
    public void test_0100_geoDistance() {
        KiiClause clause = KiiClause.geoDistance("myLocation", 30, 100, 1000);
        // Assertion
        JSONObject json = clause.toJson();
        Assert.assertEquals(4, json.length());
        Assert.assertEquals("geodistance", json.optString("type"));
        Assert.assertEquals("myLocation", json.optString("field"));
        Assert.assertEquals(1000, json.optDouble("radius"), 0.001);
        JSONObject centerJson = json.optJSONObject("center");
        Assert.assertNotNull(centerJson);
        assertPoint(30, 100, centerJson);

    }

    private void assertPoint(double latitude, double longitude, JSONObject actual) {
        Assert.assertEquals("point", actual.optString("_type"));
        Assert.assertEquals(latitude, actual.optDouble("lat"), 0.001);
        Assert.assertEquals(longitude, actual.optDouble("lon"), 0.001);
    }
}
