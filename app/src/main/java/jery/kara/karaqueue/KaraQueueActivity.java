package jery.kara.karaqueue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.fragment.KaraQueueDialog;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.QueueItem;
import jery.kara.karaqueue.model.User;
import jery.kara.karaqueue.view.ButtonOpenQueue;
import jery.kara.karaqueue.view.PulsatorLayout;
import jery.kara.karaqueue.view.WattingSingerLayout;
import jery.kara.lyric.view.LyricView;

public class KaraQueueActivity extends AppCompatActivity {
    static final int STATE_SINGER_LOADING = 1;
    static final int STATE_SINGING = 2;

    int state = STATE_SINGER_LOADING;

    PulsatorLayout pulsatorLayout;
    LyricView lyricView;
    ButtonOpenQueue btnOpenQueue;

    ImageView img_setting;

    WattingSingerLayout wattingSingerLayout;
    RelativeLayout lyricLayout;

    User currentUser = new User();
    private List<QueueItem> queueData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kara_queue);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();

        loadData();

        KaraQueueManager.getInstance().setContext(this);
        KaraQueueManager.getInstance().setLyricView(lyricView);
        KaraQueueManager.getInstance().setQueueData(queueData);

        currentUser.id = 10;
        currentUser.name = "Nguyễn Văn A";
        currentUser.avatarURL = "http://gallery.gamerha.net/wp-content/uploads/2018/03/gallery.gamerha.net-iron-man-2560x1440-avengers-infinity-war-4k-12794-400x226.jpg";
        currentUser.type = User.TYPE_VIWER;
        currentUser.role = User.ROLE_USER;

        KaraQueueManager.getInstance().setCurrentUser(currentUser);
        KaraQueueManager.getInstance().setBtnOpenQueue(btnOpenQueue);
        KaraQueueManager.getInstance().setOnStateChangeListener(new KaraQueueManager.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                setupUI(state, currentUser.type);
            }
        });
        KaraQueueManager.getInstance().setOnUserTypeChangeListener(new KaraQueueManager.OnUserTypeChangeListener() {
            @Override
            public void onUserTypeChange(int userType) {
                setupUI(state, userType);
            }
        });

        btnOpenQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueDialog karaQueueDialog = new KaraQueueDialog();
                karaQueueDialog.show(getFragmentManager(), "karaQueueDialog");
            }
        });
        btnOpenQueue.setQueueSize(queueData.size());

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueManager.getInstance().showSettingDialog();
            }
        });

        setupUI(state, currentUser.type);
    }

    void setupUI(int state, int currentUserType){
        this.state = state;
        currentUser.type = currentUserType;

        if (state == STATE_SINGER_LOADING){
            wattingSingerLayout.setVisibility(View.VISIBLE);
            wattingSingerLayout.setAvatarUrl("https://www.sideshowtoy.com/assets/products/400291-iron-man-mark-xlvi/lg/marvel-captain-america-civil-war-iron-man-mk-xlvi-legendary-scale-400291-05.jpg");
            wattingSingerLayout.setNotify("Đang đợi Iron Man cầm mic");
            lyricLayout.setVisibility(View.INVISIBLE);
            pulsatorLayout.setVisibility(View.INVISIBLE);
        } else {
            switch (currentUserType) {
                case User.TYPE_BANNED:
                case User.TYPE_VIWER:
                case User.TYPE_WAITTING:
                    wattingSingerLayout.setVisibility(View.INVISIBLE);
                    lyricLayout.setVisibility(View.INVISIBLE);
                    pulsatorLayout.setVisibility(View.VISIBLE);
                    break;
                case User.TYPE_SINGER:
                    wattingSingerLayout.setVisibility(View.INVISIBLE);
                    lyricLayout.setVisibility(View.VISIBLE);
                    pulsatorLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    void init(){
        btnOpenQueue = (ButtonOpenQueue) findViewById(R.id.btn_open_queue);
        lyricView = (LyricView) findViewById(R.id.lyricView);
        pulsatorLayout = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsatorLayout.setAvatarUrl("https://www.sideshowtoy.com/wp-content/uploads/2018/04/marvel-avengers-infinity-war-thanos-sixth-scale-figure-hot-toys-feature-903429-1.jpg");
        pulsatorLayout.startEffect();

        img_setting = (ImageView) findViewById(R.id.img_setting);

        wattingSingerLayout = (WattingSingerLayout) findViewById(R.id.watting_singer_layout);
        lyricLayout = (RelativeLayout) findViewById(R.id.lyric_layout);
    }

    void loadData(){
        QueueItem u = new QueueItem();
        u.userId = 0;
        u.username = "Ca sĩ đang hát";
        u.songName = "Bài hát ca sĩ đang hát";
        queueData.add(u);

        for (int i = 1; i < 10; i++){
            u = new QueueItem();
            u.userId = i;
            u.username = "Ca sĩ đang đợi " + i;
            u.songName = "Bài hát đang đợi " + i;
            queueData.add(u);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        KaraQueueManager.getInstance().onActionStopSong();
        KaraQueueManager.getInstance().deleteAllFile();
        KaraQueueManager.getInstance().cancelDownload();
    }
}
