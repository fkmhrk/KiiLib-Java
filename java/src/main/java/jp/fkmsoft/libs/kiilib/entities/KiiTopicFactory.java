package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiTopic
 */
public interface KiiTopicFactory<TOPIC extends KiiTopic> {
    TOPIC create(BucketOwnable owner, String name);
}
