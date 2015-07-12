package jp.fkmsoft.libs.kiilib.apis.impl;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiTopic;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiTopicDTO;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.entities.test.TestKiiContext;

/**
 * Testcase
 */
public class KiiTopicAPITest {

    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";
    private static final String TOPIC_NAME = "testTopic";

    @Test
    public void test_0000_create_subscribe_unsubscribe() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        TopicAPI api = new KiiTopicAPI(context);

        KiiUser user = login(context, "fkmtest");
        KiiTopic topic = createTopic(api, user);

        getList(api, user);

        subscribe(api, topic);

        unsubscribe(api, topic, user);
    }

    private KiiUser login(KiiContext context, String username) throws Exception {
        AppAPI api = new KiiAppAPI(context);

        String password = "123456";
        final List<KiiUser> resultList = new ArrayList<KiiUser>();
        api.loginAsUser(username, password, BasicKiiUserDTO.getInstance(), new AppAPI.LoginCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(String token, BasicKiiUser testKiiUser) {
                resultList.add(testKiiUser);
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("login error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
        return resultList.get(0);
    }

    private KiiTopic createTopic(TopicAPI api, final KiiUser user) {
        final List<BasicKiiTopic> resultList = new ArrayList<BasicKiiTopic>();
        api.create(user, TOPIC_NAME, BasicKiiTopicDTO.getInstance(), new TopicAPI.TopicCallback<BasicKiiTopic>() {
            @Override
            public void onSuccess(BasicKiiTopic topic) {
                resultList.add(topic);
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 409) {
                    Assert.fail("create topic error status=" + e.getStatus() + " / " + e.getBody());
                }
                resultList.add(new BasicKiiTopic(user, TOPIC_NAME));
            }
        });
        return resultList.get(0);
    }

    private void getList(TopicAPI api, final KiiUser user) {
        api.getList(user, BasicKiiTopicDTO.getInstance(), new TopicAPI.TopicListCallback<BasicKiiTopic>() {
            @Override
            public void onSuccess(List<BasicKiiTopic> topicList) {
                Assert.assertEquals(1, topicList.size());
                BasicKiiTopic topic = topicList.get(0);
                Assert.assertEquals(user.getResourcePath() + "/topics/" + TOPIC_NAME, topic.getResourcePath());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("get topic list error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void subscribe(TopicAPI api, KiiTopic topic) {
        api.subscribe(topic, new TopicAPI.TopicCallback<KiiTopic>() {
            @Override
            public void onSuccess(KiiTopic kiiTopic) {
                System.out.println("Subscribed");
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Subscribe topic error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void unsubscribe(TopicAPI api, KiiTopic topic, KiiUser user) {
        api.unsubscribe(topic, user.getId(), new TopicAPI.TopicCallback<KiiTopic>() {
            @Override
            public void onSuccess(KiiTopic kiiTopic) {
                System.out.println("Unsubscribed");
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Unsubscribe topic error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }
}