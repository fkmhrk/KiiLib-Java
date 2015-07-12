package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides group API.
 */
public interface GroupAPI {

    /**
     * Gets owned group list.
     * @param user Target user.
     * @param dto DTO for KiiGroup.
     * @param callback Callback object.
     * @param <T> Type of Kii Group.
     */
    <T extends KiiGroup> void getOwnedGroup(KiiUser user, KiiDTO<T> dto, GroupListCallback<T> callback);

    /**
     * Gets joined group list.
     * @param user Target user.
     * @param dto DTO for KiiGroup.
     * @param callback Callback object.
     * @param <T> Type of Kii Group.
     */
    <T extends KiiGroup> void getJoinedGroup(KiiUser user, KiiDTO<T> dto, GroupListCallback<T> callback);

    /**
     * Creates new Group.
     * @param groupName Group name.
     * @param owner Group owner.
     * @param memberList Member list.
     * @param dto DTO for KiiGroup.
     * @param callback Callback object.
     * @param <T> Type of Kii Group.
     */
    <T extends KiiGroup> void create(String groupName, KiiUser owner, List<KiiUser> memberList, KiiDTO<T> dto, GroupCallback<T> callback);

    /**
     * Gets member list.
     * @param group Target group.
     * @param dto DTO for Kii User.
     * @param callback Callback object.
     * @param <U> Type of Kii User.
     */
    <U extends KiiUser> void getMembers(KiiGroup group, KiiDTO<U> dto, MemberCallback<U> callback);

    /**
     * Adds user to target group.
     * @param group Target group.
     * @param user User to be added.
     * @param callback Callback object.
     */
    void addMember(KiiGroup group, KiiUser user, GroupCallback<KiiGroup> callback);

    /**
     * Removes user from target group.
     * @param group Target group.
     * @param user User to be removed.
     * @param callback Callback object.
     */
    void removeMember(KiiGroup group, KiiUser user, GroupCallback<KiiGroup> callback);

    /**
     * Changes group name.
     * @param group Target group.
     * @param name New group name.
     * @param callback Callback object.
     */
    void changeName(KiiGroup group, String name, GroupCallback<KiiGroup> callback);

    /**
     * Deletes target group.
     * @param group Target group
     * @param callback Callback object.
     */
    void delete(KiiGroup group, KiiItemCallback<Void> callback);

    interface GroupCallback<T extends KiiGroup> extends KiiItemCallback<T> { }

    interface GroupListCallback<T extends KiiGroup> extends KiiItemCallback<List<T>> { }

    interface MemberCallback<T extends KiiUser> extends KiiItemCallback<List<T>> { }
}
