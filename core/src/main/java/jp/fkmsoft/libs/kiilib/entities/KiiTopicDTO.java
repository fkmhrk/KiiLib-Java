package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * DTO for Kii Topic
 */
public interface KiiTopicDTO<T extends KiiTopic> {
    /**
     * Creates T by JSONObject
     * @param owner Topic owner
     * @param input JSONObject
     * @return An instance of T
     */
    T fromJson(BucketOwnable owner, JSONObject input);

    /**
     * Creates JSONObject by T
     * @param input T
     * @return JSONObject
     */
    JSONObject toJson(T input);
}
