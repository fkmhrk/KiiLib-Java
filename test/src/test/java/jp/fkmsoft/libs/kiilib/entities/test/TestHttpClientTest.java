package jp.fkmsoft.libs.kiilib.entities.test;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;

/**
 * Testcase
 */
public class TestHttpClientTest {
    // test App ID / App Key
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";

    @Test
    public void test_0000_PostRequest() throws Exception {
        // params
        TestKiiContext context = new TestKiiContext(APP_ID, APP_KEY, "https://api-jp.kii.com/api");
        String url = "https://api-jp.kii.com/api/oauth2/token";
        JSONObject json = new JSONObject();
        json.put("username", "fkmtest");
        json.put("password", "123456");

        TestHTTPClient client = new TestHTTPClient(context);
        client.sendJsonRequest(KiiHTTPClient.Method.POST, url, null, "application/json", null, json, new KiiHTTPClient.ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                Assert.assertEquals("791e70341321-bf39-5e11-0682-02a8afe0", response.optString("id", ""));
            }

            @Override
            public void onException(Exception e) {
                Assert.fail(e.toString());
            }
        });
    }

    @Test
    public void test_0100_GetRequest() throws Exception {
        // params
        TestKiiContext context = new TestKiiContext(APP_ID, APP_KEY, "https://api-jp.kii.com/api");
        TestHTTPClient client = new TestHTTPClient(context);
        login(client, context);

        String url = "https://api-jp.kii.com/api/apps/" + APP_ID + "/users/me";
        client.sendJsonRequest(KiiHTTPClient.Method.GET, url, context.getAccessToken(), "application/json", null, null, new KiiHTTPClient.ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                Assert.assertEquals("791e70341321-bf39-5e11-0682-02a8afe0", response.optString("userID", ""));
            }

            @Override
            public void onException(Exception e) {
                Assert.fail(e.toString());
            }
        });
    }

    private void login(KiiHTTPClient client, final KiiContext context) throws Exception {
        // params
        String url = "https://api-jp.kii.com/api/oauth2/token";
        JSONObject json = new JSONObject();
        json.put("username", "fkmtest");
        json.put("password", "123456");

        client.sendJsonRequest(KiiHTTPClient.Method.POST, url, null, "application/json", null, json, new KiiHTTPClient.ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                context.setAccessToken(response.optString("access_token", ""));
            }

            @Override
            public void onException(Exception e) {
                Assert.fail(e.toString());
            }
        });
    }
}
