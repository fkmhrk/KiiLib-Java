package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
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
public class KiiObjectAPITest {
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";
    private static final String BUCKET_NAME = "objectTestBucket";

    @Test
    public void test_0000_create_update_delete() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        ObjectAPI api = new KiiObjectAPI(context);

        KiiUser user = login(context);
        KiiBucket bucket = new BasicKiiBucket(user, BUCKET_NAME);
        JSONObject data = new JSONObject();
        data.put("score", 120);
        data.put("comment", "great!");

        KiiObject obj = createObject(api, bucket, data);
        JSONObject patch1 = new JSONObject();
        patch1.put("score", 130);
        obj.updateFields(patch1);

        updateObject(api, obj);

        deleteObject(api, obj);

    }

    private KiiUser login(KiiContext context) throws Exception {
        AppAPI api = new KiiAppAPI(context);

        String username = "fkmtest";
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

    private KiiObject createObject(ObjectAPI api, KiiBucket bucket, JSONObject data) {
        final List<KiiObject> resultList = new ArrayList<KiiObject>();
        api.create(bucket, data, BasicKiiObjectDTO.getInstance(), new ObjectAPI.ObjectCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(BasicKiiObject testKiiObject) {
                resultList.add(testKiiObject);
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Create object error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
        return resultList.get(0);
    }

    private void updateObject(ObjectAPI api, KiiObject obj) {
        api.save(obj, new ObjectAPI.ObjectCallback<KiiObject>() {
            @Override
            public void onSuccess(KiiObject kiiObject) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Update object error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void deleteObject(ObjectAPI api, KiiObject obj) {
        api.delete(obj, new KiiItemCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Delete object error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }
}