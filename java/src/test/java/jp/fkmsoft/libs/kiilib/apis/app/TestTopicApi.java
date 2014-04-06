package jp.fkmsoft.libs.kiilib.apis.app;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicMessage;
import jp.fkmsoft.libs.kiilib.test.MockHttpClient;
import jp.fkmsoft.libs.kiilib.test.TestKiiAppApi;

/**
 * Test case for TopicApi
 */
public class TestTopicApi {

    private static final String APP_ID = "appId";
    private static final String APP_KEY = "appKey";
    private static final String BASE_URL = "https://api-jp.kii.com/api";

    private TestKiiAppApi mAppApi;
    private MockHttpClient mClient;

    @Before
    public void setUp() {
        mAppApi = new TestKiiAppApi(APP_ID, APP_KEY, BASE_URL);
        mClient = (MockHttpClient) mAppApi.getHttpClient();
    }

    @Test
    public void test_0000_create() {
        mClient.addToSendJson(204, null, "");

        KiiGroup group = new KiiGroup("group1234");
        String name = "topicName";

        mAppApi.topicAPI().create(group, name, new TopicAPI.TopicCallback<KiiTopic>() {
            @Override
            public void onSuccess(KiiTopic topic) {
                Assert.assertNotNull(topic);
                Assert.assertEquals("topicName", topic.getName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0100_subscribe() {
        mClient.addToSendJson(204, null, "");

        KiiGroup group = new KiiGroup("group1234");
        String name = "topicName";
        KiiTopic topic = new KiiTopic(group, name);

        mAppApi.topicAPI().subscribe(topic, new TopicAPI.TopicCallback<KiiTopic>() {
            @Override
            public void onSuccess(KiiTopic topic) {
                Assert.assertNotNull(topic);
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0200_sendMessage() {
        mClient.addToSendJson(200, "{\"pushMessageID\":\"message1234\"}", "");

        KiiGroup group = new KiiGroup("group1234");
        String name = "topicName";
        KiiTopic topic = new KiiTopic(group, name);

        JSONObject pushData = new JSONObject();
        KiiTopicMessage message = new KiiTopicMessage(pushData, "type01");

        mAppApi.topicAPI().sendMessage(topic, message, new TopicAPI.SendMessageCallback() {
            @Override
            public void onSuccess(String pushMessageId) {
                Assert.assertEquals("message1234", pushMessageId);
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0300_getList() {
        mClient.addToSendJson(200, "{\"topics\":[" +
                "{\"topicID\":\"topic01\"}" +
                "]}", "");

        KiiGroup group = new KiiGroup("group1234");

        mAppApi.topicAPI().getList(group, new TopicAPI.TopicListCallback<KiiTopic>() {
            @Override
            public void onSuccess(List<KiiTopic> list) {
                Assert.assertNotNull(list);
                Assert.assertEquals(1, list.size());

                KiiTopic topic = list.get(0);
                Assert.assertEquals("topic01", topic.getName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
