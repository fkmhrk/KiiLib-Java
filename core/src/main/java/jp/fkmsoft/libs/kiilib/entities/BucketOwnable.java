package jp.fkmsoft.libs.kiilib.entities;

/**
 * Implementted class can have bucket.
 */
public interface BucketOwnable {
    int TYPE_APP = 0;
    int TYPE_GROUP = 1;
    int TYPE_USER = 2;
    
    String getResourcePath();
    
    int getType();
}
