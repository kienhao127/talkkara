package jery.kara.lyric.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class Lyric {
    private long mLength;
    private ArrayList<Sentence> mArrSentences = new ArrayList<>();

    public void addSentence(String content, long time) {
        mArrSentences.add(new Sentence(content, time));
    }

    public Lyric() {
    }

    public long getLength() {
        return mLength;
    }

    public void setLength(long length) {
        mLength = length;
    }

    public ArrayList<Sentence> getArrSentences() {
        return mArrSentences;
    }

    public void setArrSentences(ArrayList<Sentence> arrSentences) {
        mArrSentences = arrSentences;
    }

    public static class SentenceComparator implements Comparator<Sentence> {
        @Override
        public int compare(Sentence sent1, Sentence sent2) {
            return (int) (sent1.getFromTime() - sent2.getFromTime());
        }
    }
}
