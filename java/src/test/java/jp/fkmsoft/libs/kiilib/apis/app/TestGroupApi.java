package jp.fkmsoft.libs.kiilib.apis.app;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.test.MockHttpClient;
import jp.fkmsoft.libs.kiilib.test.TestKiiAppApi;

/**
 * Test case for GroupApi
 */
public class TestGroupApi {

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
    public void test_0000_getOwnedGroup() {
        mClient.addToSendJson(200, "{\"groups\":[" +
                "{\"groupID\":\"group1234\",\"name\":\"testGroup\",\"owner\":\"user1234\"}" +
                "]}", "");

        KiiUser user = new KiiUser("me");

        mAppApi.groupAPI().getOwnedGroup(user, new GroupAPI.ListCallback<KiiGroup>() {
            @Override
            public void onSuccess(List<KiiGroup> result) {
                Assert.assertNotNull(result);
                Assert.assertEquals(1, result.size());

                KiiGroup kiiGroup = result.get(0);
                Assert.assertEquals("group1234", kiiGroup.getId());
                Assert.assertEquals("testGroup", kiiGroup.getName());
                Assert.assertEquals("user1234", kiiGroup.getOwner().getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());

            }
        });
    }

    @Test
    public void test_0100_getJoinedGroup() {
        mClient.addToSendJson(200, "{\"groups\":[" +
                "{\"groupID\":\"group1234\",\"name\":\"testJoinedGroup\",\"owner\":\"user1234\"}" +
                "]}", "");

        KiiUser user = new KiiUser("me");

        mAppApi.groupAPI().getJoinedGroup(user, new GroupAPI.ListCallback<KiiGroup>() {
            @Override
            public void onSuccess(List<KiiGroup> result) {
                Assert.assertNotNull(result);
                Assert.assertEquals(1, result.size());

                KiiGroup kiiGroup = result.get(0);
                Assert.assertEquals("group1234", kiiGroup.getId());
                Assert.assertEquals("testJoinedGroup", kiiGroup.getName());
                Assert.assertEquals("user1234", kiiGroup.getOwner().getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());

            }
        });
    }

    @Test
    public void test_0200_create() {
        mClient.addToSendJson(201, "{\"groupID\":\"group1234\"}", "");

        String groupName = "testCreated";
        KiiUser user = new KiiUser("me");

        mAppApi.groupAPI().create(groupName, user, null, new GroupAPI.GroupCallback<KiiGroup>() {
            @Override
            public void onSuccess(KiiGroup group) {
                Assert.assertEquals("group1234", group.getId());
                Assert.assertEquals("testCreated", group.getName());
                Assert.assertEquals("me", group.getOwner().getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }


    @Test
    public void test_0300_getMembers() {
        mClient.addToSendJson(200, "{\"members\":[" +
                "{\"userID\":\"user1234\"}" +
                "]}", "");

        KiiGroup group = new KiiGroup("group2345");

        mAppApi.groupAPI().getMembers(group, new GroupAPI.MemberCallback<KiiUser>() {
            @Override
            public void onSuccess(List<KiiUser> memberList) {
                Assert.assertNotNull(memberList);
                Assert.assertEquals(1, memberList.size());

                KiiUser user = memberList.get(0);
                Assert.assertEquals("user1234", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0400_addMember() {
        mClient.addToSendJson(200, "{}", "");

        KiiGroup group = new KiiGroup("group2345");

        KiiUser newUser = new KiiUser("user2345");
        mAppApi.groupAPI().addMember(group, newUser, new GroupAPI.GroupCallback<KiiGroup>() {
            @Override
            public void onSuccess(KiiGroup group) {
                Assert.assertNotNull(group);

                List<KiiUser> members = group.getMembers();
                Assert.assertNotNull(members);
                Assert.assertEquals(1, members.size());

                KiiUser user = members.get(0);
                Assert.assertEquals("user2345", user.getId());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0500_removeMember() {
        mClient.addToSendJson(204, null, "");

        KiiGroup group = new KiiGroup("group2345");

        KiiUser user = new KiiUser("user2345");
        group.getMembers().add(user);
        mAppApi.groupAPI().removeMember(group, user, new GroupAPI.GroupCallback<KiiGroup>() {
            @Override
            public void onSuccess(KiiGroup group) {
                Assert.assertNotNull(group);
                List<KiiUser> members = group.getMembers();
                Assert.assertNotNull(members);
                Assert.assertEquals(0, members.size());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void test_0600_changeName() {
        mClient.addToSendPlain(200, "{}", "");

        KiiGroup group = new KiiGroup("group2345");
        String newName = "newGroupName";

        mAppApi.groupAPI().changeName(group, newName, new GroupAPI.GroupCallback<KiiGroup>() {
            @Override
            public void onSuccess(KiiGroup group) {
                Assert.assertNotNull(group);
                Assert.assertEquals("newGroupName", group.getName());
            }

            @Override
            public void onError(Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
