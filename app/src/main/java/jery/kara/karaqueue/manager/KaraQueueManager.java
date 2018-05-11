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
import jery.kara.searchbeat.BeatInfo;
import jery.kara.searchbeat.SearchBeatDialog;

/**
 * Created by CPU11341-local on 08-May-18.
 */

public class KaraQueueManager {
    LyricView lyricView;
    Context context;
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

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLyricView(LyricView lyricView) {
        this.lyricView = lyricView;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> joinToQueue(BeatInfo beatInfo){
        //Thêm vào hàng đợi
        return queueData;
    }

    public List<User> removeFromQueue(int userID){
        //Xóa khỏi hàng đợi
        return queueData;
    }

    public void downloadFile(BeatInfo beatInfo){
        //Download beat & lyric
    }
}
