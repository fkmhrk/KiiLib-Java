package jp.fkmsoft.libs.kiilib.entities;

/**
 * Describes implemented class can own buckets
 * @author fkm
 *
 */
public interface BucketOwnable {
    public static final int TYPE_APP = 0;
    public static final int TYPE_GROUP = 1;
    public static final int TYPE_USER = 2;
    
    String getResourcePath();
    
    int getType();
    
}
