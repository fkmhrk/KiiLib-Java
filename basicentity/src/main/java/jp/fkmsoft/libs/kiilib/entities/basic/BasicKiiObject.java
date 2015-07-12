package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

/**
 * Implementation
 */
public class BasicKiiObject extends JSONObject implements KiiObject {

    private final KiiBucket mBucket;
    private final String mId;
    private String mVersion;
    private long mModifiedTime;

    public BasicKiiObject(KiiBucket bucket, String id, String version, JSONObject data) {
        mBucket = bucket;
        mId = id;
        mVersion = version;
        if (data != null) {
            Utils.copyJson(data, this);
        }
    }

    @Override
    public String getVersion() {
        return mVersion;
    }

    @Override
    public void setVersion(String version) {
        mVersion = version;
    }

    @Override
    public void updateFields(JSONObject json) {
        Utils.copyJson(json, this);
    }

    @Override
    public void setModifiedTime(long time) {
        mModifiedTime = time;
    }

    @Override
    public JSONObject toJson() {
        return this;
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
