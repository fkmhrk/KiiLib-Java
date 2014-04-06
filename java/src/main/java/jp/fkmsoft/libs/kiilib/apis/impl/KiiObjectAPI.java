package jp.fkmsoft.libs.kiilib.apis.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectFactory;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONException;
import org.json.JSONObject;

class KiiObjectAPI<BUCKET extends KiiBaseBucket, OBJECT extends KiiBaseObject<BUCKET>> implements ObjectAPI<BUCKET, OBJECT> {

    private final KiiAppAPI api;
    private final KiiObjectFactory<BUCKET, OBJECT> mFactory;

    KiiObjectAPI(KiiAppAPI api, KiiObjectFactory<BUCKET, OBJECT> factory) {
        this.api = api;
        this.mFactory = factory;
    }
    
    @Override
    public void getById(final BUCKET bucket, String id, ObjectCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/objects/" + id;
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                callback.onSuccess(mFactory.create(bucket, response));
            }
        });
    }
    
    @Override
    public void create(final BUCKET bucket, final JSONObject obj, final ObjectCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/objects";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/json", null, obj, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                try {
                    long createdTime = response.getLong("createdAt");
                    String id = response.getString("objectID");
                    obj.put("_id", id);
                    
                    OBJECT kiiObj = mFactory.create(bucket, obj);
                    kiiObj.setCreatedTime(createdTime);
                    kiiObj.setModifiedTime(createdTime);
                    kiiObj.setVersion(etag);
                    
                    callback.onSuccess(kiiObj);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }

    @Override
    public void save(final OBJECT obj, final ObjectCallback<BUCKET, OBJECT> callback) {

        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken,
                "application/json", null, obj, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                long modifiedTime = response.optLong("modifiedAt", -1);
                if (modifiedTime != -1) {
                    obj.setModifiedTime(modifiedTime);
                }
                obj.setVersion(etag);
                callback.onSuccess(obj);
            }
        });
    }

    @Override
    public void updatePatch(final OBJECT obj, final JSONObject patch, final ObjectCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/json", headers, patch, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                // copy to obj
                @SuppressWarnings("unchecked")
                Iterator<String> keys = patch.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        obj.put(key, patch.opt(key));
                    } catch (JSONException e) {
                        // nop
                    }
                }
                long modifiedTime = response.optLong("modifiedAt", -1);
                if (modifiedTime != -1) {
                    obj.setModifiedTime(modifiedTime);
                }
                obj.setVersion(etag);
                callback.onSuccess(obj);
            }
        });
    }
    
    @Override
    public void updatePatchIfUnmodified(final OBJECT obj, final JSONObject patch, ObjectCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        headers.put("If-Match", obj.getVersion());
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/json", headers, patch, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                // copy to obj
                @SuppressWarnings("unchecked")
                Iterator<String> keys = patch.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        obj.put(key, patch.opt(key));
                    } catch (JSONException e) {
                        // nop
                    }
                }
                long modifiedTime = response.optLong("modifiedAt", -1);
                if (modifiedTime != -1) {
                    obj.setModifiedTime(modifiedTime);
                }
                obj.setVersion(etag);
                callback.onSuccess(obj);
            }
        });
    }
    
    @Override
    public void updateBody(final OBJECT obj, String contentType, InputStream source, ObjectCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath() + "/body";
        
        api.getHttpClient().sendStreamRequest(Method.PUT, url, api.accessToken,
                contentType, null, source, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                long modifiedTime = response.optLong("modifiedAt", -1);
                if (modifiedTime != -1) {
                    obj.setModifiedTime(modifiedTime);
                }
                obj.setVersion(etag);
                callback.onSuccess(obj);
            }
        });
    }
    
    @Override
    public void publish(OBJECT obj, PublishCallback callback) {

        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath() + "/body/publish";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/vnd.kii.ObjectBodyPublicationRequest+json", null, obj, new KiiResponseHandler<PublishCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, PublishCallback callback) {
                String url = response.optString("url", null);
                callback.onSuccess(url);
            }
        });        
    }
    
    @Override
    public void delete(final OBJECT obj, final ObjectCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        
        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<ObjectCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<BUCKET, OBJECT> callback) {
                callback.onSuccess(obj);
            }
        });
    }

}
