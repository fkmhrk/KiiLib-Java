package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiBaseGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseUser;

/**
 * Provides group API. To get this instance, Please call {@link AppAPI#groupAPI()}
 * @author fkm
 *
 */
public interface GroupAPI<USER extends KiiBaseUser, GROUP extends KiiBaseGroup> {
    public interface ListCallback<U extends KiiBaseGroup> extends KiiCallback {
        void onSuccess(List<U> result);
    }
    void getOwnedGroup(USER user, ListCallback<GROUP> callback);
    
    void getJoinedGroup(USER user, ListCallback<GROUP> callback);
    
    public interface GroupCallback<U extends KiiBaseGroup> extends KiiCallback {
        void onSuccess(U group);
    }
    
    void create(String groupName, USER owner, List<USER> memberList, GroupCallback<GROUP> callback);
    
    public interface MemberCallback<U extends KiiBaseUser> extends KiiCallback {
        void onSuccess(List<U> memberList);
    }
    
    void getMembers(GROUP group, MemberCallback<USER> callback);
    
    void addMember(GROUP group, USER user, GroupCallback<GROUP> callback);

    void removeMember(GROUP group, USER user, GroupCallback<GROUP> callback);
    
    void changeName(GROUP group, String name, GroupCallback<GROUP> callback);
}
