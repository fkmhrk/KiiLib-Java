package jp.fkmsoft.libs.kiilib.apis;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describes signup information
 */
public class SignupInfo {
    private static final String FIELD_USERNAME = "loginName";
    private static final String FIELD_EMAIL = "emailAddress";
    private static final String FIELD_PHONE = "phoneNumber";

    private final JSONObject mJson = new JSONObject();

    /**
     * Creates new instance for username + password
     * @param username Username
     * @return An instance
     */
    public static SignupInfo UserWithUsername(String username) {
        SignupInfo info = new SignupInfo();
        try {
            info.mJson.put(FIELD_USERNAME, username);
        } catch (JSONException e) {
            // nop
        }
        return info;
    }

    /**
     * Creates new instance for username + password
     * @param email Email address
     * @return An instance
     */
    public static SignupInfo UserWithEmail(String email) {
        SignupInfo info = new SignupInfo();
        try {
            info.mJson.put(FIELD_EMAIL, email);
        } catch (JSONException e) {
            // nop
        }
        return info;
    }

    /**
     * Creates new instance for phone + password
     * @param phone Phone number(+81xxxxyyyy)
     * @return An instance
     */
    public static SignupInfo UserWithPhone(String phone) {
        SignupInfo info = new SignupInfo();
        try {
            info.mJson.put(FIELD_PHONE, phone);
        } catch (JSONException e) {
            // nop
        }
        return info;
    }

    private SignupInfo() {
        
    }

    public JSONObject toJson() {
        return mJson;
    }
}
