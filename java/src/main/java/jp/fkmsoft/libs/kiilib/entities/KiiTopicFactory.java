package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiBaseTopic
 */
public interface KiiTopicFactory<TOPIC extends KiiBaseTopic> {
    TOPIC create(BucketOwnable owner, String name);
}
