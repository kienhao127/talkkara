//package jery.kara.searchbeat.helper;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.DatabaseErrorHandler;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.net.BindException;
//import java.util.ArrayList;
//
//import jery.kara.searchbeat.BeatInfo;
//
///**
// * Created by CPU11341-local on 17-Apr-18.
// */
//
//public class DataHelper extends SQLiteOpenHelper {
//    public static final String DATABASE_NAME = "recentbeat.db";
//
//    public static final String RECENT_BEAT_TABLE_NAME = "beatinfo";
//    public static final String BEAT_COLUMN_NAME_TYPE = "type";
//    public static final String BEAT_COLUMN_NAME_ID = "id";
//    public static final String BEAT_COLUMN_NAME_TITLE = "title";
//    public static final String BEAT_COLUMN_NAME_AUTHOR = "author";
//    public static final String BEAT_COLUMN_NAME_DONWLOADLINK = "downloadlink";
//    public static final String BEAT_COLUMN_NAME_LOCALPATH = "localpath";
//    public static final String BEAT_COLUMN_NAME_LYRIC = "lyric";
//    public static final String BEAT_COLUMN_NAME_RECENTTIME = "recenttime";
//
//    private static final String RECENT_BEAT_TABLE_CREATE =
//            "CREATE TABLE " + RECENT_BEAT_TABLE_NAME + " (" +
//                    BEAT_COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
//                    BEAT_COLUMN_NAME_TYPE + " INTEGER, " +
//                    BEAT_COLUMN_NAME_TITLE + " TEXT, " +
//                    BEAT_COLUMN_NAME_AUTHOR + " TEXT, " +
//                    BEAT_COLUMN_NAME_DONWLOADLINK + " TEXT, " +
//                    BEAT_COLUMN_NAME_LOCALPATH + " TEXT, " +
//                    BEAT_COLUMN_NAME_LYRIC + " TEXT, " +
//                    BEAT_COLUMN_NAME_RECENTTIME + " INTEGER " +
//                    ");";
//
//    public DataHelper(Context context) {
//        super(context, DATABASE_NAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(RECENT_BEAT_TABLE_CREATE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        db.execSQL("DROP TABLE IF EXISTS" + RECENT_BEAT_TABLE_CREATE);
//        onCreate(db);
//    }
//
//    public void insertBeatInfo(BeatInfo beatInfo){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(BEAT_COLUMN_NAME_ID, beatInfo.id);
//        values.put(BEAT_COLUMN_NAME_TITLE, beatInfo.title);
//        values.put(BEAT_COLUMN_NAME_AUTHOR, beatInfo.author);
//        values.put(BEAT_COLUMN_NAME_DONWLOADLINK, beatInfo.downloadLink);
//        values.put(BEAT_COLUMN_NAME_LOCALPATH, beatInfo.localPath);
//        values.put(BEAT_COLUMN_NAME_LYRIC, beatInfo.lyric);
//        values.put(BEAT_COLUMN_NAME_RECENTTIME, beatInfo.recentTime);
//        values.put(BEAT_COLUMN_NAME_TYPE, beatInfo.type);
//
//        db.insert(RECENT_BEAT_TABLE_NAME, null, values);
//    }
//
//    public void updateBeatRecentTime(BeatInfo beatInfo){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(BEAT_COLUMN_NAME_RECENTTIME, beatInfo.recentTime);
//
//        db.update(RECENT_BEAT_TABLE_NAME, values, BEAT_COLUMN_NAME_ID + " = ?", new String[] {String.valueOf(beatInfo.id)});
//    }
//
//    public ArrayList<BeatInfo> getListRecentBeat(){
//        ArrayList<BeatInfo> arrBeatInfo = new ArrayList<>();
//        String selectQuery = "SELECT *" +
//                " FROM " + RECENT_BEAT_TABLE_NAME +
//                " ORDER BY " + BEAT_COLUMN_NAME_RECENTTIME + " DESC";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cBeat = db.rawQuery(selectQuery, null);
//        if (cBeat.moveToFirst()) {
//            do {
//                BeatInfo beatInfo = new BeatInfo();
//                beatInfo.id = cBeat.getInt(cBeat.getColumnIndex(BEAT_COLUMN_NAME_ID));
//                beatInfo.title = cBeat.getString(cBeat.getColumnIndex(BEAT_COLUMN_NAME_TITLE));
//                beatInfo.author = cBeat.getString(cBeat.getColumnIndex(BEAT_COLUMN_NAME_AUTHOR));
//                beatInfo.downloadLink = cBeat.getString(cBeat.getColumnIndex(BEAT_COLUMN_NAME_DONWLOADLINK));
//                beatInfo.localPath = cBeat.getString(cBeat.getColumnIndex(BEAT_COLUMN_NAME_LOCALPATH));
//                beatInfo.lyric = cBeat.getString(cBeat.getColumnIndex(BEAT_COLUMN_NAME_LYRIC));
//                beatInfo.recentTime = cBeat.getInt(cBeat.getColumnIndex(BEAT_COLUMN_NAME_RECENTTIME));
//                beatInfo.type = cBeat.getInt(cBeat.getColumnIndex(BEAT_COLUMN_NAME_TYPE));
//
//                arrBeatInfo.add(beatInfo);
//            } while (cBeat.moveToNext());
//        }
//        return arrBeatInfo;
//    }
//}
