package jp.fkmsoft.libs.kiilib.entities.test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

/**
 * Test Kii Object
 */
public class TestKiiObject implements KiiObject {
    private final KiiBucket mBucket;
    private final JSONObject mData;
    private String mVersion;
    private String mId;

    public TestKiiObject(KiiBucket bucket, JSONObject data) {
        mBucket = bucket;
        mData = data;
        mId = data.optString("_id", null);
    }

    @Override
    public String getVersion() {
        return mVersion;
    }

    @Override
    public void setVersion(String s) {
        mVersion = s;
    }

    @Override
    public void updateFields(JSONObject jsonObject) {
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                mData.put(key, jsonObject.opt(key));
            } catch (JSONException e) {
                // nop
            }
        }
    }

    @Override
    public void setModifiedTime(long l) {

    }

    @Override
    public JSONObject toJson() {
        return mData;
    }

    @Override
    public String getResourcePath() {
        return mBucket.getResourcePath() + "/objects/" + mId;
    }

    @Override
    public String getId() {
        return mId;
    }
}
