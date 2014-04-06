package jp.fkmsoft.libs.kiilib.apis;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseUser;

/**
 * Provides Kii API
 * @author fkm
 *
 */
public interface AppAPI<
        USER extends KiiBaseUser,
        GROUP extends KiiBaseGroup<USER>,
        BUCKET extends KiiBaseBucket,
        OBJECT extends KiiBaseObject<BUCKET>,
        TOPIC extends KiiBaseTopic
        > {
    public interface LoginCallback<T extends KiiBaseUser> extends KiiCallback {
        void onSuccess(String token, T user);
    }
    void loginAsAdmin(String clientId, String clientSecret, LoginCallback<USER> callback);
    
    void loginAsUser(String identifier, String password, LoginCallback<USER> callback);
    
    public interface SignupCallback<T extends KiiBaseUser> extends KiiCallback {
        void onSuccess(T user);
    }
    void signup(JSONObject info, String password, SignupCallback<USER> callback);
    
    void setAccessToken(String accessToken);
    
    String getAccessToken();
    
    UserAPI<USER> userAPI();
    
    GroupAPI<USER, GROUP> groupAPI();
    
    BucketAPI<BUCKET, OBJECT> bucketAPI();
    
    ObjectAPI<BUCKET, OBJECT> objectAPI();
    
    TopicAPI<TOPIC> topicAPI();
    
    ACLAPI aclAPI();
}
