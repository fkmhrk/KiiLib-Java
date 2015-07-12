package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * Test Kii User
 */
public class TestKiiUser implements KiiUser {

    private final JSONObject mData;

    public TestKiiUser(JSONObject data) {
        mData = data;
    }

    @Override
    public String getSubjectType() {
        return null;
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getId() {
        return mData.optString("UserID", null);
    }
}
