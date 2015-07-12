package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONException;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Implementation
 */
public class KiiUserAPI implements UserAPI {

    private final KiiContext mContext;

    KiiUserAPI(KiiContext context) {
        mContext = context;
    }

    @Override
    public <T extends KiiUser> void getById(String id, KiiDTO<T> dto, UserCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/users/" + id;
        getUser(url, dto, callback);
    }

    @Override
    public <T extends KiiUser> void findUserByUsername(String username, KiiDTO<T> dto, UserCallback<T> callback) {
        findUser("LOGIN_NAME:" + username, dto, callback);
    }

    @Override
    public <T extends KiiUser> void findUserByEmail(String email, KiiDTO<T> dto, UserCallback<T> callback) {
        findUser("EMAIL:" + email, dto, callback);
    }

    @Override
    public <T extends KiiUser> void findUserByPhone(String phone, KiiDTO<T> dto, UserCallback<T> callback) {
        findUser("PHONE:" + phone, dto, callback);
    }
    
    private <T extends KiiUser> void findUser(String identifier, KiiDTO<T> dto, UserCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/users/" + identifier;
        getUser(url, dto, callback);
    }

    private <T extends KiiUser> void getUser(String url, final KiiDTO<T> dto, final UserCallback<T> callback) {
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.GET, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<UserCallback<T>>(callback) {

            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback<T> callback) {
                try {
                    String id = response.getString("userID");
                    response.remove("userID");
                    response.put("UserID", id);
                    callback.onSuccess(dto.fromJson(response));
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public void updateEmail(final KiiUser user, final String newEmail, boolean verified, UserCallback<KiiUser> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + user.getResourcePath() + "/email-address";
        JSONObject json = new JSONObject();
        try {
            json.put("emailAddress", newEmail);
            if (verified) {
                json.put("verified", true);
            }
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), "application/vnd.kii.EmailAddressModificationRequest+json", null, json, new KiiResponseHandler<UserCallback<KiiUser>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback<KiiUser> callback) {
                callback.onSuccess(user);
            }
        });
    }

    @Override
    public void updatePhone(final KiiUser user, final String newPhone, boolean verified, final UserCallback<KiiUser> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + user.getResourcePath() + "/phone-number";
        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", newPhone);
            if (verified) {
                json.put("verified", true);
            }
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), "application/vnd.kii.PhoneNumberModificationRequest+json", null, json, new KiiResponseHandler<UserCallback<KiiUser>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback<KiiUser> callback) {
                callback.onSuccess(user);
            }
        });
    }

    @Override
    public void resetPassword(String email, final KiiItemCallback<Void> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/users/EMAIL:" + email +
                "/password/request-reset";
        mContext.getHttpClient().sendPlainTextRequest(KiiHTTPClient.Method.POST, url, mContext.getAccessToken(), null, "", new KiiHTTPClient.ResponseHandler() {
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
                callback.onError(new KiiException(599, e));
            }
        });
    }

    @Override
    public void changePassword(KiiUser user, String currentPassword, String newPassword, final KiiItemCallback<Void> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + user.getResourcePath() + "/password";
        JSONObject json = new JSONObject();
        try {
            json.put("oldPassword", currentPassword);
            json.put("newPassword", newPassword);
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), "application/vnd.kii.ChangePasswordRequest+json", null, json, new KiiHTTPClient.ResponseHandler() {
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
                callback.onError(new KiiException(599, e));
            }
        });
    }

    @Override
    public void installDevice(String regId, final KiiItemCallback<Void> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/installations";
        JSONObject json = new JSONObject();
        try {
            json.put("installationRegistrationID", regId);
            json.put("deviceType", "ANDROID");
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mContext.getAccessToken(), "application/vnd.kii.InstallationCreationRequest+json", null, json, new KiiHTTPClient.ResponseHandler() {
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
                callback.onError(new KiiException(599, e));
            }
        });
    }

    @Override
    public void uninstallDevice(String regId, final KiiItemCallback<Void> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/installations/ANDROID:" + regId;
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mContext.getAccessToken(), null, null, null, new KiiHTTPClient.ResponseHandler() {
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
                callback.onError(new KiiException(599, e));
            }
        });
    }
}
