package jp.fkmsoft.libs.kiilib.apis.app;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.test.MockHttpClient;
import jp.fkmsoft.libs.kiilib.test.TestKiiAppApi;

/**
 * Test case for AppApi
 */
public class TestAppApi {

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

    @Test
    public void test_0000_loginAsAdmin() {
        mClient.addToSendJson(200, "{\"access_token\":\"token1234\",\"id\":\"userID1234\"}", "");

        String clientId = "clientId";
        String clientSecret = "clientSecret";

        mAppApi.loginAsAdmin(clientId, clientSecret, new AppAPI.LoginCallback<KiiUser>() {
            @Override
            public void onSuccess(String token, KiiUser user) {
                Assert.assertEquals("token1234", token);
                Assert.assertNotNull(user);
                Assert.assertEquals("userID1234", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0100_loginAsUser() {
        mClient.addToSendJson(200, "{\"access_token\":\"token1234\",\"id\":\"userID1234\"}", "");

        String username = "user1234";
        String password = "pass1234";

        mAppApi.loginAsUser(username, password, new AppAPI.LoginCallback<KiiUser>() {
            @Override
            public void onSuccess(String token, KiiUser user) {
                Assert.assertEquals("token1234", token);
                Assert.assertNotNull(user);
                Assert.assertEquals("userID1234", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0200_signup() throws Exception {
        mClient.addToSendJson(201, "{\"userID\":\"userID1234\"}", "");

        JSONObject info = new JSONObject();
        info.put("loginName", "name1234");

        String password = "pass1234";

        mAppApi.signup(info, password, new AppAPI.SignupCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("userID1234", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
