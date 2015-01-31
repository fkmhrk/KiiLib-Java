package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicMessage;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiTopicAPI<TOPIC extends KiiBaseTopic> implements TopicAPI<TOPIC> {

    private final KiiAppAPI api;
    private final KiiTopicFactory<TOPIC> mFactory;
    
    KiiTopicAPI(KiiAppAPI api, KiiTopicFactory<TOPIC> factory) {
        this.api = api;
        this.mFactory = factory;
    }
    
    @Override
    public void create(final BucketOwnable owner, final String name, final TopicCallback<TOPIC> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + owner.getResourcePath() + "/topics/" + name;
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, null, null, null, new KiiResponseHandler<TopicCallback<TOPIC>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback<TOPIC> callback) {
                callback.onSuccess(mFactory.create(owner, name));
            }
        });
    }

    @Override
    public void subscribe(final TOPIC topic, final TopicCallback<TOPIC> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/subscriptions/users";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, null, null, null, new KiiResponseHandler<TopicCallback<TOPIC>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback<TOPIC> callback) {
                callback.onSuccess(topic);
            }
        });
    }

    @Override
    public void unsubscribe(final TOPIC topic, String userId, TopicCallback<TOPIC> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/subscriptions/users/" + userId;

        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, null, null, null, new KiiResponseHandler<TopicCallback<TOPIC>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback<TOPIC> callback) {
                callback.onSuccess(topic);
            }
        });
    }
    
    @Override
    public void getList(final BucketOwnable owner, final TopicListCallback<TOPIC> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + owner.getResourcePath() + "/topics";
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.TopicsRetrievalResponse+json");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, null, headers, null, new KiiResponseHandler<TopicListCallback<TOPIC>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicListCallback<TOPIC> callback) {
                try {
                    JSONArray array = response.getJSONArray("topics");
                    callback.onSuccess(toList(array));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
            
            private List<TOPIC> toList(JSONArray array) throws JSONException {
                List<TOPIC> list = new ArrayList<TOPIC>(array.length());
                for (int i = 0 ; i < array.length() ; ++i) {
                    JSONObject obj = array.getJSONObject(i);
                    list.add(mFactory.create(owner, obj.getString("topicID")));
                }
                return list;
            }
        });
        
    }

    @Override
    public void sendMessage(TOPIC topic, KiiTopicMessage message, final SendMessageCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/messages";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/vnd.kii.SendPushMessageRequest+json", null, message.toJson(), new KiiResponseHandler<SendMessageCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SendMessageCallback callback) {
                try {
                    String id = response.getString("pushMessageID");
                    callback.onSuccess(id);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }
}
