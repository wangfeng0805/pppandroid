package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * �ҵĺ���
 * Created by Administrator on 2017/7/5.
 */

public class FriendsPageInfo implements java.io.Serializable{
    private static final long serialVersionUID = -8159007845214039125L;

    private String count_extension_user;//�ܼƺ��ѣ�ֱ�Ӻ���+��Ӻ��ѣ�
    private String one_level_users_count;//һ�����ѵ�����
    private String second_level_users_count;//�������ѵ�����
    private String extension_user_list;//�ƹ���ѵ���Ϣ�б�
    private List<FriendInfo> friendList;

    public String getCount_extension_user() {
        return count_extension_user;
    }

    public void setCount_extension_user(String count_extension_user) {
        this.count_extension_user = count_extension_user;
    }

    public String getOne_level_users_count() {
        return one_level_users_count;
    }

    public void setOne_level_users_count(String one_level_users_count) {
        this.one_level_users_count = one_level_users_count;
    }

    public String getSecond_level_users_count() {
        return second_level_users_count;
    }

    public void setSecond_level_users_count(String second_level_users_count) {
        this.second_level_users_count = second_level_users_count;
    }

    public String getExtension_user_list() {
        return extension_user_list;
    }

    public void setExtension_user_list(String extension_user_list) {
        this.extension_user_list = extension_user_list;
    }

    public List<FriendInfo> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendInfo> friendList) {
        this.friendList = friendList;
    }
}
