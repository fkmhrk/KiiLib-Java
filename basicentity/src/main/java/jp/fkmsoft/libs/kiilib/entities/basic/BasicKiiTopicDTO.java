package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicDTO;

/**
 * DTO for {@link BasicKiiTopic}
 */
public class BasicKiiTopicDTO implements KiiTopicDTO<BasicKiiTopic> {

    private static BasicKiiTopicDTO INSTANCE = new BasicKiiTopicDTO();
    private BasicKiiTopicDTO() {
        // singleton
    }

    public static BasicKiiTopicDTO getInstance() {
        return INSTANCE;
    }

    @Override
    public BasicKiiTopic fromJson(BucketOwnable owner, String name) {
        return new BasicKiiTopic(owner, name);
    }
}
