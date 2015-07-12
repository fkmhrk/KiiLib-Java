package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * DTO for Kii Entity
 */
public interface KiiDTO<T> {
    /**
     * Creates T by JSONObject
     * @param input JSONObject
     * @return An instance of T
     */
    T fromJson(JSONObject input);

    /**
     * Creates JSONObject by T
     * @param input T
     * @return JSONObject
     */
    JSONObject toJson(T input);
}
