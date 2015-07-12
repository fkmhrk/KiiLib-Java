package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiDTO;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides Kii API
 */
public interface AppAPI {

    /**
     * Logs in Kii Cloud as App Admin
     * @param clientId Client ID
     * @param clientSecret Client Secret
     * @param dto DTO for creating KiiUser
     * @param callback Callback object
     */
    <T extends KiiUser> void loginAsAdmin(String clientId, String clientSecret, KiiDTO<T> dto, LoginCallback<T> callback);

    /**
     * Logs in Kii Cloud as App user
     * @param identifier Username or email or phone
     * @param password Password
     * @param dto DTO for creating KiiUser
     * @param callback Callback object
     */
    <T extends KiiUser> void loginAsUser(String identifier, String password, KiiDTO<T> dto, LoginCallback<T> callback);

    /**
     * Signs up Kii Cloud
     * @param info User info to be created.
     * @param password User password
     * @param dto DTO for creating KiiUser
     * @param callback Callback object
     */
    <T extends KiiUser> void signup(SignupInfo info, String password, KiiDTO<T> dto, SignupCallback<T> callback);

    interface LoginCallback<T extends KiiUser> extends KiiCallback {
        void onSuccess(String token, T user);
    }

    interface SignupCallback<T extends KiiUser> extends KiiItemCallback<T> { }
}
