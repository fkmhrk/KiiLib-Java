package jp.fkmsoft.libs.kiilib.entities;

/**
 * Describes bucket entity in Kii Cloud
 */
public interface KiiBucket extends AccessControllable {
    /**
     * Gets the name of this bucket.
     * @return Bucket name
     */
    String getName();

    /**
     * Gets owner of this bucket.
     * @return Bucket owner
     */
    BucketOwnable getOwner();
}
