package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicDTO;

/**
 * Provides topic API.
 */
public interface TopicAPI {
    /**
     * Creates new Topic.
     * @param owner Topic owner.
     * @param name Topic name.
     * @param dto DTO for Topic.
     * @param callback Callback object.
     * @param <T> Type of Kii Topic.
     */
    <T extends KiiTopic> void create(BucketOwnable owner, String name, KiiTopicDTO<T> dto, TopicCallback<T> callback);

    /**
     * Subscribes target Topic.
     * @param topic Target topic.
     * @param callback Callback object.
     */
    void subscribe(KiiTopic topic, TopicCallback<KiiTopic> callback);

    /**
     * Unsubscribe target Topic.
     * @param topic Target topic.
     * @param userId User ID.
     * @param callback Callback object.
     */
    void unsubscribe(KiiTopic topic, String userId, TopicCallback<KiiTopic> callback);

    /**
     * Sends message to target topic.
     * @param topic Target topic.
     * @param message Message.
     * @param callback Callback object.
     */
    void sendMessage(KiiTopic topic, KiiTopicMessage message, SendMessageCallback callback);

    /**
     * Gets topic list.
     * @param owner Topic owner.
     * @param dto DTO for Kii Topic.
     * @param callback Callback object.
     * @param <T> Type of Kii Topic.
     */
    <T extends KiiTopic> void getList(BucketOwnable owner, KiiTopicDTO<T> dto, TopicListCallback<T> callback);

    interface TopicCallback<T extends KiiTopic> extends KiiItemCallback<T> { }

    interface TopicListCallback<T extends KiiTopic> extends KiiItemCallback<List<T>> { }

    interface SendMessageCallback extends KiiItemCallback<String> { }
}
