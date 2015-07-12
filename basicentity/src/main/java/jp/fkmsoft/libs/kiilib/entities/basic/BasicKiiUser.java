package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Implementation
 */
public class BasicKiiUser extends JSONObject implements KiiUser {

    private String mId;

    public BasicKiiUser(String id, JSONObject data) {
        mId = id;
        if (data != null) {
            Iterator keys = data.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {
                    put(key, data.opt(key));
                } catch (JSONException e) {
                    // nop
                }
            }
        }
    }

    @Override
    public String getSubjectType() {
        return "UserID";
    }

    @Override
    public String getResourcePath() {
        return "/users/" + mId;
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_USER;
    }

    @Override
    public String getId() {
        return mId;
    }
}
