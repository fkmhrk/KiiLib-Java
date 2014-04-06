package jp.fkmsoft.libs.kiilib.test;

import org.json.JSONObject;

/**
 * Mock Response
 */
public class MockResponse {
    public final int mStatus;
    public final JSONObject mBody;
    public final String mEtag;

    public MockResponse(int mStatus, JSONObject mBody, String mEtag) {
        this.mStatus = mStatus;
        this.mBody = mBody;
        this.mEtag = mEtag;
    }
}
