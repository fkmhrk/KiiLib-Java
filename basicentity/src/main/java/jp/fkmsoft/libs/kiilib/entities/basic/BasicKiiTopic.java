package jp.fkmsoft.libs.kiilib.entities.basic;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;

/**
 * Implementation
 */
public class BasicKiiTopic implements KiiTopic {
    private static final BucketOwnable APP_SCOPE = new BasicKiiApp();
    private final BucketOwnable mOwner;
    private final String mName;

    public BasicKiiTopic(BucketOwnable owner, String name) {
        if (owner == null) {
            owner = APP_SCOPE;
        }
        mOwner = owner;
        mName = name;
    }

    @Override
    public String getResourcePath() {
        return mOwner.getResourcePath() + "/topics/" + mName;
    }
}
