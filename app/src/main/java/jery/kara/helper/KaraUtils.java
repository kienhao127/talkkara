package jery.kara.helper;

import android.os.Environment;

/**
 * Created by CPU11341-local on 30-May-18.
 */

public class KaraUtils {
    public static String getFolderPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/TalkTV";
    }
}
