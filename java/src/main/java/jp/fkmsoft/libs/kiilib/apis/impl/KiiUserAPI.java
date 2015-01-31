package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseUser;
import jp.fkmsoft.libs.kiilib.entities.KiiUserFactory;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Override;

class KiiUserAPI<T extends KiiBaseUser> implements UserAPI<T> {

    private final KiiAppAPI api;
    private final KiiUserFactory<T> mFactory;
    
    KiiUserAPI(KiiAppAPI api, KiiUserFactory<T> factory) {
        this.api = api;
        this.mFactory = factory;
    }
    
    @Override
    public void getById(String id, UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/users/" + id;
        getUser(url, callback);
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
    
    private void findUser(String identifier, UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/users/" + identifier;
        getUser(url, callback);
    }

    private void getUser(String url, final UserCallback<T> callback) {
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
                    callback.onError(e);
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
            callback.onError(e);
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
                    callback.onError(e);
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
            callback.onError(e);
            return;
        }
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, 
                "application/vnd.kii.PhoneNumberModificationRequest+json", null, json, new ResponseHandler() {
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
                    user.put("phoneNumber", newPhone);
                    callback.onSuccess(user);
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
    public void resetPassword(String email, final UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/users/EMAIL:" + email +
                "/password/request-reset";
        api.getHttpClient().sendPlainTextRequest(Method.POST, url, api.accessToken, null, "", new ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void changePassword(T user, String currentPassword, String newPassword, final UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + user.getResourcePath() + "/password";
        JSONObject json = new JSONObject();
        try {
            json.put("oldPassword", currentPassword);
            json.put("newPassword", newPassword);
        } catch (JSONException e) {
            callback.onError(e);
            return;
        }

        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken,
                "application/vnd.kii.ChangePasswordRequest+json", null, json, new ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }

            private void success(JSONObject response) {
                callback.onSuccess(null);
            }

            @Override
            public void onException(Exception e) {
                callback.onError(e);
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
            callback.onError(e);
            return;
        }
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, 
                "application/vnd.kii.InstallationCreationRequest+json", null, json, new ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }
            
            private void success(JSONObject response) {
                callback.onSuccess(null);
            }

            @Override
            public void onException(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void uninstallDevice(String regId, final UserCallback<T> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/installations/ANDROID:" + regId;
        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken,
                null, null, null, new ResponseHandler() {
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status == 204) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new KiiException(status, response));
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(e);
            }
        });
    }
}
