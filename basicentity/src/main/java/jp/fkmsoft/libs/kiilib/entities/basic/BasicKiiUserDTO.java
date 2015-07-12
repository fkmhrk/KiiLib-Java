package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiDTO;

/**
 * DTO for {@link BasicKiiUser}
 */
public class BasicKiiUserDTO implements KiiDTO<BasicKiiUser> {
    private static final String FIELD_ID = "UserID";

    private static BasicKiiUserDTO INSTANCE = new BasicKiiUserDTO();
    private BasicKiiUserDTO() {
        // singleton
    }

    public static BasicKiiUserDTO getInstance() {
        return INSTANCE;
    }

    @Override
    public BasicKiiUser fromJson(JSONObject json) {
        String userId = json.optString(FIELD_ID, "");
        return new BasicKiiUser(userId, json);
    }
}
