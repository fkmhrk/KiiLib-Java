package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.apis.QueryParams;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectDTO;

public class KiiBucketAPI implements BucketAPI {

    private final KiiContext mContext;

    public KiiBucketAPI(KiiContext context) {
        mContext = context;
    }

    @Override
    public <T extends KiiObject> void query(final KiiBucket bucket, QueryParams params, final KiiObjectDTO<T> dto, QueryCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + bucket.getResourcePath() + "/query";
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mContext.getAccessToken(), "application/vnd.kii.QueryRequest+json", null, params.toJson(), new KiiResponseHandler<QueryCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, QueryCallback<T> callback) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    
                    KiiObjectQueryResult<T> result = new KiiObjectQueryResult<T>(array.length());
                    for (int i = 0 ; i < array.length() ; ++i) {
                        result.add(dto.fromJson(bucket, array.getJSONObject(i)));
                    }
                    
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public void delete(KiiBucket bucket, KiiItemCallback<Void> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + bucket.getResourcePath();
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<KiiItemCallback<Void>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, KiiItemCallback<Void> callback) {
                callback.onSuccess(null);
            }
        });
    }
}
