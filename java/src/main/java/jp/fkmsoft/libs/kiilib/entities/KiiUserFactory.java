package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiBaseUser
 */
public interface KiiUserFactory<T extends KiiBaseUser> {
    T create(String id);
}
