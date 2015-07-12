package jp.fkmsoft.libs.kiilib.apis;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;

abstract public class KiiResponseHandler<T extends KiiCallback> implements KiiHTTPClient.ResponseHandler {

    private final T callback;
    
    public KiiResponseHandler(T callback) {
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

    @Override
    public void onException(Exception e) {
        callback.onError(new KiiException(599, e));
    }

    abstract protected void onSuccess(JSONObject response, String etag, T callback);
}
