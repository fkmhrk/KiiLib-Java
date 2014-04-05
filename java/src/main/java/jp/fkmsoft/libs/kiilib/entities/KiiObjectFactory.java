package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * Factory for KiiObject
 */
public interface KiiObjectFactory<BUCKET extends KiiBucket,OBJECT extends KiiObject<BUCKET>> {
    OBJECT create(BUCKET bucket, JSONObject body);
}
