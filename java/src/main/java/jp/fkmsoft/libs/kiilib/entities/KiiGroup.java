package jp.fkmsoft.libs.kiilib.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes group
 * @author fkm
 *
 */
public class KiiGroup<USER extends KiiUser> implements BucketOwnable, AccessControllable, ACLSubject {
    private final String id;
    private final String name;
    private final USER owner;
    private final List<USER> members;
    
    public KiiGroup(String id) {
        this(id, null, null);
    }
    
    public KiiGroup(String id, String name, USER owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<USER>();
    }

    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public USER getOwner() {
        return owner;
    }
    
    public List<USER> getMembers() {
        return members;
    }
    
    @Override
    public String getResourcePath() {
        return "/groups/" + id;
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_GROUP;
    }
    
    @Override
    public String getSubjectType() {
        return "GroupID";
    }
}
