package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiGroup
 */
public interface KiiGroupFactory<T extends KiiGroup, USER extends KiiUser> {
    T create(String id, String name, USER owner);
}
