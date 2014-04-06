package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectFactory;
import jp.fkmsoft.libs.kiilib.entities.QueryParams;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiBucketAPI<BUCKET extends KiiBaseBucket, OBJECT extends KiiBaseObject<BUCKET>> implements BucketAPI<BUCKET, OBJECT> {

    private final KiiAppAPI api;
    private final KiiObjectFactory<BUCKET, OBJECT> mObjectFactory;
    
    KiiBucketAPI(KiiAppAPI api, KiiObjectFactory<BUCKET, OBJECT> objectFactory) {
        this.api = api;
        this.mObjectFactory = objectFactory;
    }

    @Override
    public void query(final BUCKET bucket, QueryParams condition, final QueryCallback<BUCKET, OBJECT> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/query";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, 
                "application/vnd.kii.QueryRequest+json", null, condition.toJson(), new KiiResponseHandler<QueryCallback<BUCKET, OBJECT>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, QueryCallback<BUCKET, OBJECT> callback) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    
                    KiiObjectQueryResult<BUCKET, OBJECT> result = new KiiObjectQueryResult<BUCKET, OBJECT>(array.length());
                    for (int i = 0 ; i < array.length() ; ++i) {
                        result.add(mObjectFactory.create(bucket, array.getJSONObject(i)));
                    }
                    
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }

    @Override
    public void delete(final BUCKET bucket, BucketCallback<BUCKET> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath();
        
        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<BucketCallback<BUCKET>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, BucketCallback<BUCKET> callback) {
                callback.onSuccess(bucket);
            }
        });
    }
}
