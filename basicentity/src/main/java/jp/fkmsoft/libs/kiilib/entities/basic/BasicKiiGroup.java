package jp.fkmsoft.libs.kiilib.entities.basic;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Implementation
 */
public class BasicKiiGroup implements KiiGroup {
    private final String mId;
    private String mName;
    private KiiUser mOwner;

    public BasicKiiGroup(String id, String name, KiiUser owner) {
        mId = id;
        mName = name;
        mOwner = owner;
    }

    @Override
    public String getSubjectType() {
        return "GroupID";
    }

    @Override
    public String getResourcePath() {
        return "/groups/" + mId;
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_GROUP;
    }

    @Override
    public String getId() {
        return mId;
    }
}
