package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory interface
 */
public interface EntityFactory<
        USER extends KiiBaseUser,
        GROUP extends KiiBaseGroup<USER>,
        BUCKET extends KiiBaseBucket,
        OBJECT extends KiiBaseObject<BUCKET>,
        TOPIC extends KiiBaseTopic
        > {
    KiiUserFactory<USER> getKiiUserFactory();

    KiiGroupFactory<USER, GROUP> getKiiGroupFactory();

    KiiObjectFactory<BUCKET, OBJECT> getKiiObjectFactory();

    KiiTopicFactory<TOPIC> getKiitopicFactory();
}
