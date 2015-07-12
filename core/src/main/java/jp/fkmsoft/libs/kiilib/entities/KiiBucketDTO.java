package jp.fkmsoft.libs.kiilib.entities;

/**
 * DTO for Kii Bucket.
 */
public interface KiiBucketDTO<T> {
    /**
     * Creates T by JSONObject.
     * @param owner Owner.
     * @param name Bucket name.
     * @return An instance of T
     */
    T fromJson(BucketOwnable owner, String name);
}
