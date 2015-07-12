package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * DTO for {@link BasicKiiGroup}
 */
public class BasicKiiGroupDTO implements KiiDTO<BasicKiiGroup> {
    private static final String FIELD_ID = "groupID";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_OWNER_ID = "owner";

    private static BasicKiiGroupDTO INSTANCE = new BasicKiiGroupDTO();
    private BasicKiiGroupDTO() {
        // singleton
    }

    public static BasicKiiGroupDTO getInstance() {
        return INSTANCE;
    }

    @Override
    public BasicKiiGroup fromJson(JSONObject json) {
        String groupId = json.optString(FIELD_ID, "");
        String name = json.optString(FIELD_NAME, "");
        String ownerId = json.optString(FIELD_OWNER_ID, null);
        KiiUser owner = ownerId == null ? null : new BasicKiiUser(ownerId, null);
        return new BasicKiiGroup(groupId, name, owner);
    }
}
