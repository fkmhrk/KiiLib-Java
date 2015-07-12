package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

public class KiiACLAPI implements ACLAPI {

    private final KiiContext mContext;

    public KiiACLAPI(KiiContext context) {
        mContext = context;
    }

    @Override
    public <T extends KiiUser, U extends KiiGroup> void get(AccessControllable object, final KiiDTO<T> userDTO, final KiiDTO<U> groupDTO, ACLGetCallback callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + object.getResourcePath() + "/acl";
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "*");
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.GET, url, mContext.getAccessToken(), null, headers, null, new KiiResponseHandler<ACLGetCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ACLGetCallback callback) {
                try {
                    Map<String, List<ACLSubject>> map = new HashMap<String, List<ACLSubject>>(response.length());
                    @SuppressWarnings("unchecked")
                    Iterator<String> it = response.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        JSONArray array = response.getJSONArray(key);
                        List<ACLSubject> list = toSubjectList(array);
                        map.put(key, list);
                    }
                    callback.onSuccess(map);
                } catch (JSONException e) {
                     callback.onError(new KiiException(599, e));
                }
            }
            
            private List<ACLSubject> toSubjectList(JSONArray array) throws JSONException {
                List<ACLSubject> list = new ArrayList<ACLSubject>(array.length());
                for (int i = 0 ; i < array.length() ; ++i) {
                    JSONObject item = array.getJSONObject(i);
                    if (item.has("userID")) {
                        String id = item.getString("userID");
                        item.put("UserID", id);
                        item.remove("userID");
                        list.add(userDTO.fromJson(item));
                    } else if (item.has("groupID")) {
                        list.add(groupDTO.fromJson(item));
                    }
                }
                return list;
            }
        });
    }

    @Override
    public void grant(final AccessControllable object, String action, ACLSubject subject, final ACLCallback callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + object.getResourcePath() + "/acl/" + action +
                "/" + subject.getSubjectType() + ":" + subject.getId();
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<ACLCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ACLCallback callback) {
                callback.onSuccess(object);
            }
        });
    }

    @Override
    public void revoke(final AccessControllable object, String action, ACLSubject subject, final ACLCallback callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + object.getResourcePath() + "/acl/" + action +
                "/" + subject.getSubjectType() + ":" + subject.getId();
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<ACLCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ACLCallback callback) {
                callback.onSuccess(object);
            }
        });
    }
}
