package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.KiiResponseHandler;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

public class KiiGroupAPI implements GroupAPI {

    private final KiiContext mContext;

    public KiiGroupAPI(KiiContext context) {
        mContext = context;
    }

    @Override
    public <T extends KiiGroup> void getOwnedGroup(KiiUser user, KiiDTO<T> dto, GroupListCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/groups?owner=" + user.getId();
        getGroup(url, dto, callback);
    }

    @Override
    public <T extends KiiGroup> void getJoinedGroup(KiiUser user, KiiDTO<T> dto, GroupListCallback<T> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/groups?is_member=" + user.getId();
        getGroup(url, dto, callback);
    }
    
    private <T extends KiiGroup> void getGroup(String url, final KiiDTO<T> dto, final GroupListCallback<T> callback) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.GroupsRetrievalResponse+json");
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.GET, url, mContext.getAccessToken(), null, headers, null, new KiiResponseHandler<GroupListCallback<T>>(callback) {

            @Override
            protected void onSuccess(JSONObject response, String etag, GroupListCallback<T> callback) {
                try {
                    JSONArray array = response.getJSONArray("groups");
                    List<T> result = new ArrayList<T>(array.length());
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject json = array.getJSONObject(i);
//                        String id = json.getString("groupID");
//                        String name = json.getString("name");
//                        String owner = json.getString("owner");
                        result.add(dto.fromJson(json));
                    }

                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public <T extends KiiGroup> void create(final String groupName, final KiiUser owner, List<KiiUser> memberList, final KiiDTO<T> dto, GroupCallback<T> callback) {
        if (memberList == null) { memberList = Collections.emptyList(); }
        
        JSONObject json = new JSONObject();
        final JSONArray memberArray = new JSONArray();
        try {
            json.put("name", groupName);
            json.put("owner", owner.getId());

            for (KiiUser member : memberList) {
                memberArray.put(member.getId());
            }
            json.put("members", memberArray);
        } catch (JSONException e) {
            callback.onError(new KiiException(2000, e));
            return;
        }
        
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/groups";
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.POST, url, mContext.getAccessToken(), "application/vnd.kii.GroupCreationRequest+json", null, json, new KiiResponseHandler<GroupCallback<T>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<T> callback) {
                try {
                    //response.getString("groupID");
                    response.put("name", groupName);
                    response.put("owner", owner.getId());
                    response.put("members", memberArray);
                    callback.onSuccess(dto.fromJson(response));
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public <U extends KiiUser> void getMembers(KiiGroup group, final KiiDTO<U> dto, MemberCallback<U> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + group.getResourcePath() + "/members";
            
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.MembersRetrievalResponse+json");
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.GET, url, mContext.getAccessToken(), null, headers, null, new KiiResponseHandler<MemberCallback<U>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, MemberCallback<U> callback) {
                try {
                    JSONArray array = response.getJSONArray("members");
                    
                    List<U> result = new ArrayList<U>(array.length());
                    for (int i = 0 ; i < array.length() ; ++i) {
                        JSONObject json = array.getJSONObject(i);
                        String id = json.getString("userID");
                        json.remove("userID");
                        json.put("UserID", id);
                        result.add(dto.fromJson(json));
                    }
                    
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(new KiiException(599, e));
                }
            }
        });
    }

    @Override
    public void addMember(final KiiGroup group, final KiiUser user, GroupCallback<KiiGroup> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + group.getResourcePath() + "/members/" + user.getId();
        
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<GroupCallback<KiiGroup>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<KiiGroup> callback) {
                // group.getMembers().add(user);
                callback.onSuccess(group);
            }
        });
    }

    @Override
    public void removeMember(final KiiGroup group, final KiiUser user, GroupCallback<KiiGroup> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + group.getResourcePath() + "/members/" + user.getId();

        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<GroupCallback<KiiGroup>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<KiiGroup> callback) {
                // group.getMembers().remove(user);
                callback.onSuccess(group);
            }
        });
    }

    @Override
    public void changeName(final KiiGroup group, String name, GroupCallback<KiiGroup> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + group.getResourcePath() + "/name";
        
        mContext.getHttpClient().sendPlainTextRequest(KiiHTTPClient.Method.PUT, url, mContext.getAccessToken(), null, name, new KiiResponseHandler<GroupCallback<KiiGroup>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<KiiGroup> callback) {
                callback.onSuccess(group);
            }
        });
    }

    @Override
    public void delete(KiiGroup group, KiiItemCallback<Void> callback) {
        String url = mContext.getBaseUrl() + "/apps/" + mContext.getAppId() + "/groups/" + group.getId();
        mContext.getHttpClient().sendJsonRequest(KiiHTTPClient.Method.DELETE, url, mContext.getAccessToken(), null, null, null, new KiiResponseHandler<KiiItemCallback<Void>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, KiiItemCallback<Void> callback) {
                callback.onSuccess(null);
            }
        });
    }
}
