package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiGroup
 */
public interface KiiGroupFactory<USER extends KiiUser, GROUP extends KiiGroup> {
    GROUP create(String id, String name, USER owner);
}
