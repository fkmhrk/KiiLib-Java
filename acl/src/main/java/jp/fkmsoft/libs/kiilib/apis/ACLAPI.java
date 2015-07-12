package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;
import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;


/**
 * Provides ACL API.
 */
public interface ACLAPI {
    /**
     * Gets ACL of target Entity.
     * @param object Target entity.
     * @param callback Callback object.
     */
    <T extends KiiUser, U extends KiiGroup> void get(AccessControllable object, KiiDTO<T> userDTO, KiiDTO<U> groupDTO, ACLGetCallback callback);

    /**
     * Grants action of target to subject.
     * @param object Target entity.
     * @param action Action.
     * @param subject Action Subject.
     * @param callback Callback object.
     */
    void grant(AccessControllable object, String action, ACLSubject subject, ACLCallback callback);

    /**
     * Revokes action of target from subject.
     * @param object Target entity.
     * @param action Action.
     * @param subject Action subject
     * @param callback Callback object
     */
    void revoke(AccessControllable object, String action, ACLSubject subject, ACLCallback callback);

    interface ACLGetCallback extends KiiItemCallback<Map<String, List<ACLSubject>>> { }

    interface ACLCallback extends KiiItemCallback<AccessControllable> { }
}
