package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicMessage;

/**
 * Provides topic API.
 * @author fkm
 *
 */
public interface TopicAPI<TOPIC extends KiiBaseTopic> {
    public interface TopicCallback<T extends KiiBaseTopic> extends KiiCallback {
        void onSuccess(T topic);
    }
    void create(BucketOwnable owner, String name, TopicCallback<TOPIC> callback);

    void subscribe(TOPIC topic, TopicCallback<TOPIC> callback);

    void unsubscribe(TOPIC topic, String userId, TopicCallback<TOPIC> callback);
    
    public interface SendMessageCallback extends KiiCallback {
        void onSuccess(String pushMessageId);
    }
    
    void sendMessage(TOPIC topic, KiiTopicMessage message, SendMessageCallback callback);
    
    public interface TopicListCallback<T extends KiiBaseTopic> extends KiiCallback {
        void onSuccess(List<T> list);
    }
    
    void getList(BucketOwnable owner, TopicListCallback<TOPIC> callback);
}
