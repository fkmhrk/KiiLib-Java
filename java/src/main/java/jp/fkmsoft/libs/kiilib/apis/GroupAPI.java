package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides group API. To get this instance, Please call {@link AppAPI#groupAPI()}
 * @author fkm
 *
 */
public interface GroupAPI<USER extends KiiUser, GROUP extends KiiGroup> {
    public interface ListCallback<U extends KiiGroup> extends KiiCallback {
        void onSuccess(List<U> result);
    }
    void getOwnedGroup(USER user, ListCallback<GROUP> callback);
    
    void getJoinedGroup(USER user, ListCallback<GROUP> callback);
    
    public interface GroupCallback<U extends KiiGroup> extends KiiCallback {
        void onSuccess(U group);
    }
    
    void create(String groupName, USER owner, List<USER> memberList, GroupCallback<GROUP> callback);
    
    public interface MemberCallback<U extends KiiUser> extends KiiCallback {
        void onSuccess(List<U> memberList);
    }
    
    void getMembers(GROUP group, MemberCallback<USER> callback);
    
    public interface AddCallback<U extends KiiUser> extends KiiCallback {
        void onSuccess(U user);
    }
    
    void addMember(GROUP group, USER user, GroupCallback<GROUP> callback);
    
    void changeName(GROUP group, String name, GroupCallback<GROUP> callback);
}
