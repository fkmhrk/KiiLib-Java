package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.EntityFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Implementation of {@link AppAPI}
 */
public abstract class KiiAppAPI<
        USER extends KiiUser,
        GROUP extends KiiGroup<USER>,
        BUCKET extends KiiBucket,
        OBJECT extends KiiObject<BUCKET>> implements AppAPI<USER, BUCKET, OBJECT> {

    final String appId;
    final String appKey;
    final String baseUrl;
    
    String accessToken;
    
    private final UserAPI<USER> userAPI;
    private final GroupAPI<GROUP, USER> groupAPI;
    private final BucketAPI<BUCKET, OBJECT> bucketAPI;
    private final ObjectAPI<BUCKET, OBJECT> objectAPI;
    private final TopicAPI topicAPI;
    private final ACLAPI aclAPI;
    
    public KiiAppAPI(String appId, String appKey, String baseUrl) {
        this.appId = appId;
        this.appKey = appKey;
        this.baseUrl = baseUrl;

        EntityFactory<USER, GROUP, BUCKET, OBJECT> factory = getEntityFactory();

        userAPI = new KiiUserAPI<USER>(this, factory.getKiiUserFactory());
        groupAPI = new KiiGroupAPI<GROUP, USER>(this, factory.getKiiGroupFactory(), factory.getKiiUserFactory());
        bucketAPI = new KiiBucketAPI<BUCKET, OBJECT>(this, factory.getKiiObjectFactory());
        objectAPI = new KiiObjectAPI<BUCKET, OBJECT>(this, factory.getKiiObjectFactory());
        topicAPI = new KiiTopicAPI(this);
        aclAPI = new KiiACLAPI(this);
    }

    @Override
    public void loginAsAdmin(String clientId, String clientSecret, final LoginCallback callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", clientId);
            json.put("client_secret", clientSecret);
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, 
                "application/json", null, json, new ResponseHandler() {
            
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(status, response.toString());
                }
            }
            
            private void success(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(KiiCallback.STATUS_GENERAL_EXCEPTION, e.getMessage());
            }
        });
    }

    @Override
    public void loginAsUser(String identifier, String password, final LoginCallback callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("username", identifier);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, "application/json", null, json, new KiiResponseHandler<LoginCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, LoginCallback callback) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }
    
    @Override
    public void signup(JSONObject info, String password, final SignupCallback callback) {
        String url = baseUrl + "/apps/" + appId + "/users";
        
        try {
            info.put("password", password);
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null,
                "application/json", null, info, new KiiResponseHandler<SignupCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SignupCallback callback) {
                try {
                    String userId = response.getString("userID");
                    callback.onSuccess(new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }
    
    /*
     * (non-Javadoc)
     * @see com.kii.cloud.KiiAPI#setAccessToken(java.lang.String)
     */
    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public UserAPI<USER> userAPI() {
        return userAPI;
    }

    @Override
    public GroupAPI<GROUP, USER> groupAPI() {
        return groupAPI;
    }

    @Override
    public BucketAPI<BUCKET, OBJECT> bucketAPI() {
        return bucketAPI;
    }

    @Override
    public ObjectAPI<BUCKET, OBJECT> objectAPI() {
        return objectAPI;
    }

    @Override
    public TopicAPI topicAPI() {
        return topicAPI;
    }

    @Override
    public ACLAPI aclAPI() {
        return aclAPI;
    }
    
    public abstract KiiHTTPClient getHttpClient();

    protected abstract EntityFactory<USER, GROUP, BUCKET, OBJECT> getEntityFactory();
}
