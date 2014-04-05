package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory interface
 */
public interface EntityFactory<
        USER extends KiiUser,
        GROUP extends KiiGroup<USER>,
        BUCKET extends KiiBucket,
        OBJECT extends KiiObject<BUCKET>,
        TOPIC extends KiiTopic
        > {
    KiiUserFactory<USER> getKiiUserFactory();

    KiiGroupFactory<USER, GROUP> getKiiGroupFactory();

    KiiObjectFactory<BUCKET, OBJECT> getKiiObjectFactory();

    KiiTopicFactory<TOPIC> getKiitopicFactory();
}
