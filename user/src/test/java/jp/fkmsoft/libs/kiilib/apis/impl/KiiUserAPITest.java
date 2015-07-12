package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.SignupInfo;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.entities.test.TestKiiContext;

/**
 * Testcase
 */
public class KiiUserAPITest {
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";

    @Test
    public void test_0000_findUser() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        UserAPI api = new KiiUserAPI(context);

        KiiUser currentUser = login(context, "fkmtest");

        findByUsername(api);

        findByEmail(api, "test@fkmsoft.jp", "dfa848a00022-8e09-5e11-ca82-0b6f4b24");

        findByPhone(api, "+818011112222", "dfa848a00022-8e09-5e11-ca82-032cff15");

        getById(api, currentUser.getId());
    }

    private KiiUser login(KiiContext context, String username) throws Exception {
        AppAPI api = new KiiAppAPI(context);

        String password = "123456";
        final List<KiiUser> resultList = new ArrayList<KiiUser>();
        api.loginAsUser(username, password, BasicKiiUserDTO.getInstance(), new AppAPI.LoginCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(String token, BasicKiiUser testKiiUser) {
                resultList.add(testKiiUser);
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("login error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
        return resultList.get(0);
    }

    private void findByUsername(UserAPI api) {
        api.findUserByUsername("fkmtest", BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                Assert.assertEquals("88eb20051321-fc19-5e11-8682-0fe8323b", user.getId());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("findByUsername error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void findByEmail(UserAPI api, String email, final String expectedId) {
        api.findUserByEmail(email, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                Assert.assertEquals(expectedId, user.getId());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("findByEmail error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void findByPhone(UserAPI api, String phoneNumber, final String expectedId) {
        api.findUserByPhone(phoneNumber, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                Assert.assertEquals(expectedId, user.getId());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("findByPhone error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void getById(UserAPI api, String id) {
        api.getById(id, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                Assert.assertEquals("88eb20051321-fc19-5e11-8682-0fe8323b", user.getId());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("getById error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    @Test
    public void test_0100_update() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        UserAPI api = new KiiUserAPI(context);

        login(context, "fkmtest");

        signup(context, "fkmtest2");

        KiiUser user2 = login(context, "fkmtest2");

        updateEmail(api, user2, "user2@fkmsoft.jp");

        updatePhone(api, user2, "+819022223333");

        findByEmail(api, "user2@fkmsoft.jp", user2.getId());

        findByPhone(api, "+819022223333", user2.getId());

        deleteUser(context, user2.getId());
    }

    private KiiUser signup(final KiiContext context, final String username) {
        AppAPI api = new KiiAppAPI(context);

        SignupInfo info = SignupInfo.UserWithUsername(username);
        final List<KiiUser> resultList = new ArrayList<KiiUser>();
        api.signup(info, "123456", BasicKiiUserDTO.getInstance(), new AppAPI.SignupCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser testKiiUser) {
                resultList.add(testKiiUser);
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 409) {
                    Assert.fail("Failed to create user status=" + e.getStatus());
                }
                UserAPI userAPI = new KiiUserAPI(context);
                userAPI.findUserByUsername(username, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
                    @Override
                    public void onSuccess(BasicKiiUser testKiiUser) {
                        resultList.add(testKiiUser);
                    }

                    @Override
                    public void onError(KiiException e) {
                        Assert.fail("Failed to find user status=" + e.getStatus());
                    }
                });
            }
        });

        return resultList.get(0);
    }

    private void updateEmail(UserAPI api, KiiUser target, String newEmail) {
        api.updateEmail(target, newEmail, false, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser kiiUser) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Failed to update email status=" + e.getStatus());
            }
        });
    }

    private void updatePhone(UserAPI api, KiiUser target, String newPhoneNumber) {
        api.updatePhone(target, newPhoneNumber, false, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser kiiUser) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Failed to update phone status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void deleteUser(KiiContext context, final String id) {
        String url = context.getBaseUrl() + "/apps/" + context.getAppId() + "/users/" + id;
        context.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, context.getAccessToken(), null, null, null, new KiiHTTPClient.ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject jsonObject, String s) {
                System.out.println("user " + id + " deleted");
            }

            @Override
            public void onException(Exception e) {
                Assert.fail("Failed to delete user " + e);
            }
        });
    }
}