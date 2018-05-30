package jery.kara.download.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import jery.kara.helper.KaraUtils;
import jery.kara.searchbeat.BeatInfo;

/**
 * Created by CPU10584-local on 03-May-18.
 */

public class TKaraDownloader {
    private final String TAG = getClass().getName();
    public static final String folderPath = KaraUtils.getFolderPath();

    private String beatFilePath;
    private String lrcFilePath;

    private boolean lyricSuccess;

    private Context mContext;

    DownloadFile downloadLyric, downloadBeat;
    private DownLoadSongResourceListener downLoadSongResourceListener;

    public TKaraDownloader(Context context) {
        this.mContext = context;
        lyricSuccess = false;

    }

    public void downloadSongResources(BeatInfo beatInfo) {
        if(!isExistFile(folderPath)) {
            boolean createFolder = initFolder(folderPath);
            if(!createFolder) {
                String error = "Failed to create directory "+ folderPath;
                Log.d(TAG,error);
            } else {
                Log.d(TAG, "created new folder");
            }
        }

        String beatFileName = String.valueOf(beatInfo.id) + ".mp3";
        beatFilePath = folderPath + File.separator + beatFileName;
        String lyricFileName = String.valueOf(beatInfo.id) + ".lrc";
        lrcFilePath = folderPath + File.separator + lyricFileName;

        downLoadSongResourceListener.onDownloadSongResoureStart();


        if (isExistFile(lrcFilePath) && isExistFile(beatFilePath)) {
            downLoadSongResourceListener.onDownloadSongResourceSuccess();
        } else {
            if(!isExistFile(lrcFilePath)) {
                if(downloadLyric != null) {
                    downloadLyric.cancel();
                }
                downloadLyric = new DownloadFile(mContext);
                downloadLyric.setDownloadFileListener(new DownloadFile.DownloadFileListener() {
                    @Override
                    public void onProgress(int iProgress) {

                    }

                    @Override
                    public void onCompleted() {
                        lyricSuccess = true;
                    }

                    @Override
                    public void onFailed() {
                        Log.d(TAG,"Failed to download lyric ");
                        lyricSuccess = false;
                        onDownloadFailed();
                    }
                });
                downloadLyric.execute(beatInfo.lyricDownloadLink, lrcFilePath);
            }

            if(!isExistFile(beatFilePath)) {
                if(downloadBeat != null) {
                    downloadBeat.cancel();
                }
                downloadBeat = new DownloadFile(mContext);
                downloadBeat.setDownloadFileListener(new DownloadFile.DownloadFileListener() {
                    @Override
                    public void onProgress(int iProgress) {
                        downLoadSongResourceListener.onDownloadSongProgress(iProgress);
                    }

                    @Override
                    public void onCompleted() {
                        if(lyricSuccess) {
                            downLoadSongResourceListener.onDownloadSongResourceSuccess();
                        } else {
                            Log.d(TAG, "can't download lyric");
                            onDownloadFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        Log.d(TAG,"Failed to download beat ");
                        onDownloadFailed();
                    }
                });
                downloadBeat.execute(beatInfo.beatDownloadLink, beatFilePath);
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

    private void onDownloadFailed() {
        if(isExistFile(beatFilePath)) {
            removeFile(beatFilePath);
        }

        if(isExistFile(lrcFilePath)) {
            removeFile(lrcFilePath);
        }

        downLoadSongResourceListener.onDownloadSongResourceError();
    }

    public void setDownLoadSongResourceListener(DownLoadSongResourceListener downLoadSongResourceListener) {
        this.downLoadSongResourceListener = downLoadSongResourceListener;
    }

    public void cancel() {
        if (downloadBeat != null) {
            downloadBeat.cancel();
        }
        if (downloadLyric != null){
            downloadLyric.cancel();
        }
        if(isExistFile(beatFilePath)) {
            removeFile(beatFilePath);
        }

        if(isExistFile(lrcFilePath)) {
            removeFile(lrcFilePath);
        }
    }

    public interface DownLoadSongResourceListener {
        void onDownloadSongResoureStart();
        void onDownloadSongProgress(int iProgress);
        void onDownloadSongResourceError();
        void onDownloadSongResourceSuccess();
    }
}