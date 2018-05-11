//package jery.kara.searchbeat.helper;
//
//import android.content.Context;
//import android.net.ParseException;
//
//import java.util.ArrayList;
//
//import jery.kara.searchbeat.BeatInfo;
//
///**
// * Created by CPU11341-local on 17-Apr-18.
// */
//
//public class DataManager {
//    private static DataManager instance = null;
//
//    protected DataManager() throws ParseException {
//    }
//
//    public static DataManager getInstance() {
//        if (instance == null) {
//            try {
//                instance = new DataManager();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return instance;
//    }
//
//    public void insertBeatInfo(BeatInfo beatInfo, Context context){
//        DataHelper dataHelper = new DataHelper(context);
//        dataHelper.insertBeatInfo(beatInfo);
//    }
//
//    public void updateBeatRecentTime(BeatInfo beatInfo, Context context){
//        DataHelper dataHelper = new DataHelper(context);
//        dataHelper.updateBeatRecentTime(beatInfo);
//    }
//
//    public ArrayList<BeatInfo> getListRecentBeat(Context context){
//        DataHelper dataHelper = new DataHelper(context);
//        ArrayList<BeatInfo> arrBeatInfo = dataHelper.getListRecentBeat();
//        return arrBeatInfo;
//    }
//}
