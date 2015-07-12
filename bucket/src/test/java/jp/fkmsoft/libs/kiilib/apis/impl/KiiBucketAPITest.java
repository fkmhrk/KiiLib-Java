package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiClause;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.apis.QueryParams;
import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiBucket;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObject;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObjectDTO;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.entities.test.TestKiiContext;

/**
 * Testcase
 */
public class KiiBucketAPITest {
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";
    private static final String BUCKET_NAME = "bucketTest";

    @Test
    public void test_0000_query() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        BucketAPI api = new KiiBucketAPI(context);

        KiiUser currentUser = login(context, "fkmtest");

        KiiBucket bucket = new BasicKiiBucket(currentUser, BUCKET_NAME);

        // delete
        delteBucket(api, bucket);

        for (int i = 0 ; i < 5 ; ++i) {
            JSONObject data = new JSONObject();
            data.put("score", i * 100 + 50);

            createObject(context, bucket, data);
        }

        // query
        queryBucket(api, bucket, 5);
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

    private void delteBucket(BucketAPI api, KiiBucket bucket) {
        api.delete(bucket, new KiiItemCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Bucket is deleted.");
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 404) {
                    Assert.fail("Bucket delete error status=" + e.getStatus() + " / " + e.getBody());
                }
            }
        });
    }

    private BasicKiiObject createObject(KiiContext context, KiiBucket bucket, JSONObject data) {
        ObjectAPI api = new KiiObjectAPI(context);
        final List<BasicKiiObject> resultList = new ArrayList<BasicKiiObject>();
        api.create(bucket, data, BasicKiiObjectDTO.getInstance(), new ObjectAPI.ObjectCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(BasicKiiObject obj) {
                resultList.add(obj);
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("create object error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
        return resultList.get(0);
    }

    private void queryBucket(BucketAPI api, KiiBucket bucket, final int expectedCount) {
        QueryParams params = new QueryParams(KiiClause.all());
        params.sortByAsc("score");
        api.query(bucket, params, BasicKiiObjectDTO.getInstance(), new BucketAPI.QueryCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(QueryResult<BasicKiiObject> result) {
                Assert.assertEquals(expectedCount, result.size());
                for (BasicKiiObject obj : result) {
                    System.out.println(obj.toString());
                }
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("query error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }
}