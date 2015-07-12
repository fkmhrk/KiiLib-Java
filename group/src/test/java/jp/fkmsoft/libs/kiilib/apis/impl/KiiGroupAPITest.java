package jp.fkmsoft.libs.kiilib.apis.impl;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.SignupInfo;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiGroup;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiGroupDTO;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.entities.test.TestKiiContext;

/**
 * Testcase
 */
public class KiiGroupAPITest {
    private static final String APP_ID = "bd2268ad";
    private static final String APP_KEY = "a5f02a87216dfc06e16ecf035a83d8bf";

    @Test
    public void test_0000_create_addMember_getMembers_removeMember_delete() throws Exception {
        KiiContext context = new TestKiiContext(APP_ID, APP_KEY, KiiContext.SITE_JP);
        GroupAPI api = new KiiGroupAPI(context);

        KiiUser currentUser = login(context);

        String username2 = "fkmtest2";
        KiiUser user2 = signup(context, username2);

        KiiGroup group = createGroup(api, currentUser);

        addMember(api, group, user2);

        getMembers(api, group, 2);

        removeMember(api, group, user2);

        getMembers(api, group, 1);

        deleteGroup(api, group);
    }

    private KiiUser signup(final KiiContext context, final String username) {
        AppAPI api = new KiiAppAPI(context);

        SignupInfo info = SignupInfo.UserWithUsername(username);
        final List<KiiUser> resultList = new ArrayList<KiiUser>();
        api.signup(info, "123456", BasicKiiUserDTO.getInstance(), new AppAPI.SignupCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser testKiiUser) {
                resultList.add(testKiiUser);
            }

            @Override
            public void onError(KiiException e) {
                if (e.getStatus() != 409) {
                    Assert.fail("Failed to create user status=" + e.getStatus());
                }
                UserAPI userAPI = new KiiUserAPI(context);
                userAPI.findUserByUsername(username, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
                    @Override
                    public void onSuccess(BasicKiiUser testKiiUser) {
                        resultList.add(testKiiUser);
                    }

                    @Override
                    public void onError(KiiException e) {
                        Assert.fail("Failed to find user status=" + e.getStatus());
                    }
                });
            }
        });

        return resultList.get(0);
    }

    private KiiUser login(KiiContext context) throws Exception {
        AppAPI api = new KiiAppAPI(context);

        String username = "fkmtest";
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

    private KiiGroup createGroup(GroupAPI api, KiiUser currentUser) {
        String groupName = "group1111";
        final List<KiiGroup> resultList = new ArrayList<KiiGroup>();
        api.create(groupName, currentUser, null, BasicKiiGroupDTO.getInstance(), new GroupAPI.GroupCallback<BasicKiiGroup>() {
            @Override
            public void onSuccess(BasicKiiGroup g) {
                resultList.add(g);
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("create group error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
        return resultList.get(0);
    }

    private void addMember(GroupAPI api, KiiGroup group, KiiUser user) {
        api.addMember(group, user, new GroupAPI.GroupCallback<KiiGroup>() {
            @Override
            public void onSuccess(KiiGroup kiiGroup) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Add member error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void getMembers(GroupAPI api, KiiGroup group, final int expectedCount) {
        api.getMembers(group, BasicKiiUserDTO.getInstance(), new GroupAPI.MemberCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(List<BasicKiiUser> testKiiUsers) {
                Assert.assertEquals(expectedCount, testKiiUsers.size());
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Get members error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void removeMember(GroupAPI api, KiiGroup group, KiiUser user) {
        api.removeMember(group, user, new GroupAPI.GroupCallback<KiiGroup>() {
            @Override
            public void onSuccess(KiiGroup kiiGroup) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Remove member error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }

    private void deleteGroup(GroupAPI api, KiiGroup group) {
        api.delete(group, new KiiItemCallback<Void>() {
            @Override
            public void onSuccess(Void v) {
                // OK
            }

            @Override
            public void onError(KiiException e) {
                Assert.fail("Delete group error status=" + e.getStatus() + " / " + e.getBody());
            }
        });
    }
}