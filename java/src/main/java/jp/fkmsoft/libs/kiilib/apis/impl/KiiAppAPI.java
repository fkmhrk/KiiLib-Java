package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.EntityFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseUser;
import jp.fkmsoft.libs.kiilib.entities.KiiUserFactory;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Implementation of {@link AppAPI}
 */
public abstract class KiiAppAPI<
        USER extends KiiBaseUser,
        GROUP extends KiiBaseGroup<USER>,
        BUCKET extends KiiBaseBucket,
        OBJECT extends KiiBaseObject<BUCKET>,
        TOPIC extends KiiBaseTopic
        > implements AppAPI<USER, GROUP, BUCKET, OBJECT, TOPIC> {

    final String appId;
    final String appKey;
    final String baseUrl;
    
    String accessToken;
    
    private final UserAPI<USER> userAPI;
    private final GroupAPI<USER, GROUP> groupAPI;
    private final BucketAPI<BUCKET, OBJECT> bucketAPI;
    private final ObjectAPI<BUCKET, OBJECT> objectAPI;
    private final TopicAPI<TOPIC> topicAPI;
    private final ACLAPI aclAPI;

    private final KiiUserFactory<USER> mUserFactory;
    
    protected KiiAppAPI(String appId, String appKey, String baseUrl) {
        this.appId = appId;
        this.appKey = appKey;
        this.baseUrl = baseUrl;

        EntityFactory<USER, GROUP, BUCKET, OBJECT, TOPIC> factory = getEntityFactory();
        mUserFactory = factory.getKiiUserFactory();

        userAPI = new KiiUserAPI<USER>(this, factory.getKiiUserFactory());
        groupAPI = new KiiGroupAPI<USER, GROUP>(this, factory.getKiiUserFactory(), factory.getKiiGroupFactory());
        bucketAPI = new KiiBucketAPI<BUCKET, OBJECT>(this, factory.getKiiObjectFactory());
        objectAPI = new KiiObjectAPI<BUCKET, OBJECT>(this, factory.getKiiObjectFactory());
        topicAPI = new KiiTopicAPI<TOPIC>(this, factory.getKiitopicFactory());
        aclAPI = new KiiACLAPI<USER, GROUP>(this, factory.getKiiUserFactory(), factory.getKiiGroupFactory());
    }

    @Override
    public void loginAsAdmin(String clientId, String clientSecret, final LoginCallback<USER> callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", clientId);
            json.put("client_secret", clientSecret);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, 
                "application/json", null, json, new ResponseHandler() {
            
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }
            
            private void success(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, mUserFactory.create(userId));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void loginAsUser(String identifier, String password, final LoginCallback<USER> callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("username", identifier);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, "application/json", null, json, new KiiResponseHandler<LoginCallback<USER>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, LoginCallback<USER> callback) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, mUserFactory.create(userId));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }
    
    @Override
    public void signup(JSONObject info, String password, final SignupCallback<USER> callback) {
        String url = baseUrl + "/apps/" + appId + "/users";
        
        try {
            info.put("password", password);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null,
                "application/json", null, info, new KiiResponseHandler<SignupCallback<USER>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SignupCallback<USER> callback) {
                try {
                    String userId = response.getString("userID");
                    callback.onSuccess(mUserFactory.create(userId));
                } catch (JSONException e) {
                    callback.onError(e);
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
    public GroupAPI<USER, GROUP> groupAPI() {
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
    public TopicAPI<TOPIC> topicAPI() {
        return topicAPI;
    }

    @Override
    public ACLAPI aclAPI() {
        return aclAPI;
    }
    
    public abstract KiiHTTPClient getHttpClient();

    protected abstract EntityFactory<USER, GROUP, BUCKET, OBJECT, TOPIC> getEntityFactory();
}
