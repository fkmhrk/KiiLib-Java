package jp.fkmsoft.libs.kiilib.apis;

import java.io.InputStream;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

import org.json.JSONObject;


public interface ObjectAPI<T extends KiiObject> {
    public interface ObjectCallback<U extends KiiObject> extends KiiCallback {
        void onSuccess(U obj);
    };
    
    public interface PublishCallback extends KiiCallback {
        void onSuccess(String url);
    };
    
    void getById(KiiBucket bucket, String id, ObjectCallback<T> callback);
    
    void create(KiiBucket bucket, JSONObject obj, ObjectCallback<T> callback);
    
    void update(T obj, ObjectCallback<T> callback);
    
    void updatePatch(T obj, JSONObject patch, ObjectCallback<T> callback);
    
    void updatePatchIfUnmodified(T obj, JSONObject patch, ObjectCallback<T> callback);
    
    void updateBody(T obj, InputStream source, String contentType, ObjectCallback<T> callback);
    
    void publish(T obj, PublishCallback callback);
    
    void delete(T obj, ObjectCallback<T> callback);
}
