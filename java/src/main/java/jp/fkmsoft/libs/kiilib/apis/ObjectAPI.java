package jp.fkmsoft.libs.kiilib.apis;

import java.io.InputStream;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

import org.json.JSONObject;


public interface ObjectAPI<BUCKET extends KiiBucket, OBJECT extends KiiObject<BUCKET>> {
    public interface ObjectCallback<T extends KiiBucket, U extends KiiObject<T>> extends KiiCallback {
        void onSuccess(U obj);
    };
    
    public interface PublishCallback extends KiiCallback {
        void onSuccess(String url);
    };
    
    void getById(BUCKET bucket, String id, ObjectCallback<BUCKET, OBJECT> callback);
    
    void create(BUCKET bucket, JSONObject obj, ObjectCallback<BUCKET, OBJECT> callback);
    
    void update(OBJECT obj, ObjectCallback<BUCKET, OBJECT> callback);
    
    void updatePatch(OBJECT obj, JSONObject patch, ObjectCallback<BUCKET, OBJECT> callback);
    
    void updatePatchIfUnmodified(OBJECT obj, JSONObject patch, ObjectCallback<BUCKET, OBJECT> callback);
    
    void updateBody(OBJECT obj, InputStream source, String contentType, ObjectCallback<BUCKET, OBJECT> callback);
    
    void publish(OBJECT obj, PublishCallback callback);
    
    void delete(OBJECT obj, ObjectCallback<BUCKET, OBJECT> callback);
}
