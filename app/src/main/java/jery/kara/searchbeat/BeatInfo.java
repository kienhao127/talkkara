package jery.kara.searchbeat;

/**
 * Created by CPU11296-local on 1/4/2018.
 */

public class BeatInfo {

    public static final int TYPE_BEAT = 0;
    public static final int TYPE_RECENT = 1;
    public static final int TYPE_RESULT = 2;

    public int id;
    public String title;
    public String author;
    public String beatDownloadLink;
    public String beatLocalPath;
    public String lyricDownloadLink;
    public String lyricLocalPath;

    public int type;
}
