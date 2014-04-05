package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiUser
 */
public interface KiiUserFactory<T extends KiiUser> {
    T create(String id);
}
