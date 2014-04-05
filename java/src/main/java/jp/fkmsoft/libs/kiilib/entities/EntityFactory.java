package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory interface
 */
public interface EntityFactory<
        USER extends KiiUser,
        OBJECT extends KiiObject> {
    KiiUserFactory<USER> getKiiUserFactory();

    KiiObjectFactory<OBJECT> getKiiObjectFactory();
}
