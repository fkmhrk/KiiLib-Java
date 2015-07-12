package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectDTO;

/**
 * DTO for {@link BasicKiiObject}
 */
public class BasicKiiObjectDTO implements KiiObjectDTO<BasicKiiObject> {
    private static final String FIELD_ID = "_id";
    private static final String FIELD_VERSION = "_version";

    @Override
    public BasicKiiObject fromJson(KiiBucket bucket, JSONObject json) {
        String id = json.optString(FIELD_ID, null);
        String version = json.optString(FIELD_VERSION, "");
        return new BasicKiiObject(bucket, id, version, json);
    }

    @Override
    public JSONObject toJson(BasicKiiObject basicKiiObject) {
        return null;
    }
}
