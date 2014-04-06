package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiBaseGroup
 */
public interface KiiGroupFactory<USER extends KiiBaseUser, GROUP extends KiiBaseGroup> {
    GROUP create(String id, String name, USER owner);
}
