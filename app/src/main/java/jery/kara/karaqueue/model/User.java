package jery.kara.karaqueue.model;

import jery.kara.searchbeat.BeatInfo;

/**
 * Created by CPU11341-local on 09-May-18.
 */

public class User {
    public static final int TYPE_BANNED = 0;
    public static final int TYPE_VIWER = 1;
    public static final int TYPE_SINGER = 2;
    public static final int TYPE_WAITTING = 3;

    public static final int ROLE_MANAGER = 1;
    public static final int ROLE_USER = 2;

    public User(){

    }

    public int type;
    public int role;
    public int id;
    public String name;
    public String avatarURL;
    public BeatInfo beatInfo = new BeatInfo();
}
