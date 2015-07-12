package jp.fkmsoft.libs.kiilib.entities.test;

import org.json.JSONException;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Test Kii User
 */
public class TestKiiUser implements KiiUser {

    private final JSONObject mData;

    public static TestKiiUser createMe() {
        JSONObject json = new JSONObject();
        try {
            json.put("UserID", "me");
        } catch (JSONException e) {
            // nop
        }
        return new TestKiiUser(json);
    }

    public TestKiiUser(JSONObject data) {
        mData = data;
    }

    @Override
    public String getSubjectType() {
        return null;
    }

    @Override
    public String getResourcePath() {
        return "/users/" + getId();
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
