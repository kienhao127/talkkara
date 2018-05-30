package jery.kara.karaqueue.manager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.model.QueueItem;
import jery.kara.karaqueue.model.User;
import jery.kara.karaqueue.view.ButtonOpenQueue;
import jery.kara.helper.KaraManager;
import jery.kara.searchbeat.BeatInfo;

/**
 * Created by CPU11341-local on 08-May-18.
 */

public class KaraQueueManager extends KaraManager {

    private static final int STATE_SINGER_LOADING = 1;
    private static final int STATE_SINGING = 2;

    private int state = STATE_SINGING;

    private List<QueueItem> mQueueData = new ArrayList<>();
    private QueueItem queueItem = new QueueItem();
    private User currentUser = new User();

    private TextView btnChooseSong;

    private static KaraQueueManager instance;
    private KaraQueueManager(){}
    public static KaraQueueManager getInstance(){
        if (instance == null){
            instance = new KaraQueueManager();
        }
        return instance;
    }

    public void setQueueData(List<QueueItem> queueData){
        mQueueData = queueData;
        if (onQueueChangeListener != null) {
            onQueueChangeListener.onQueueChange(mQueueData);
        }
    }

    public List<QueueItem> getQueueData() {
        return mQueueData;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setBtnChooseSong(TextView btnChooseSong) {
        this.btnChooseSong = btnChooseSong;
    }

    @Override
    protected void downloadSongStart() {
        btnChooseSong.setEnabled(false);
        btnChooseSong.setBackgroundResource(R.drawable.radius_choosesong_background);
        btnChooseSong.setText("Đang tải bài hát: 0%");
    }

    @Override
    protected void downloadSongProgress(int iProgress) {
        btnChooseSong.setEnabled(false);
        btnChooseSong.setBackgroundResource(R.drawable.radius_choosesong_background);
        btnChooseSong.setText("Đang tải bài hát: " + iProgress + "%");
    }

    @Override
    protected void downloadSongSuccess(BeatInfo beatInfo) {
        queueItem.songName = beatInfo.title;
        queueItem.userId = currentUser.id;
        queueItem.username = currentUser.name;
        queueItem.avatarURL = currentUser.avatarURL;

        joinToQueue(queueItem);
    }

    @Override
    protected void downloadSongError() {
        btnChooseSong.setText("Chọn bài hát");
        btnChooseSong.setBackgroundResource(R.drawable.radius_choosesong_background);
        btnChooseSong.setEnabled(true);
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
        removeFromQueue(queueItem);
        QueueItem nextSinger = getNextSinger();
        if (nextSinger != null){
            //Ca sĩ tiếp theo
        }
    }

    //Hát hết bài
    @Override
    protected void onBeatFinish() {
        removeFromQueue(queueItem);
        QueueItem nextSinger = getNextSinger();
        if (nextSinger != null){
            //Ca sĩ tiếp theo
        }
    }

    public void onChooseSongClicked(){
        switch (currentUser.type){
            case User.TYPE_VIWER:
                showSearchDailog();
                break;
            case User.TYPE_SINGER:
                stopSinging();
            case User.TYPE_WAITTING:
                removeFromQueue(queueItem);
                break;
        }
    }

    //Thêm vào hàng đợi
    private void joinToQueue(QueueItem queueItem){
        //Request in queue API(queueItem)
        //

        currentUser.type = User.TYPE_WAITTING;
        onUserTypeChangeListener.onUserTypeChange(currentUser.type);
    }

    //Xóa khỏi hàng đợi
    private void removeFromQueue(QueueItem queueItem){
        //Request out queue API(queueItem)
        //

        if (currentUser.type == User.TYPE_SINGER){
            state = STATE_SINGER_LOADING;
            onStateChangeListener.onStateChange(state);
        }

        currentUser.type = User.TYPE_VIWER;
        onUserTypeChangeListener.onUserTypeChange(currentUser.type);
    }

    //Bắt đầu hát
    private void startSinging(){
        currentUser.type = User.TYPE_SINGER;
        onUserTypeChangeListener.onUserTypeChange(currentUser.type);
        state = STATE_SINGING;
        onStateChangeListener.onStateChange(state);
    }

    private QueueItem getNextSinger(){
        if (mQueueData.size() >= 1){
            return mQueueData.get(0);
        }
        return null;
    }

    private void stopSinging(){
        //Ngừng biểu diễn
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

    private OnQueueChangeListener onQueueChangeListener;
    public interface OnQueueChangeListener{
        void onQueueChange(List<QueueItem> queueData);
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
