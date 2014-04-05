package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * Factory for KiiObject
 */
public interface KiiObjectFactory<T extends KiiObject> {
    T create(KiiBucket bucket, JSONObject body);
}
