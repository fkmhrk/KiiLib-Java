package jp.fkmsoft.libs.kiilib.test;

import jp.fkmsoft.libs.kiilib.apis.impl.KiiAppAPI;
import jp.fkmsoft.libs.kiilib.entities.EntityFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;

/**
 * Implementation for test
 */
public class TestKiiAppApi extends KiiAppAPI<KiiUser, KiiGroup, KiiBucket, KiiObject, KiiTopic> {

    private final MockHttpClient mClient = new MockHttpClient();

    public TestKiiAppApi(String appId, String appKey, String baseUrl) {
        super(appId, appKey, baseUrl);
    }

    @Override
    public KiiHTTPClient getHttpClient() {
        return mClient;
    }

    @Override
    protected EntityFactory<KiiUser, KiiGroup, KiiBucket, KiiObject, KiiTopic> getEntityFactory() {
        return new TestEntityFactory();
    }
}
