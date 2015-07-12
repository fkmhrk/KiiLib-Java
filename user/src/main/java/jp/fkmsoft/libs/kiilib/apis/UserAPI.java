package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides User related APIs.
 */
public interface UserAPI {

    /**
     * Gets User.
     * @param id User ID.
     * @param dto DTO for User.
     * @param callback Callback object.
     * @param <T> Type of KiiUser.
     */
    <T extends KiiUser> void getById(String id, KiiDTO<T> dto, UserCallback<T> callback);

    /**
     * Finds User by username.
     * @param username Username.
     * @param dto DTO for User.
     * @param callback Callback object.
     * @param <T> Type of KiiUser.
     */
    <T extends KiiUser> void findUserByUsername(String username, KiiDTO<T> dto, UserCallback<T> callback);

    /**
     * Finds User by email.
     * @param email Email address.
     * @param dto DTO for User.
     * @param callback Callback object.
     * @param <T> Type of KiiUser.
     */
    <T extends KiiUser> void findUserByEmail(String email, KiiDTO<T> dto, UserCallback<T> callback);

    /**
     * Finds User by phone.
     * @param phone Phone number.
     * @param dto DTO for User.
     * @param callback Callback object.
     * @param <T> Type of KiiUser.
     */
    <T extends KiiUser> void findUserByPhone(String phone, KiiDTO<T> dto, UserCallback<T> callback);

    /**
     * Updates email.
     * @param user Target user.
     * @param newEmail new email address.
     * @param verified true if email is verified.
     * @param callback Callback object.
     */
    void updateEmail(KiiUser user, String newEmail, boolean verified, UserCallback<KiiUser> callback);

    /**
     * Updates phone.
     * @param user Target user.
     * @param newPhone new phone number.
     * @param verified true if phone number is verified.
     * @param callback Callback object.
     */
    void updatePhone(KiiUser user, String newPhone, boolean verified, UserCallback<KiiUser> callback);

    /**
     * Sends reset password request.
     * @param email Email address to be reset.
     * @param callback Callback object.
     */
    void resetPassword(String email, KiiItemCallback<Void> callback);

    /**
     * Sends change password request. Password will be changed wheh user clicks a link in email.
     * @param user Target user.
     * @param currentPassword current password.
     * @param newPassword new password.
     * @param callback Callback object.
     */
    void changePassword(KiiUser user, String currentPassword, String newPassword, KiiItemCallback<Void> callback);

    /**
     * Installs push device token to Kii Cloud
     * @param regId Registration ID
     * @param callback Callback object
     */
    void installDevice(String regId, KiiItemCallback<Void> callback);

    /**
     * Uninstalls push device token
     * @param regId Registration ID
     * @param callback Callback object
     */
    void uninstallDevice(String regId, KiiItemCallback<Void> callback);

    interface UserCallback<T extends KiiUser> extends KiiItemCallback<T> { }
}
