package jp.fkmsoft.libs.kiilib.entities.test;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectDTO;

/**
 * DTO for Kii Object
 */
public class TestObjectDTO implements KiiObjectDTO<TestKiiObject> {
    @Override
    public TestKiiObject fromJson(KiiBucket kiiBucket, JSONObject jsonObject) {
        return new TestKiiObject(kiiBucket, jsonObject);
    }

    @Override
    public JSONObject toJson(TestKiiObject testKiiObject) {
        return null;
    }
}
