package jery.kara.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import jery.kara.download.helper.TKaraDownloader;
import jery.kara.settingbeat.SettingKaraDialog;
import jery.kara.lyric.model.Lyric;
import jery.kara.lyric.view.LyricView;
import jery.kara.lyric.utils.LyricUtils;
import jery.kara.searchbeat.BeatInfo;
import jery.kara.searchbeat.SearchBeatDialog;

/**
 * Created by CPU11341-local on 27-Apr-18.
 */

public abstract class KaraManager {

    private LyricView mLyricView;
    private MediaPlayer mediaPlayer;
    private Lyric lyric;

    private int indexOfTime;
    private long currentTime;
    private int beatDuration = 1;

    private TKaraDownloader tKaraDownloader;
    protected Context mContext;

    //Settting Kara
    private int beatVol = 30;
    private int micVol = 70;
    private int tone = 2;
    private boolean isWhatUHearChecked = true;

    protected abstract void downloadSongStart();
    protected abstract void downloadSongProgress(int iProgress);
    protected abstract void downloadSongSuccess(BeatInfo beatInfo);
    protected abstract void downloadSongError();
    protected abstract void onCancelDownload();
    protected abstract void onBeatPlaying(int playingProgress);
    protected abstract void onBeatStop();
    protected abstract void onBeatFinish();

    public void setLyricView(LyricView lyricView) {
        mLyricView = lyricView;
    }
    public void setContext(Context context) {
        mContext = context;
    }

    protected void showSearchDailog(){
        SearchBeatDialog searchBeatDialog = new SearchBeatDialog();
        searchBeatDialog.setSearchSongListener(new SearchBeatDialog.SearchSongListener() {
            @Override
            public void onSongSelected(BeatInfo beatInfo) {
                downloadFile(beatInfo);
            }
        });
        searchBeatDialog.show(((Activity) mContext).getFragmentManager(), "searchBeatDialog");
    }

    public void showSettingDialog(){
        SettingKaraDialog settingKaraDialog = new SettingKaraDialog();
        settingKaraDialog.beatVolume = beatVol;
        settingKaraDialog.micVolume = micVol;
        settingKaraDialog.tone = tone;
        settingKaraDialog.isWhatUHearChecked = isWhatUHearChecked;

        settingKaraDialog.setOnSettingChangeListener(new SettingKaraDialog.OnSettingChangeListener() {
            @Override
            public void onBeatVolumeSeekbarChange(int value) {
                Log.d("Beat:", String.valueOf(value));
                beatVol = value;
            }

            @Override
            public void onMicVolumeSeekbarChange(int value) {
                Log.d("Mic:", String.valueOf(value));
                micVol = value;
            }

            @Override
            public void onToneSeekbarChange(int value) {
                Log.d("Tone:", String.valueOf(value));
                tone = value;
            }

            @Override
            public void onSwitchWhatUHearChange(boolean b) {
                Log.d("What U Hear:", String.valueOf(b));
                isWhatUHearChecked = b;
            }

            @Override
            public void onButtonStopClicked() {
                onActionStopSong();
            }
        });
        settingKaraDialog.show(((Activity) mContext).getFragmentManager(), "settingKaraDialog");
    }

    public void onActionStopSong(){
        stopSync();
    }

    protected void prepareLyric(String lyricLocalPath){
        lyric = LyricUtils.parseLyric(new File(lyricLocalPath), "UTF-8");
        mLyricView.start();
        mLyricView.receiveNewContent("--- *** ---", 500);
        mLyricView.receiveNewContent(lyric.getArrSentences().get(0).getContent(), 500);
        indexOfTime++;
    }

    protected void prepareBeat(String beatLocalPath){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(beatLocalPath);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void startSync(){
        currentTime = 0;
        indexOfTime = 0;
        beatDuration = mediaPlayer.getDuration();
        mediaPlayer.start();
        handler = new Handler();
        handler.postDelayed(runnable, 50);
    }

    protected void stopSync(){
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mLyricView.stop();
            onBeatStop();
        }
    }

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doSync();
            handler.postDelayed(this, 500);
        }
    };

    private long beforeCurrentTime = 0;
    private void doSync(){
        currentTime = mediaPlayer.getCurrentPosition();
        float playingProgress = currentTime*100/beatDuration;
        onBeatPlaying((int)playingProgress);
        if (indexOfTime < lyric.getArrSentences().size()){
            long lrcTime = lyric.getArrSentences().get(indexOfTime).getFromTime();
            if (currentTime >= lrcTime){
                if (indexOfTime < lyric.getArrSentences().size() - 1){
                    long time = lyric.getArrSentences().get(indexOfTime + 1).getFromTime() - lrcTime;
                    mLyricView.receiveNewContent(lyric.getArrSentences().get(indexOfTime + 1).getContent(), (int) time);
                } else {
                    mLyricView.receiveNewContent("", 500);
                }
                indexOfTime++;
            }
        }
        Log.d("isFinish", String.valueOf(currentTime) + " = " + String.valueOf(mediaPlayer.getDuration()));
        if (currentTime == beforeCurrentTime){
            onBeatFinish();
        }
        beforeCurrentTime = currentTime;
    }

    private void downloadFile(final BeatInfo beatInfo){

        tKaraDownloader = new TKaraDownloader(mContext);
        tKaraDownloader.setDownLoadSongResourceListener(new TKaraDownloader.DownLoadSongResourceListener() {
            @Override
            public void onDownloadSongResoureStart() {
                downloadSongStart();
            }

            @Override
            public void onDownloadSongProgress(int iProgress) {
                downloadSongProgress(iProgress);
            }

            @Override
            public void onDownloadSongResourceError() {
                downloadSongError();
                showAlertDialog();
            }

            @Override
            public void onDownloadSongResourceSuccess() {
                downloadSongSuccess(beatInfo);
            }
        });

        tKaraDownloader.downloadSongResources(beatInfo);
    }

    public void deleteAllFile(){
        File dir = new File(KaraUtils.getFolderPath());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    if (children[i].endsWith(".lrc") || children[i].endsWith(".mp3")) {
                        new File(dir, children[i]).delete();
                    }
                }
            }
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Thông báo");
        builder.setMessage("Tải không thành công");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void cancelDownload(){
        if (tKaraDownloader != null) {
            tKaraDownloader.cancel();
        }
        onCancelDownload();
    }
}
