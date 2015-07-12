package jp.fkmsoft.libs.kiilib.apis;

import org.json.JSONObject;

/**
 * Exception of Kii Cloud
 */
public class KiiException extends Exception {

    private final int mStatus;
    private final JSONObject mBody;

    public KiiException(int status, JSONObject body) {
        super();
        this.mStatus = status;
        this.mBody = body;
    }

    public KiiException(int status, Throwable cause) {
        super(cause);
        this.mStatus = status;
        this.mBody = null;
    }

    /**
     * Gets the HTTP status code
     * @return HTTP status code
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * Gets the body of response
     * @return response
     */
    public JSONObject getBody() {
        return mBody;
    }
}
