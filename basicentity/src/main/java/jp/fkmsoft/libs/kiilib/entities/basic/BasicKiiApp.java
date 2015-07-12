package jp.fkmsoft.libs.kiilib.entities.basic;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiApp;

/**
 * Implementation
 */
public class BasicKiiApp implements KiiApp {
    @Override
    public String getResourcePath() {
        return "";
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_APP;
    }
}
