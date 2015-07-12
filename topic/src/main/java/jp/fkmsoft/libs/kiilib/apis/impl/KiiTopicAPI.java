package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.apis.KiiTopicMessage;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicDTO;

public class KiiTopicAPI implements TopicAPI {

    private final KiiContext mContext;

    public KiiTopicAPI(KiiContext context) {
        mContext = context;
    }

    @Override
    public <T extends KiiTopic> void create(final BucketOwnable owner, final String name, final KiiTopicDTO<T> dto, TopicCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + owner.getResourcePath() + "/topics/" + name;
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<TopicCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback<T> callback) {
                callback.onSuccess(dto.fromJson(owner, name));
            }
        });
    }

    @Override
    public void subscribe(final KiiTopic topic, TopicCallback<KiiTopic> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + topic.getResourcePath() + "/push/subscriptions/users";
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<TopicCallback<KiiTopic>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback<KiiTopic> callback) {
                callback.onSuccess(topic);
            }
        });
    }

    @Override
    public void unsubscribe(final KiiTopic topic, String userId, TopicCallback<KiiTopic> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + topic.getResourcePath() + "/push/subscriptions/users/" + userId;

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<TopicCallback<KiiTopic>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback<KiiTopic> callback) {
                callback.onSuccess(topic);
            }
        });
    }

    @Override
    public <T extends KiiTopic> void getList(final BucketOwnable owner, final KiiTopicDTO<T> dto, TopicListCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + owner.getResourcePath() + "/topics";
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.TopicsRetrievalResponse+json");
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.GET, url, mContext.getAccessToken(), null, headers, null, new KiiResponseHandler<TopicListCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicListCallback<T> callback) {
                try {
                    JSONArray array = response.getJSONArray("topics");
                    callback.onSuccess(toList(array, dto));
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
            
            private List<T> toList(JSONArray array, KiiTopicDTO<T> dto) throws JSONException {
                List<T> list = new ArrayList<T>(array.length());
                for (int i = 0 ; i < array.length() ; ++i) {
                    JSONObject obj = array.getJSONObject(i);
                    String topicName = obj.optString("topicID", null);
                    if (topicName != null) {
                        list.add(dto.fromJson(owner, topicName));
                    }
                }
                return list;
            }
        });
        
    }

    @Override
    public void sendMessage(KiiTopic topic, KiiTopicMessage message, SendMessageCallback callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + topic.getResourcePath() + "/push/messages";
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mContext.getAccessToken(), "application/vnd.kii.SendPushMessageRequest+json", null, message.toJson(), new KiiResponseHandler<SendMessageCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SendMessageCallback callback) {
                try {
                    String id = response.getString("pushMessageID");
                    callback.onSuccess(id);
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }
}
