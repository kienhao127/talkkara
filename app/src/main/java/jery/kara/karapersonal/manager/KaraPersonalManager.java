package jery.kara.karapersonal.manager;

import jery.kara.lyric.myview.LyricView;
import jery.kara.manager.KaraManager;
import jery.kara.searchbeat.BeatInfo;

/**
 * Created by CPU11341-local on 11-May-18.
 */

public class KaraPersonalManager extends KaraManager {

    private static KaraPersonalManager instance;

    private KaraPersonalManager() {
    }

    public static KaraPersonalManager getInstance() {
        if (instance == null) {
            instance = new KaraPersonalManager();
        }
        return instance;
    }

    static final int STATE_NONE = 1;
    static final int STATE_DOWNLOADING = 2;
    static final int STATE_PLAYING = 3;

    int state = STATE_NONE;

    BeatInfo beatInfo;

    @Override
    protected void downloadSongStart() {
        state = STATE_DOWNLOADING;
        onStateChangeListener.onStateChange(state);
    }

    @Override
    protected void downloadSongProgress(int iProgress) {
        onProgressChangeListener.onProgressChange(iProgress);
    }

    @Override
    protected void downloadSongSuccess(BeatInfo beatInfo) {
        this.beatInfo = beatInfo;
        state = STATE_PLAYING;
        onStateChangeListener.onStateChange(state);
        prepareBeat(beatInfo.beatLocalPath);
        prepareLyric(beatInfo.lyricLocalPath);
        startSync();
    }

    @Override
    protected void downloadSongError() {
        state = STATE_NONE;
        onStateChangeListener.onStateChange(state);
    }

    @Override
    protected void onCancelDownload() {
        state = STATE_NONE;
    }

    @Override
    protected void onBeatPlaying(int playingProgress) {
        onProgressChangeListener.onProgressChange(playingProgress);
    }

    @Override
    protected void onBeatStop() {
        state = STATE_NONE;
        onStateChangeListener.onStateChange(state);
    }

    @Override
    protected void onBeatFinish() {
        state = STATE_PLAYING;
        onStateChangeListener.onStateChange(state);
        prepareBeat(beatInfo.beatLocalPath);
        prepareLyric(beatInfo.lyricLocalPath);
        startSync();
    }

    public void onControlViewClick(){
        if (state == STATE_NONE){
            showSearchDailog();
        }
        if (state == STATE_PLAYING){
            showSettingDialog();
        }
    }

    public interface OnStateChangeListener {
        void onStateChange(int state);
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeLister(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }


    public interface OnProgressChangeListener {
        void onProgressChange(int iProgress);
    }

    private OnProgressChangeListener onProgressChangeListener;

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}