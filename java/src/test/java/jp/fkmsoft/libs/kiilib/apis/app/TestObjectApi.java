package jp.fkmsoft.libs.kiilib.apis.app;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.test.MockHttpClient;
import jp.fkmsoft.libs.kiilib.test.TestKiiAppApi;

/**
 * Test case for ObjectApi
 */
public class TestObjectApi {

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
    public void test_0000_getById() {
        mClient.addToSendJson(200, createObjectJson("obj1234", 1122, 3344, "user1234", "name", "fkm"), "");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        String id = "obj1234";

        mAppApi.objectAPI().getById(bucket, id, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
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
    public void test_0100_create() {
        mClient.addToSendJson(201, "{\"objectID\":\"obj1234\",\"createdAt\":1234}", "1");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        JSONObject data = new JSONObject();
        try {
            data.put("name", "fkm");
        } catch (JSONException e) {
            // nop
        }

        mAppApi.objectAPI().create(bucket, data, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
                Assert.assertEquals("obj1234", obj.getId());
                Assert.assertEquals(1234, obj.getCreatedTime());
                Assert.assertEquals(1234, obj.getModifiedTime());
                Assert.assertEquals("fkm", obj.optString("name"));
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0200_save() {
        mClient.addToSendJson(200, "{\"modifiedAt\":3344}", "2");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        KiiObject obj = new KiiObject(bucket, "obj1234");
        obj.setCreatedTime(1234);
        try {
            obj.put("name", "fkm");
        } catch (JSONException e) {
            // nop
        }

        mAppApi.objectAPI().save(obj, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
                Assert.assertEquals("obj1234", obj.getId());
                Assert.assertEquals(1234, obj.getCreatedTime());
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
    public void test_0300_updatePatch() {
        mClient.addToSendJson(200, "{\"modifiedAt\":3344}", "2");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        KiiObject obj = new KiiObject(bucket, "obj1234");
        obj.setCreatedTime(1234);

        JSONObject patch = new JSONObject();
        try {
            patch.put("name", "fkm");
        } catch (JSONException e) {
            // nop
        }

        mAppApi.objectAPI().updatePatch(obj, patch, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
                Assert.assertEquals("obj1234", obj.getId());
                Assert.assertEquals(1234, obj.getCreatedTime());
                Assert.assertEquals(3344, obj.getModifiedTime());
                Assert.assertEquals("fkm", obj.optString("name"));
                Assert.assertEquals("2", obj.getVersion());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0400_updatePatchIfUnmodified() {
        mClient.addToSendJson(200, "{\"modifiedAt\":3344}", "2");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        KiiObject obj = new KiiObject(bucket, "obj1234");
        obj.setCreatedTime(1234);

        JSONObject patch = new JSONObject();
        try {
            patch.put("name", "fkm");
        } catch (JSONException e) {
            // nop
        }

        mAppApi.objectAPI().updatePatchIfUnmodified(obj, patch, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
                Assert.assertEquals("obj1234", obj.getId());
                Assert.assertEquals(1234, obj.getCreatedTime());
                Assert.assertEquals(3344, obj.getModifiedTime());
                Assert.assertEquals("fkm", obj.optString("name"));
                Assert.assertEquals("2", obj.getVersion());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0500_updateBody() {
        mClient.addToSendStream(200, "{\"modifiedAt\":3344}", "2");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        KiiObject obj = new KiiObject(bucket, "obj1234");
        obj.setCreatedTime(1234);

        String data = "body data";
        ByteArrayInputStream in;
        try {
            in = new ByteArrayInputStream(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Assert.fail(e.getMessage());
            return;
        }

        mAppApi.objectAPI().updateBody(obj, "text/plain", in, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
                Assert.assertEquals("obj1234", obj.getId());
                Assert.assertEquals(1234, obj.getCreatedTime());
                Assert.assertEquals(3344, obj.getModifiedTime());
                Assert.assertEquals("2", obj.getVersion());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0600_publish() {
        mClient.addToSendJson(201, "{\"url\":\"http://api-jp.kii.com/api/a/publishURL\"}", "2");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        KiiObject obj = new KiiObject(bucket, "obj1234");

        mAppApi.objectAPI().publish(obj, new ObjectAPI.PublishCallback() {
            @Override
            public void onSuccess(String url) {
                Assert.assertEquals("http://api-jp.kii.com/api/a/publishURL", url);
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0700_delete() {
        mClient.addToSendJson(204, null, "");

        KiiBucket bucket = new KiiBucket(null, "scoreBoard");
        KiiObject obj = new KiiObject(bucket, "obj1234");

        mAppApi.objectAPI().delete(obj, new ObjectAPI.ObjectCallback<KiiBucket, KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                Assert.assertNotNull(obj);
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
