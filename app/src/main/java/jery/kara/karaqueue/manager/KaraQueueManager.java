package jery.kara.karaqueue.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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

    static final int STATE_SINGER_LOADING = 1;
    static final int STATE_SINGING = 2;

    int state = STATE_SINGING;

    List<User> queueData = new ArrayList<>();
    User currentUser = new User();

    TextView btnOpenQueue;

    private static KaraQueueManager instance;
    private KaraQueueManager(){}
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

    public void setBtnOpenQueue(TextView btnOpenQueue) {
        this.btnOpenQueue = btnOpenQueue;
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

    //Chủ động dừng bài hát
    @Override
    protected void onBeatStop() {
        removeFromQueue(currentUser);
        state = STATE_SINGER_LOADING;
        onStateChangeListener.onStateChange(state);
    }

    //Hát hết bài
    @Override
    protected void onBeatFinish() {
        removeFromQueue(currentUser);
        state = STATE_SINGER_LOADING;
        onStateChangeListener.onStateChange(state);
    }

    public void onChooseSongClicked(){
        switch (currentUser.type){
            case User.TYPE_BANNED:
            case User.TYPE_VIWER:
                showSearchDailog();
                break;
            case User.TYPE_SINGER:
                stopSinging();
            case User.TYPE_WAITTING:
                removeFromQueue(currentUser);
                break;
        }
    }

    //Thêm vào hàng đợi
    public void joinToQueue(BeatInfo beatInfo){
        currentUser.beatInfo = beatInfo;
        currentUser.type = User.TYPE_WAITTING;
        queueData.add(currentUser);
        btnOpenQueue.setText("Cầm mic (" + queueData.size() + ")");
        onUserTypeChangeListener.onUserTypeChange(currentUser.type);
        onQueueChangeListener.onQueueChange();
    }

    //Xóa khỏi hàng đợi
    public void removeFromQueue(User user){
        queueData.remove(user);
        currentUser.type = User.TYPE_VIWER;
        btnOpenQueue.setText("Cầm mic (" + queueData.size() + ")");
        onUserTypeChangeListener.onUserTypeChange(currentUser.type);
        onQueueChangeListener.onQueueChange();
    }

    //Bắt đầu hát
    void startSinging(){
        currentUser.type = User.TYPE_SINGER;
        onUserTypeChangeListener.onUserTypeChange(currentUser.type);
        state = STATE_SINGING;
        onStateChangeListener.onStateChange(state);
    }

    void stopSinging(){
        //Ngừng biểu diễn
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        String notiString = "";
        builder.setMessage("Bạn có thật sự muốn ngừng biểu diễn?" + notiString);
        builder.setCancelable(false);
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Ngừng biểu diễn
                state = STATE_SINGER_LOADING;
                onStateChangeListener.onStateChange(state);
            }
        });
        builder.setPositiveButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    public interface OnStateChangeListener{
        void onStateChange(int state);
    }
    private OnStateChangeListener onStateChangeListener;
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public interface OnUserTypeChangeListener{
        void onUserTypeChange(int userType);
    }
    private OnUserTypeChangeListener onUserTypeChangeListener;
    public void setOnUserTypeChangeListener(OnUserTypeChangeListener onUserTypeChangeListener) {
        this.onUserTypeChangeListener = onUserTypeChangeListener;
    }
}
