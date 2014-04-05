package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory interface
 */
public interface EntityFactory<
        USER extends KiiUser,
        GROUP extends KiiGroup<USER>,
        BUCKET extends KiiBucket,
        OBJECT extends KiiObject<BUCKET>> {
    KiiUserFactory<USER> getKiiUserFactory();

    KiiGroupFactory<USER, GROUP> getKiiGroupFactory();

    KiiObjectFactory<BUCKET, OBJECT> getKiiObjectFactory();
}
