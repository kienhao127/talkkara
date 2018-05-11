package jery.kara.lyric.utils;

import android.text.TextUtils;
import android.util.Log;

import jery.kara.lyric.model.Lyric;
import jery.kara.lyric.model.Sentence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class LyricUtils {

    public static Lyric parseLyric(InputStream inputStream, String Encoding) {
        Lyric lyric = new Lyric();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Encoding));
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line, lyric);
            }
            Collections.sort(lyric.getArrSentences(), new Lyric.SentenceComparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyric;
    }

    public static Lyric parseLyric(File file, String Encoding) {
        Lyric lyric = new Lyric();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Encoding));
            Log.i(TAG, String.format("parseLyric(%s, %s)", file.getPath(), Encoding));
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line, lyric);
            }
            Collections.sort(lyric.getArrSentences(), new Lyric.SentenceComparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyric;
    }

    public static Lyric parseLyric(String fullLyric, String Encoding) {
        Lyric lyric = new Lyric();
        String[] lyricArr;
        lyricArr = fullLyric.split("\n");

        for (String line: lyricArr){
            parseLine(line, lyric);
        }
        Collections.sort(lyric.getArrSentences(), new Lyric.SentenceComparator());
        return lyric;
    }

    public static boolean parseLine(String line, Lyric lyric){
        if (line == null || line.length() <= 0){
            return false;
        }

        int range = line.indexOf(']');
        if (range > 0){
            String strTime = line.substring(1, range);
            String content = line.substring(range + 1);
            if (strTime != null && strTime.length() == 8 && content != null && content.length() > 0){
                long time = parseTime(strTime);
                lyric.addSentence(content, time);
                Log.d("Line: ", String.valueOf(strTime) + ": " + content);
            }
        }
        return true;
    }

    public static long parseTime(String time){
        String[] ss = time.split("\\:|\\.");
        if (ss.length < 2) {
            return -1;
        }

        if (ss.length == 2){
            int min = Integer.parseInt(ss[0]);
            int sec = Integer.parseInt(ss[1]);
            if (min < 0 || sec < 0 || sec >= 60) {
                throw new RuntimeException("Chữ số không hợp lệ!");
            }
            // System.out.println("time" + (min * 60 + sec) * 1000L);
            return (min * 60 + sec) * 1000L;
        }

        if (ss.length == 3){
            int min = Integer.parseInt(ss[0]);
            int sec = Integer.parseInt(ss[1]);
            int mm = Integer.parseInt(ss[2]);
            if (min < 0 || sec < 0 || sec >= 60 || mm < 0 || mm > 99) {
                throw new RuntimeException("Chữ số không hợp lệ!");
            }
            // System.out.println("time" + (min * 60 + sec) * 1000L + mm * 10);
            return (min * 60 + sec) * 1000L + mm * 10;
        }

        return -1;
    }
}
