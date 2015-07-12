package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * DTO for Kii Object
 */
public interface KiiObjectDTO<T> {
    /**
     * Creates T by JSONObject
     * @param input JSONObject
     * @return An instance of T
     */
    T fromJson(KiiBucket bucket, JSONObject input);
}
