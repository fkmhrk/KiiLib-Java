package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiDTO;

/**
 * DTO for {@link BasicKiiBucket}
 */
public class BasicKiiBucketDTO implements KiiDTO<BasicKiiBucket> {
    @Override
    public BasicKiiBucket fromJson(JSONObject jsonObject) {
        // is this method really called?
        return null;
    }

    @Override
    public JSONObject toJson(BasicKiiBucket basicKiiBucket) {
        return null;
    }
}
