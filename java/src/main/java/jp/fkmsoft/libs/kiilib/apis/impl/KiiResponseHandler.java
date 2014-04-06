package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

abstract class KiiResponseHandler<T extends KiiCallback> implements ResponseHandler {

    private final T callback;
    
    KiiResponseHandler(T callback) {
        this.callback = callback;
    }
    
    @Override
    public void onResponse(int status, JSONObject response, String etag) {
        if (status < 300) {
            onSuccess(response, etag, callback);
        } else {
            callback.onError(new KiiException(status, response));
        }
    }

    protected void onSuccess(JSONObject response, String etag, T callback)
    {
        // nop
    }
 
    
    @Override
    public void onException(Exception e) {
        callback.onError(e);
    }

}
