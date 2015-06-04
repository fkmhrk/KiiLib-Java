package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseUser;
import jp.fkmsoft.libs.kiilib.entities.KiiGroupFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiUserFactory;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiGroupAPI<USER extends KiiBaseUser, GROUP extends KiiBaseGroup<USER>> implements GroupAPI<USER, GROUP> {

    private final KiiAppAPI api;
    private final KiiUserFactory<USER> mUserFactory;
    private final KiiGroupFactory<USER, GROUP> mGroupFactory;

    KiiGroupAPI(KiiAppAPI api, KiiUserFactory<USER> userFactory, KiiGroupFactory<USER, GROUP> groupFactory) {
        this.api = api;
        this.mUserFactory = userFactory;
        this.mGroupFactory = groupFactory;
    }
    
    @Override
    public void getOwnedGroup(USER user, final ListCallback<GROUP> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/groups?owner=" + user.getId();
        getGroup(url, callback);
    }

    @Override
    public void getJoinedGroup(USER user, ListCallback<GROUP> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/groups?is_member=" + user.getId();
        getGroup(url, callback);
    }
    
    private void getGroup(String url, final ListCallback<GROUP> callback) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.GroupsRetrievalResponse+json");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, headers, null, new KiiResponseHandler<ListCallback<GROUP>>(callback) {

            @Override
            protected void onSuccess(JSONObject response, String etag, ListCallback<GROUP> callback) {
                try {
                    JSONArray array = response.getJSONArray("groups");
                    List<GROUP> result = new ArrayList<GROUP>(array.length());
                    for (int i = 0 ; i < array.length() ; ++i) {
                        JSONObject json = array.getJSONObject(i);
                        String id = json.getString("groupID");
                        String name = json.getString("name");
                        String owner = json.getString("owner");
                        result.add(mGroupFactory.create(id, name, mUserFactory.create(owner)));
                    }
                    
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }

    @Override
    public void create(final String groupName, final USER owner, List<USER> memberList, final GroupCallback<GROUP> callback) {
        if (memberList == null) { memberList = Collections.emptyList(); }
        
        JSONObject json = new JSONObject();
        try {
            json.put("name", groupName);
            json.put("owner", owner.getId());
            
            JSONArray memberArray = new JSONArray();
            for (KiiBaseUser member : memberList) {
                memberArray.put(member.getId());
            }
            json.put("members", memberArray);
        } catch (JSONException e) {
            return;
        }
        
        String url = api.baseUrl + "/apps/" + api.appId + "/groups";
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, 
                "application/vnd.kii.GroupCreationRequest+json", null, json, new KiiResponseHandler<GroupCallback<GROUP>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<GROUP> callback) {
                try {
                    String id = response.getString("groupID");
                    callback.onSuccess(mGroupFactory.create(id, groupName, owner));
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }

    @Override
    public void getMembers(GROUP group, final MemberCallback<USER> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members";
            
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.MembersRetrievalResponse+json");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, headers, null, new KiiResponseHandler<MemberCallback<USER>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, MemberCallback<USER> callback) {
                try {
                    JSONArray array = response.getJSONArray("members");
                    
                    List<USER> result = new ArrayList<USER>(array.length());
                    for (int i = 0 ; i < array.length() ; ++i) {
                        JSONObject json = array.getJSONObject(i);
                        String id = json.getString("userID");
                        result.add(mUserFactory.create(id));
                    }
                    
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        });
    }

    @Override
    public void addMember(final GROUP group, final USER user, final GroupCallback<GROUP> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members/" + user.getId();
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, "", null, null, new KiiResponseHandler<GroupCallback<GROUP>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<GROUP> callback) {
                group.getMembers().add(user);
                callback.onSuccess(group);
            }
        });
    }

    @Override
    public void removeMember(final GROUP group, final USER user, final GroupCallback<GROUP> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members/" + user.getId();

        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, "", null, null, new KiiResponseHandler<GroupCallback<GROUP>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<GROUP> callback) {
                group.getMembers().remove(user);
                callback.onSuccess(group);
            }
        });
    }

    @Override
    public void changeName(final GROUP group, final String name, final GroupCallback<GROUP> callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/name";
        
        api.getHttpClient().sendPlainTextRequest(Method.PUT, url, api.accessToken, 
                null, name, new KiiResponseHandler<GroupCallback<GROUP>>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback<GROUP> callback) {
                callback.onSuccess(mGroupFactory.create(group.getId(), name, group.getOwner()));
            }
        });
    }
}
