package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.KiiUserFactory;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

class KiiUserAPI<T extends KiiUser> implements UserAPI<T> {

    private final KiiAppAPI api;
    private final KiiUserFactory<T> mFactory;
    
    KiiUserAPI(KiiAppAPI api, KiiUserFactory<T> factory) {
        this.api = api;
        this.mFactory = factory;
    }
    
    @Override
    public void findUserByUsername(String username, UserCallback<T> callback) {
        findUser("LOGIN_NAME:" + username, callback);
    }

    @Override
    public void findUserByEmail(String email, UserCallback<T> callback) {
        findUser("EMAIL:" + email, callback);
    }

    @Override
    public void findUserByPhone(String phone, UserCallback<T> callback) {
        findUser("PHONE:" + phone, callback);
    }
    
    private void findUser(String identifier, final UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/users/" + identifier;
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<UserCallback<T>>(callback) {
            
            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback<T> callback) {
                try {
                    String id = response.getString("userID");
                    T user = mFactory.create(id);
                    user.replace(response);
                    callback.onSuccess(user);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }

    @Override
    public void updateEmail(final T user, final String newEmail, boolean verified, final UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + user.getResourcePath() + "/email-address";
        JSONObject json = new JSONObject();
        try {
            json.put("emailAddress", newEmail);
            if (verified) {
                json.put("verified", true);
            }
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, 
                "application/vnd.kii.EmailAddressModificationRequest+json", null, json, new KiiResponseHandler<UserCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback<T> callback) {
                try {
                    user.put("emailAddress", newEmail);
                    callback.onSuccess(user);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }

    @Override
    public void updatePhone(final T user, final String newPhone, boolean verified, final UserCallback<T> callback) {

        String url = api.baseUrl + "/apps/" + api.appId + user.getResourcePath() + "/phone-number";
        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", newPhone);
            if (verified) {
                json.put("verified", true);
            }
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, 
                "application/vnd.kii.PhoneNumberModificationRequest+json", null, json, new ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(status, response.toString());
                }
            }
            
            private void success(JSONObject response) {
                try {
                    user.put("phoneNumber", newPhone);
                    callback.onSuccess(user);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(KiiCallback.STATUS_GENERAL_EXCEPTION, e.getMessage());
            }
        });
    }

    @Override
    public void installDevice(String regId, final UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/installations";
        JSONObject json = new JSONObject();
        try {
            json.put("installationRegistrationID", regId);
            json.put("deviceType", "ANDROID");
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, 
                "application/vnd.kii.InstallationCreationRequest+json", null, json, new ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(status, response.toString());
                }
            }
            
            private void success(JSONObject response) {
                callback.onSuccess(null);
            }

            @Override
            public void onException(Exception e) {
                callback.onError(KiiCallback.STATUS_GENERAL_EXCEPTION, e.getMessage());
            }
        });
    }
}
