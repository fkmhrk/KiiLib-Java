package jp.fkmsoft.libs.kiilib.entities.test;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;

/**
 * Test Kii bucket
 */
public class TestKiiBucket implements KiiBucket {
    private BucketOwnable mOwner;
    private String mName;

    public TestKiiBucket(BucketOwnable owner, String name) {
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
