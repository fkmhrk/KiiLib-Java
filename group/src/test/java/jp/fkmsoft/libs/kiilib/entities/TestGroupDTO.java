package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * Test Group DTO
 */
public class TestGroupDTO implements KiiDTO<TestKiiGroup> {
    @Override
    public TestKiiGroup fromJson(JSONObject jsonObject) {
        String groupID = jsonObject.optString("groupID", "");
        return new TestKiiGroup(groupID);
    }

    @Override
    public JSONObject toJson(TestKiiGroup testKiiGroup) {
        return null;
    }
}
