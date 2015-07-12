package jp.fkmsoft.libs.kiilib.entities;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;

/**
 * Describes context in Kii Clound
 */
public interface KiiContext {
    /**
     * Gets App ID
     * @return App ID
     */
    String getAppId();

    /**
     * Gets App key
     * @return App key
     */
    String getAppKey();

    /**
     * Gets Base URL
     * @return Base URL
     */
    String getBaseUrl();

    /**
     * Gets HTTP client
     * @return HTTP client
     */
    KiiHTTPClient getHttpClient();
}
