package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONException;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.apis.SignupInfo;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;


/**
 * Implementation
 */
public class KiiAppAPI implements AppAPI {

    private final KiiContext mContext;

    public KiiAppAPI(KiiContext context) {
        mContext = context;
    }

    @Override
    public <T extends KiiUser> void loginAsAdmin(String clientId, String clientSecret, final KiiDTO<T> dto, final LoginCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/oauth2/token";

        JSONObject json = new JSONObject();
        try {
            json.put("client_id", clientId);
            json.put("client_secret", clientSecret);
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, null, "application/json", null, json, new KiiHTTPClient.ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }

            private void success(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    // String userId = response.getString("id");
                    mContext.setAccessToken(token);
                    callback.onSuccess(token, dto.fromJson(response));
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(new KiiException(599, e));
            }
        });
    }

    @Override
    public <T extends KiiUser> void loginAsUser(String identifier, String password, final KiiDTO<T> dto, LoginCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/oauth2/token";

        JSONObject json = new JSONObject();
        try {
            json.put("username", identifier);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, null, "application/json", null, json, new KiiResponseHandler<LoginCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, LoginCallback<T> callback) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    mContext.setAccessToken(token);

                    response.remove("access_token");
                    response.put("UserID", userId);
                    callback.onSuccess(token, dto.fromJson(response));
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public <T extends KiiUser> void signup(SignupInfo info, String password, final KiiDTO<T> dto, SignupCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/users";

        JSONObject json = info.toJson();
        try {
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, null, "application/json", null, json, new KiiResponseHandler<SignupCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SignupCallback<T> callback) {
                try {
                    String userId = response.getString("userID");
                    response.put("id", userId);
                    callback.onSuccess(dto.fromJson(response));
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

/*
    @Override
    public void loginAsAdmin(String clientId, String clientSecret, final LoginCallback<USER> callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", clientId);
            json.put("client_secret", clientSecret);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, 
                "application/json", null, json, new ResponseHandler() {
            
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }
            
            private void success(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, mUserFactory.create(userId));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void loginAsUser(String identifier, String password, final LoginCallback<USER> callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("username", identifier);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, "application/json", null, json, new KiiResponseHandler<LoginCallback<USER>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, LoginCallback<USER> callback) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, mUserFactory.create(userId));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }
    
    @Override
    public void signup(JSONObject info, String password, final SignupCallback<USER> callback) {
        String url = baseUrl + "/apps/" + appId + "/users";
        
        try {
            info.put("password", password);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null,
                "application/json", null, info, new KiiResponseHandler<SignupCallback<USER>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SignupCallback<USER> callback) {
                try {
                    String userId = response.getString("userID");
                    callback.onSuccess(mUserFactory.create(userId));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }
*/
}
