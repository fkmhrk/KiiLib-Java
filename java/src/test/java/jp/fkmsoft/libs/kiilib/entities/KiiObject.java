package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Just for name
 */
public class KiiObject extends KiiBaseObject<KiiBucket> {
    public KiiObject(KiiBucket bucket) {
        super(bucket);
    }

    public KiiObject(KiiBucket bucket, String id) {
        super(bucket, id);
    }

    public KiiObject(KiiBucket bucket, JSONObject from) throws JSONException {
        super(bucket, from);
    }
}
