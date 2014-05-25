package jp.fkmsoft.libs.kiilib.apis.app;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.test.MockHttpClient;
import jp.fkmsoft.libs.kiilib.test.TestKiiAppApi;

/**
 * Test case for UserApi
 */
public class TestUserApi {

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
    public void test_0000_findByEmail() {
        mClient.addToSendJson(200, "{\"userID\":\"user2345\",\"loginName\":\"fkm\"}", "");

        String email = "android@fkmsoft.jp";

        mAppApi.userAPI().findUserByEmail(email, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("user2345", user.getId());
                Assert.assertEquals("fkm", user.getUserName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0200_findByPhone() {
        mClient.addToSendJson(200, "{\"userID\":\"user3456\",\"loginName\":\"fkm\"}", "");

        String phone = "+818011112222";

        mAppApi.userAPI().findUserByPhone(phone, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("user3456", user.getId());
                Assert.assertEquals("fkm", user.getUserName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0300_findByUsername() {
        mClient.addToSendJson(200, "{\"userID\":\"user4567\",\"loginName\":\"fkm\"}", "");

        String username = "fkm";

        mAppApi.userAPI().findUserByUsername(username, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("user4567", user.getId());
                Assert.assertEquals("fkm", user.getUserName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0400_updateEmail() {
        mClient.addToSendJson(204, null, "");

        KiiUser user = new KiiUser("me");
        String email = "newandroid@fkmsoft.jp";

        mAppApi.userAPI().updateEmail(user, email, false, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("me", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0500_updatePhone() {
        mClient.addToSendJson(204, null, "");

        KiiUser user = new KiiUser("me");
        String phone = "+818022223333";

        mAppApi.userAPI().updatePhone(user, phone, false, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("me", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0600_installDevice() {
        mClient.addToSendJson(201, "{}", "");

        String regId = "regId";

        mAppApi.userAPI().installDevice(regId, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNull(user);
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0700_getById() {
        mClient.addToSendJson(200, "{\"userID\":\"user2345\",\"loginName\":\"fkm\"}", "");

        String id = "user2345";

        mAppApi.userAPI().getById(id, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                Assert.assertNotNull(user);
                Assert.assertEquals("user2345", user.getId());
                Assert.assertEquals("fkm", user.getUserName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
