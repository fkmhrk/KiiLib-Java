package jp.fkmsoft.libs.kiilib.entities;

/**
 * Test Kii group
 */
public class TestKiiGroup implements KiiGroup {
    private final String mId;

    public TestKiiGroup(String id) {
        mId = id;
    }

    @Override
    public String getSubjectType() {
        return null;
    }

    @Override
    public String getResourcePath() {
        return "/groups/" + mId;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getId() {
        return mId;
    }
}
