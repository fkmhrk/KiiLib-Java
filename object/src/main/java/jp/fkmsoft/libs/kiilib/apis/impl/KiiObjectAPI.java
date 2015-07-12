package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectDTO;

public class KiiObjectAPI implements ObjectAPI {

    private final KiiContext mcontext;

    public KiiObjectAPI(KiiContext kiiContext) {
        mcontext = kiiContext;
    }

    @Override
    public <T extends KiiObject> void getById(final KiiBucket bucket, String id, final KiiObjectDTO<T> dto, ObjectCallback<T> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + bucket.getResourcePath() + "/objects/" + id;
        
        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.GET, url, mcontext.getAccessToken(), null, null, null, new KiiResponseHandler<ObjectCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<T> callback) {
                callback.onSuccess(dto.fromJson(bucket, response));
            }
        });
    }

    @Override
    public <T extends KiiObject> void create(final KiiBucket bucket, JSONObject obj, final KiiObjectDTO<T> dto, ObjectCallback<T> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + bucket.getResourcePath() + "/objects";
        
        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mcontext.getAccessToken(), "application/json", null, obj, new KiiResponseHandler<ObjectCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<T> callback) {
                try {
                    String id = response.getString("objectID");
                    response.put("_id", id);
                    response.remove("objectID");

                    long createdTime = response.getLong("createdAt");
                    response.put("_created", createdTime);
                    response.remove("createdAt");
                    
                    T obj = dto.fromJson(bucket, response);
                    obj.setModifiedTime(createdTime);
                    obj.setVersion(etag);
                    callback.onSuccess(obj);
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public void save(final KiiObject obj, ObjectCallback<KiiObject> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + obj.getResourcePath();
        
        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mcontext.getAccessToken(), "application/json", null, obj.toJson(), new KiiResponseHandler<ObjectCallback<KiiObject>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<KiiObject> callback) {
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
    public void updatePatch(final KiiObject obj, final JSONObject patch, ObjectCallback<KiiObject> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + obj.getResourcePath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        
        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mcontext.getAccessToken(), "application/json", headers, patch, new KiiResponseHandler<ObjectCallback<KiiObject>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<KiiObject> callback) {
                // copy to obj
                obj.updateFields(patch);
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
    public void updatePatchIfUnmodified(final KiiObject obj, final JSONObject patch, ObjectCallback<KiiObject> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + obj.getResourcePath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        headers.put("If-Match", obj.getVersion());

        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mcontext.getAccessToken(), "application/json", headers, patch, new KiiResponseHandler<ObjectCallback<KiiObject>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<KiiObject> callback) {
                // copy to obj
                obj.updateFields(patch);
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
    public void updateBody(final KiiObject obj, String contentType, InputStream source, ObjectCallback<KiiObject> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + obj.getResourcePath() + "/body";

        mcontext.getHttpClient().sendStreamRequest(KiiHTTPClient.Method.PUT, url, mcontext.getAccessToken(), contentType, null, source, new KiiResponseHandler<ObjectCallback<KiiObject>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback<KiiObject> callback) {
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
    public void publish(KiiObject obj, PublishCallback callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + obj.getResourcePath() + "/body/publish";
        
        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mcontext.getAccessToken(), "application/vnd.kii.ObjectBodyPublicationRequest+json", null, null, new KiiResponseHandler<PublishCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, PublishCallback callback) {
                String url = response.optString("url", null);
                callback.onSuccess(url);
            }
        });        
    }

    @Override
    public void delete(KiiObject obj, KiiItemCallback<Void> callback) {
        String url = mcontext.getBaseUrl() + "/apps/" + mcontext.getAppId() + obj.getResourcePath();
        
        mcontext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mcontext.getAccessToken(), null, null, null, new KiiResponseHandler<KiiItemCallback<Void>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, KiiItemCallback<Void> callback) {
                callback.onSuccess(null);
            }
        });
    }
}
