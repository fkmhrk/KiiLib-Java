package jp.fkmsoft.libs.kiilib.apis;

import java.io.InputStream;

import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;

import org.json.JSONObject;


public interface ObjectAPI<BUCKET extends KiiBaseBucket, OBJECT extends KiiBaseObject<BUCKET>> {
    public interface ObjectCallback<T extends KiiBaseBucket, U extends KiiBaseObject<T>> extends KiiCallback {
        void onSuccess(U obj);
    }
    
    public interface PublishCallback extends KiiCallback {
        void onSuccess(String url);
    }
    
    void getById(BUCKET bucket, String id, ObjectCallback<BUCKET, OBJECT> callback);
    
    void create(BUCKET bucket, JSONObject obj, ObjectCallback<BUCKET, OBJECT> callback);
    
    void save(OBJECT obj, ObjectCallback<BUCKET, OBJECT> callback);
    
    void updatePatch(OBJECT obj, JSONObject patch, ObjectCallback<BUCKET, OBJECT> callback);
    
    void updatePatchIfUnmodified(OBJECT obj, JSONObject patch, ObjectCallback<BUCKET, OBJECT> callback);
    
    void updateBody(OBJECT obj, String contentType, InputStream source, ObjectCallback<BUCKET, OBJECT> callback);
    
    void publish(OBJECT obj, PublishCallback callback);
    
    void delete(OBJECT obj, ObjectCallback<BUCKET, OBJECT> callback);
}
