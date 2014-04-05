package jp.fkmsoft.libs.kiilib.entities;

/**
 * Describes bucket
 * @author fkm
 *
 */
public class KiiBucket implements AccessControllable {

    private final BucketOwnable owner;
    private final String name;
    
    private static BucketOwnable APP_SCOPE = new KiiApp();
    
    public KiiBucket(BucketOwnable owner, String name) {
        if (owner == null) {
            owner = APP_SCOPE;
        }
        this.owner = owner;
        this.name = name;
    }
    
    public String getResourcePath() {
        return owner.getResourcePath() + "/buckets/" + name;
    }

    public String getName() {
        return name;
    }
    
}
