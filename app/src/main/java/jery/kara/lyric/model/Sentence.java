package jery.kara.lyric.model;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class Sentence {
    private String mContent;
    private long mFromTime;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getFromTime() {
        return mFromTime;
    }

    public void setFromTime(long fromTime) {
        mFromTime = fromTime;
    }

    public Sentence(String content, long fromTime) {
        mContent = content;
        mFromTime = fromTime;
    }
}
