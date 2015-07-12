package jp.fkmsoft.libs.kiilib.entities;

/**
 * DTO for Kii Topic
 */
public interface KiiTopicDTO<T extends KiiTopic> {
    /**
     * Creates T by JSONObject
     * @param owner Topic owner
     * @param name Topic name
     * @return An instance of T
     */
    T fromJson(BucketOwnable owner, String name);
}
