package jp.fkmsoft.libs.kiilib.entities.basic;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;

/**
 * Implementation
 */
public class BasicKiiBucket implements KiiBucket {
    private static final BucketOwnable APP_SCOPE = new BasicKiiApp();
    private final BucketOwnable mOwner;
    private String mName;

    public BasicKiiBucket(BucketOwnable owner, String name) {
        if (owner == null) {
            owner = APP_SCOPE;
        }
        mOwner = owner;
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public BucketOwnable getOwner() {
        return mOwner;
    }

    @Override
    public String getResourcePath() {
        return mOwner.getResourcePath() + "/buckets/" + mName;
    }
}
