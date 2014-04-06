package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * Factory for KiiBaseObject
 */
public interface KiiObjectFactory<BUCKET extends KiiBaseBucket,OBJECT extends KiiBaseObject<BUCKET>> {
    OBJECT create(BUCKET bucket, JSONObject body);
}
