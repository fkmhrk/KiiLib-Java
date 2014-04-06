package jp.fkmsoft.libs.kiilib.test;

import org.json.JSONException;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.EntityFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiGroupFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.KiiUserFactory;

/**
 * Factory for Test
 */
public class TestEntityFactory implements EntityFactory<KiiUser, KiiGroup<KiiUser>, KiiBucket, KiiObject<KiiBucket>, KiiTopic> {
    @Override
    public KiiUserFactory<KiiUser> getKiiUserFactory() {
        return new KiiUserFactory<KiiUser>() {
            @Override
            public KiiUser create(String id) {
                return new KiiUser(id);
            }
        };
    }

    @Override
    public KiiGroupFactory<KiiUser, KiiGroup<KiiUser>> getKiiGroupFactory() {
        return new KiiGroupFactory<KiiUser, KiiGroup<KiiUser>>() {
            @Override
            public KiiGroup<KiiUser> create(String id, String name, KiiUser owner) {
                return new KiiGroup<KiiUser>(id, name, owner);
            }
        };
    }

    @Override
    public KiiObjectFactory<KiiBucket, KiiObject<KiiBucket>> getKiiObjectFactory() {
        return new KiiObjectFactory<KiiBucket, KiiObject<KiiBucket>>() {
            @Override
            public KiiObject<KiiBucket> create(KiiBucket bucket, JSONObject body) {
                try {
                    return new KiiObject<KiiBucket>(bucket, body);
                } catch (JSONException e) {
                    return null;
                }
            }
        };
    }

    @Override
    public KiiTopicFactory<KiiTopic> getKiitopicFactory() {
        return new KiiTopicFactory<KiiTopic>() {
            @Override
            public KiiTopic create(BucketOwnable owner, String name) {
                return new KiiTopic(owner, name);
            }
        };
    }
}
