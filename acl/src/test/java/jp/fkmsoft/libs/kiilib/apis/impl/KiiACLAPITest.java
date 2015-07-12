package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiClause;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.apis.QueryParams;
import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.apis.SignupInfo;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiBucket;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiGroupDTO;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObject;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObjectDTO;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.entities.test.TestKiiContext;

/**
 * Testcase
 */
public class KiiACLAPITest {
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";
    private static final String BUCKET_NAME = "aclBucket";

    @Test
    public void test_0000_get_grant_revoke() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        ACLAPI api = new KiiACLAPI(context);

        KiiUser user1 = login(context, "fkmtest");

        KiiUser user2 = signup(context, "fkmtest2");

        KiiBucket bucket = new BasicKiiBucket(user1, BUCKET_NAME);

        deleteBucket(context, bucket);

        createACLBucket(context, bucket);

        getACL(api, bucket);

        grantQuery(api, bucket, user2);

        String user1Token = context.getAccessToken();
        login(context, "fkmtest2");
        String user2Token = context.getAccessToken();

        queryBucket(context, bucket);

        context.setAccessToken(user1Token);
        revokeQuery(api, bucket, user2);

        context.setAccessToken(user2Token);
        queryForbiddenBucket(context, bucket);

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

    private void deleteBucket(KiiContext context, KiiBucket bucket) {
        new KiiBucketAPI(context).delete(bucket, new KiiItemCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("bucket " + BUCKET_NAME + " deleted");
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 404) {
                    Assert.fail("Bucket delete error status=" + e.getStatus() + " / " + e.getBody());
                }
            }
        });
    }

    private void createACLBucket(KiiContext context, KiiBucket bucket) {
        new KiiObjectAPI(context).create(bucket, new JSONObject(), BasicKiiObjectDTO.getInstance(), new ObjectAPI.ObjectCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(BasicKiiObject basicKiiObject) {
                System.out.println("object is created");
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Create object error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void getACL(ACLAPI api, KiiBucket bucket) {
        api.get(bucket, BasicKiiUserDTO.getInstance(), BasicKiiGroupDTO.getInstance(), new ACLAPI.ACLGetCallback() {
            @Override
            public void onSuccess(Map<String, List<ACLSubject>> aclMap) {
                Assert.assertNotNull(aclMap);
                Assert.assertEquals(3, aclMap.size());
                Assert.assertTrue(aclMap.containsKey("DROP_BUCKET_WITH_ALL_CONTENT"));
                Assert.assertTrue(aclMap.containsKey("QUERY_OBJECTS_IN_BUCKET"));
                Assert.assertTrue(aclMap.containsKey("CREATE_OBJECTS_IN_BUCKET"));
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Get ACL error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void grantQuery(ACLAPI api, KiiBucket bucket, KiiUser user) {
        api.grant(bucket, "QUERY_OBJECTS_IN_BUCKET", user, new ACLAPI.ACLCallback() {
            @Override
            public void onSuccess(AccessControllable accessControllable) {
                System.out.println("grant succeeded");
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 409) {
                    Assert.fail("grant error status=" + e.getStatus() + " / " + e.getBody());
                }
            }
        });
    }

    private void queryBucket(KiiContext context, KiiBucket bucket) {
        QueryParams params = new QueryParams(KiiClause.all());
        new KiiBucketAPI(context).query(bucket, params, BasicKiiObjectDTO.getInstance(), new BucketAPI.QueryCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(QueryResult<BasicKiiObject> result) {
                Assert.assertEquals(0, result.size());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("query error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void revokeQuery(ACLAPI api, KiiBucket bucket, KiiUser user) {
        api.revoke(bucket, "QUERY_OBJECTS_IN_BUCKET", user, new ACLAPI.ACLCallback() {
            @Override
            public void onSuccess(AccessControllable accessControllable) {
                System.out.println("revoke succeeded");
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("revoke error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void queryForbiddenBucket(KiiContext context, KiiBucket bucket) {
        QueryParams params = new QueryParams(KiiClause.all());
        new KiiBucketAPI(context).query(bucket, params, BasicKiiObjectDTO.getInstance(), new BucketAPI.QueryCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(QueryResult<BasicKiiObject> result) {
                Assert.fail("This user must not be able to query this bucket.");
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 401) {
                    Assert.fail("query error status=" + e.getStatus() + " / " + e.getBody());
                }
            }
        });
    }
}