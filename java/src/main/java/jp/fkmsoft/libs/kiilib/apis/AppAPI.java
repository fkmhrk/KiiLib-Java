package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

import org.json.JSONObject;

/**
 * Provides Kii API
 * @author fkm
 *
 */
public interface AppAPI<
        USER extends KiiUser,
        BUCKET extends KiiBucket,
        OBJECT extends KiiObject<BUCKET>> {
    public interface LoginCallback extends KiiCallback {
        void onSuccess(String token, KiiUser user);
    }
    void loginAsAdmin(String clientId, String clientSecret, LoginCallback callback);
    
    void loginAsUser(String identifier, String password, LoginCallback callback);
    
    public interface SignupCallback extends KiiCallback {
        void onSuccess(KiiUser user);
    }
    void signup(JSONObject info, String password, SignupCallback callback);
    
    void setAccessToken(String accessToken);
    
    String getAccessToken();
    
    UserAPI<USER> userAPI();
    
    GroupAPI groupAPI();
    
    BucketAPI<BUCKET, OBJECT> bucketAPI();
    
    ObjectAPI<BUCKET, OBJECT> objectAPI();
    
    TopicAPI topicAPI();
    
    ACLAPI aclAPI();
}
