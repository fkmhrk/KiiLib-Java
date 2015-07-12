package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * DTO for test user
 */
public class TestUserDTO implements KiiDTO<TestKiiUser> {
    @Override
    public TestKiiUser fromJson(JSONObject jsonObject) {
        return new TestKiiUser(jsonObject);
    }

    @Override
    public JSONObject toJson(TestKiiUser testKiiUser) {
        return null;
    }
}
