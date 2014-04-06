package jp.fkmsoft.libs.kiilib.entities;

/**
 * Just for name
 */
public class KiiGroup extends KiiBaseGroup<KiiUser> {
    public KiiGroup(String id) {
        super(id);
    }

    public KiiGroup(String id, String name, KiiUser owner) {
        super(id, name, owner);
    }
}
