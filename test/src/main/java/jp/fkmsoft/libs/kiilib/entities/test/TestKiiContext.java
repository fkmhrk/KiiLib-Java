package jp.fkmsoft.libs.kiilib.entities.test;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;

/**
 * Test Kii Context
 */
public class TestKiiContext implements KiiContext {
    private final String mAppId;
    private final String mAppKey;
    private final String mBaseURL;
    private String mAccessToken;
    private final KiiHTTPClient mClient;

    public TestKiiContext(String appId, String appKey, String baseURL) {
        mAppId = appId;
        mAppKey = appKey;
        mBaseURL = baseURL;
        mClient = new TestHTTPClient(this);
    }

    @Override
    public String getAppId() {
        return mAppId;
    }

    @Override
    public String getAppKey() {
        return mAppKey;
    }

    @Override
    public String getBaseUrl() {
        return mBaseURL;
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public void setAccessToken(String token) {
        mAccessToken = token;
    }

    @Override
    public KiiHTTPClient getHttpClient() {
        return mClient;
    }
}
