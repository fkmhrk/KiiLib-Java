package jp.fkmsoft.libs.kiilib.apis.app;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiClause;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.QueryParams;
import jp.fkmsoft.libs.kiilib.test.MockHttpClient;
import jp.fkmsoft.libs.kiilib.test.TestKiiAppApi;

/**
 * Test case for BucketApi
 */
public class TestBucketApi {

    private static final String APP_ID = "appId";
    private static final String APP_KEY = "appKey";
    private static final String BASE_URL = "https://api-jp.kii.com/api";

    private TestKiiAppApi mAppApi;
    private MockHttpClient mClient;

    @Before
    public void setUp() {
        mAppApi = new TestKiiAppApi(APP_ID, APP_KEY, BASE_URL);
        mClient = (MockHttpClient) mAppApi.getHttpClient();
    }

    private String createObjectJson(String id, long created, long modified, String owner, String key, String value) {
        JSONObject json = new JSONObject();
        try {
            json.put("_id", id);
            json.put("_created", created);
            json.put("_modified", modified);
            json.put("_owner", owner);
            json.put(key, value);
        } catch (JSONException e) {
            // nop
        }
        return json.toString();
    }

    @Test
    public void test_0000_query() {
        mClient.addToSendJson(200, "{\"results\":[" +
                createObjectJson("obj1234", 1122, 3344, "user1234", "name", "fkm") +
                "]}", "");

        KiiBucket bucket = new KiiBucket(null, "members");
        QueryParams params = new QueryParams(KiiClause.all());

        mAppApi.bucketAPI().query(bucket, params, new BucketAPI.QueryCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(QueryResult<KiiBucket, KiiObject> result) {
                Assert.assertNotNull(result);
                Assert.assertEquals(1, result.size());

                KiiObject obj = result.get(0);
                Assert.assertEquals("obj1234", obj.getId());
                Assert.assertEquals(1122, obj.getCreatedTime());
                Assert.assertEquals(3344, obj.getModifiedTime());
                Assert.assertEquals("fkm", obj.optString("name"));
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0100_delete() {
        mClient.addToSendJson(204, null, "");

        KiiBucket bucket = new KiiBucket(null, "members");

        mAppApi.bucketAPI().delete(bucket, new BucketAPI.BucketCallback<KiiBucket>() {
            @Override
            public void onSuccess(KiiBucket bucket) {
                Assert.assertNotNull(bucket);
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

}
