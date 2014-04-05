package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory interface
 */
public interface EntityFactory<USER extends KiiUser> {
    KiiUserFactory<USER> getKiiUserFactory();
}
