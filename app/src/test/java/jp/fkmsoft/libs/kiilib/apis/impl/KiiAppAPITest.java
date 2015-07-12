package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.SignupInfo;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.entities.test.TestKiiContext;

/**
 * Testcase
 */
public class KiiAppAPITest {
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";

    @Test
    public void test_0000_Login() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        AppAPI api = new KiiAppAPI(context);

        String username = "fkmtest";
        String password = "123456";
        api.loginAsUser(username, password, BasicKiiUserDTO.getInstance(), new AppAPI.LoginCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(String token, BasicKiiUser user) {
                Assert.assertEquals("88eb20051321-fc19-5e11-8682-0fe8323b", user.getId());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail(e.getStatus() + " / " + e.getBody().toString());
            }
        });
    }

    @Test
    public void test_0100_Signup() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        AppAPI api = new KiiAppAPI(context);

        deleteIfExists(api, context);

        SignupInfo info = SignupInfo.UserWithUsername("fkmtest2");
        api.signup(info, "123456", BasicKiiUserDTO.getInstance(), new AppAPI.SignupCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser testKiiUser) {
                Assert.assertNotNull(testKiiUser);
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail(e.getStatus() + " / " + e.getBody().toString());
            }
        });
    }

    private void deleteIfExists(AppAPI api, KiiContext context) {
        String username = "fkmtest2";
        String password = "123456";
        api.loginAsUser(username, password, BasicKiiUserDTO.getInstance(), new AppAPI.LoginCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(String token, BasicKiiUser user) {
                System.out.println("user fkmtest2 ID=" + user.getId());
            }

            @Override
            public void onError(KiiException e) {
                System.out.println("user fkmtest2 is not found");
            }
        });
        if (context.getAccessToken() == null) {
            return;
        }
        String url = context.getBaseUrl() + "/apps/" + context.getAppId() + "/users/me";
        context.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, context.getAccessToken(), null, null, null, new KiiHTTPClient.ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject jsonObject, String s) {
                System.out.println("user deleted!");
            }

            @Override
            public void onException(Exception e) {
                System.out.println("Delete exception " + e);
            }
        });
    }
}