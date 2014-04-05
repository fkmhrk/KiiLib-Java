package jp.fkmsoft.libs.kiilib.apis;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides Kii API
 * @author fkm
 *
 */
public interface AppAPI<
        USER extends KiiUser,
        GROUP extends KiiGroup<USER>,
        BUCKET extends KiiBucket,
        OBJECT extends KiiObject<BUCKET>> {
    public interface LoginCallback<T extends KiiUser> extends KiiCallback {
        void onSuccess(String token, T user);
    }
    void loginAsAdmin(String clientId, String clientSecret, LoginCallback<USER> callback);
    
    void loginAsUser(String identifier, String password, LoginCallback<USER> callback);
    
    public interface SignupCallback<T extends KiiUser> extends KiiCallback {
        void onSuccess(T user);
    }
    void signup(JSONObject info, String password, SignupCallback<USER> callback);
    
    void setAccessToken(String accessToken);
    
    String getAccessToken();
    
    UserAPI<USER> userAPI();
    
    GroupAPI<USER, GROUP> groupAPI();
    
    BucketAPI<BUCKET, OBJECT> bucketAPI();
    
    ObjectAPI<BUCKET, OBJECT> objectAPI();
    
    TopicAPI topicAPI();
    
    ACLAPI aclAPI();
}
