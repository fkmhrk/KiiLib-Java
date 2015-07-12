package jp.fkmsoft.libs.kiilib.entities.basic;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiBucketDTO;

/**
 * DTO for {@link BasicKiiBucket}
 */
public class BasicKiiBucketDTO implements KiiBucketDTO<BasicKiiBucket> {

    private static BasicKiiBucketDTO INSTANCE = new BasicKiiBucketDTO();
    private BasicKiiBucketDTO() {
        // singleton
    }

    public static BasicKiiBucketDTO getInstance() {
        return INSTANCE;
    }

    @Override
    public BasicKiiBucket fromJson(BucketOwnable owner, String name) {
        return new BasicKiiBucket(owner, name);
    }
}
