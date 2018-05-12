package jery.kara.karaqueue.manager;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.KaraQueueActivity;
import jery.kara.karaqueue.fragment.KaraQueueDialog;
import jery.kara.karaqueue.model.User;
import jery.kara.karaqueue.myview.PulsatorLayout;
import jery.kara.lyric.myview.LyricView;
import jery.kara.manager.KaraManager;
import jery.kara.searchbeat.BeatInfo;
import jery.kara.searchbeat.SearchBeatDialog;

/**
 * Created by CPU11341-local on 08-May-18.
 */

public class KaraQueueManager extends KaraManager {
    List<User> queueData = new ArrayList<>();
    User currentUser = new User();

    private static KaraQueueManager instance;
    private KaraQueueManager(){

    }
    public static KaraQueueManager getInstance(){
        if (instance == null){
            instance = new KaraQueueManager();
        }
        return instance;
    }

    public void setQueueData(List<User> queueData){
        this.queueData = queueData;
    }

    public List<User> getQueueData() {
        return queueData;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }


    @Override
    protected void downloadSongStart() {

    }

    @Override
    protected void downloadSongProgress(int iProgress) {

    }

    @Override
    protected void downloadSongSuccess(BeatInfo beatInfo) {
        joinToQueue(beatInfo);
    }

    @Override
    protected void downloadSongError() {

    }

    @Override
    protected void onCancelDownload() {

    }

    @Override
    protected void onBeatPlaying(int playingProgress) {

    }

    @Override
    protected void onBeatStop() {

    }

    public void onChooseSongClicked(){
        switch (currentUser.type){
            case User.TYPE_VIWER:
                showSearchDailog();
                break;
            case User.TYPE_MANAGER:
                break;
            case User.TYPE_SINGER:
            case User.TYPE_WAITTING:
                removeFromQueue(currentUser.id);
                break;
        }
    }

    public void joinToQueue(BeatInfo beatInfo){
        //Thêm vào hàng đợi
        onQueueChangeListener.onQueueChange();
    }

    public void removeFromQueue(int userID){
        //Xóa khỏi hàng đợi
        onQueueChangeListener.onQueueChange();
    }

    public void turnOnOffCamera(boolean isOn){
        if (isOn){
            //turn off
        } else {
            //turn on
        }
    }

    private OnQueueChangeListener onQueueChangeListener;
    public interface OnQueueChangeListener{
        void onQueueChange();
    }
    public void setOnQueueChangeListener(OnQueueChangeListener onQueueChangeListener) {
        this.onQueueChangeListener = onQueueChangeListener;
    }
}
