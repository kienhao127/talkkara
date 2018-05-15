package jery.kara.manager;

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

import jery.kara.karapersonal.KaraPersonalActivity;
import jery.kara.download.helper.TKaraDownloader;
import jery.kara.settingbeat.SettingKaraDialog;
import jery.kara.lyric.model.Lyric;
import jery.kara.lyric.myview.LyricView;
import jery.kara.lyric.utils.LyricUtils;
import jery.kara.searchbeat.BeatInfo;
import jery.kara.searchbeat.SearchBeatDialog;

/**
 * Created by CPU11341-local on 27-Apr-18.
 */

public abstract class KaraManager {

    LyricView lyricView;
    MediaPlayer mediaPlayer;
    Lyric lyric;

    int indexOfTime;
    long currentTime;
    int beatDuration = 1;

    TKaraDownloader tKaraDownloader;
    protected Context context;

    //Settting Kara
    int beatVol = 30;
    int micVol = 70;
    int tone = 2;
    boolean isWhatUHearChecked = true;

    protected abstract void downloadSongStart();
    protected abstract void downloadSongProgress(int iProgress);
    protected abstract void downloadSongSuccess(BeatInfo beatInfo);
    protected abstract void downloadSongError();
    protected abstract void onCancelDownload();
    protected abstract void onBeatPlaying(int playingProgress);
    protected abstract void onBeatStop();
    protected abstract void onBeatFinish();

    public void setLyricView(LyricView lyricView) {
        this.lyricView = lyricView;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    protected void showSearchDailog(){
        SearchBeatDialog searchBeatDialog = new SearchBeatDialog();
        searchBeatDialog.setSearchSongListener(new SearchBeatDialog.SearchSongListener() {
            @Override
            public void onSongSelected(BeatInfo beatInfo) {
                downloadFile(beatInfo);
            }
        });
        searchBeatDialog.show(((Activity) context).getFragmentManager(), "searchBeatDialog");
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
        settingKaraDialog.show(((Activity) context).getFragmentManager(), "settingKaraDialog");
    }

    public void onActionStopSong(){
        stopSync();
    }

    protected void prepareLyric(String lyricLocalPath){
        lyric = LyricUtils.parseLyric(new File(lyricLocalPath), "UTF-8");
        lyricView.start();
        lyricView.receiveNewContent("--- *** ---", 500);
        lyricView.receiveNewContent(lyric.getArrSentences().get(0).getContent(), 500);
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
            lyricView.stop();
            onBeatStop();
        }
    }

    Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doSync();
            handler.postDelayed(this, 500);
        }
    };

    long beforeCurrentTime = 0;
    void doSync(){
        currentTime = mediaPlayer.getCurrentPosition();
        float playingProgress = currentTime*100/beatDuration;
        onBeatPlaying((int)playingProgress);
        if (indexOfTime < lyric.getArrSentences().size()){
            long lrcTime = lyric.getArrSentences().get(indexOfTime).getFromTime();
            if (currentTime >= lrcTime){
                if (indexOfTime < lyric.getArrSentences().size() - 1){
                    long time = lyric.getArrSentences().get(indexOfTime + 1).getFromTime() - lrcTime;
                    lyricView.receiveNewContent(lyric.getArrSentences().get(indexOfTime + 1).getContent(), (int) time);
                } else {
                    lyricView.receiveNewContent("", 500);
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

    void downloadFile(final BeatInfo beatInfo){

        tKaraDownloader = new TKaraDownloader(context);
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
            public void onDownloadSongResourceError(String error) {
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
        File dir = new File(Environment.getExternalStorageDirectory() + "/TalkTV");
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

    void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            tKaraDownloader.cancle();
        }
        onCancelDownload();
    }
}
