package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory interface
 */
public interface EntityFactory<
        USER extends KiiUser,
        GROUP extends KiiGroup<USER>,
        OBJECT extends KiiObject> {
    KiiUserFactory<USER> getKiiUserFactory();

    KiiGroupFactory<GROUP, USER> getKiiGroupFactory();

    KiiObjectFactory<OBJECT> getKiiObjectFactory();
}
