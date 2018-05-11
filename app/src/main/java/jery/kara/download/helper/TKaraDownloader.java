package jery.kara.download.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import jery.kara.searchbeat.BeatInfo;

/**
 * Created by CPU10584-local on 03-May-18.
 */

public class TKaraDownloader {
    private final String TAG = getClass().getName();
    private final String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TalkTV";

    private String beatFilePath;
    private String lrcFilePath;

    private boolean lyricSuccess = false;

    private Context mContext;
    private DownLoadSongResourceListener downLoadSongResourceListener;

    public TKaraDownloader(Context context) {
        this.mContext = context;
    }
    DownloadFile downloadLyric;
    DownloadFile downloadBeat;

    public void downloadSongResources(BeatInfo song) {
        if(!isExistFile(folderPath)) {
            boolean createFolder = initFolder(folderPath);
            if(!createFolder) {
                String error = "Failed to create directory "+ folderPath;
                Log.d(TAG,error);
            } else {
                Log.d(TAG, "created new folder");
            }
        }

        String beatFileName = String.valueOf(song.id) + ".mp3";
        beatFilePath = folderPath + File.separator + beatFileName;
        String lyricFileName = String.valueOf(song.id) + ".lrc";
        lrcFilePath = folderPath + File.separator + lyricFileName;

        downLoadSongResourceListener.onDownloadSongResoureStart();


        if (isExistFile(lrcFilePath) && isExistFile(beatFilePath)) {
            downLoadSongResourceListener.onDownloadSongResourceSuccess();
        } else {
            if(!isExistFile(lrcFilePath)) {
                downloadLyric = new DownloadFile(mContext, song.lyricDownloadLink, lrcFilePath);
                downloadLyric.setDownloadFileListener(new DownloadFile.DownloadFileListener() {
                    @Override
                    public void onProgress(int iProgress) {

                    }

                    @Override
                    public void onCompleted(String str) {
                        lyricSuccess = true;
                    }

                    @Override
                    public void onFailed(String url) {
                        Log.d(TAG,"Failed to download lyric " + url);
                        onDownloadFailed(url);
                    }
                });
                downloadLyric.execute();
            }

            if(!isExistFile(beatFilePath)) {

                downloadBeat = new DownloadFile(mContext, song.beatDownloadLink, beatFilePath);
                downloadBeat.setDownloadFileListener(new DownloadFile.DownloadFileListener() {
                    @Override
                    public void onProgress(int iProgress) {
                        if (downLoadSongResourceListener != null) {
                            downLoadSongResourceListener.onDownloadSongProgress(iProgress);
                        }
                    }

                    @Override
                    public void onCompleted(String str) {
                        if (downLoadSongResourceListener != null) {
                            if (lyricSuccess) {
                                downLoadSongResourceListener.onDownloadSongResourceSuccess();
                            } else {
                                onDownloadFailed("can't download lyric");
                            }
                        }
                    }

                    @Override
                    public void onFailed(String url) {
                        Log.d(TAG,"Failed to download beat " + url);
                        onDownloadFailed(url);
                    }
                });
                downloadBeat.execute();
            }
        }
    }


    private boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    private boolean initFolder(String path) {
        File folder = new File(path);
        return folder.mkdirs();
    }

    private boolean removeFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public void cancle(){
        if (downLoadSongResourceListener != null){
            downLoadSongResourceListener = null;
        }
        if (downloadBeat != null){
            downloadBeat.cancle();
        }
        if (downloadLyric != null){
            downloadLyric.cancle();
        }
    }

    private void onDownloadFailed(String error) {
        if(isExistFile(beatFilePath)) {
            removeFile(beatFilePath);
        }

        if(isExistFile(lrcFilePath)) {
            removeFile(lrcFilePath);
        }

        downLoadSongResourceListener.onDownloadSongResourceError(error);
    }

    public void setDownLoadSongResourceListener(DownLoadSongResourceListener downLoadSongResourceListener) {
        this.downLoadSongResourceListener = downLoadSongResourceListener;
    }

    public interface DownLoadSongResourceListener {
        void onDownloadSongResoureStart();
        void onDownloadSongProgress(int iProgress);
        void onDownloadSongResourceError(String error);
        void onDownloadSongResourceSuccess();
    }
}
