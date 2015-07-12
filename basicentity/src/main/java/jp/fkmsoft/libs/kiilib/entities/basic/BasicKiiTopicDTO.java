package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicDTO;

/**
 * DTO for {@link BasicKiiTopic}
 */
public class BasicKiiTopicDTO implements KiiTopicDTO<BasicKiiTopic> {
    @Override
    public BasicKiiTopic fromJson(BucketOwnable bucketOwnable, JSONObject jsonObject) {
        // is this method really called?
        return null;
    }

    @Override
    public JSONObject toJson(BasicKiiTopic basicKiiTopic) {
        return null;
    }
}
