package jery.kara.karaqueue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karapersonal.manager.KaraPersonalManager;
import jery.kara.karaqueue.fragment.KaraQueueDialog;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.User;
import jery.kara.karaqueue.myview.PulsatorLayout;
import jery.kara.lyric.myview.LyricView;

public class KaraQueueActivity extends AppCompatActivity {
    static final int STATE_SINGER_LOADING = 1;
    static final int STATE_SINGING = 2;

    int state = STATE_SINGING;

    PulsatorLayout pulsatorLayout;
    LyricView lyricView;
    TextView btnOpenQueue;
    TextView songName;

    ImageView img_camera;
    ImageView img_setting;
    ImageView img_close;

    RelativeLayout loadingNewSingerLayout;
    RelativeLayout singerLayout;
    LinearLayout settingLayout;

    boolean isCameraOn = false;

    User currentUser = new User();
    private List<User> queueData = new ArrayList<>();

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
        btnOpenQueue.setText("Cầm mic (" + queueData.size() + ")");

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueManager.getInstance().turnOnOffCamera(isCameraOn);
                isCameraOn = !isCameraOn;
            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueManager.getInstance().showSettingDialog();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueManager.getInstance().onActionStopSong();
            }
        });

        setupUI(state, currentUser.type);
    }

    void setupUI(int state, int currentUserType){
        this.state = state;
        currentUser.type = currentUserType;

        if (state == STATE_SINGER_LOADING){
            loadingNewSingerLayout.setVisibility(View.VISIBLE);
            singerLayout.setVisibility(View.INVISIBLE);
        } else {
            switch (currentUserType) {
                case User.TYPE_BANNED:
                case User.TYPE_VIWER:
                case User.TYPE_WAITTING:
                    loadingNewSingerLayout.setVisibility(View.INVISIBLE);
                    singerLayout.setVisibility(View.VISIBLE);
                    lyricView.setVisibility(View.INVISIBLE);
                    settingLayout.setVisibility(View.INVISIBLE);
                    songName.setVisibility(View.VISIBLE);
                    pulsatorLayout.setVisibility(View.VISIBLE);
                    break;
                case User.TYPE_SINGER:
                    loadingNewSingerLayout.setVisibility(View.INVISIBLE);
                    singerLayout.setVisibility(View.VISIBLE);
                    lyricView.setVisibility(View.VISIBLE);
                    settingLayout.setVisibility(View.VISIBLE);
                    songName.setVisibility(View.INVISIBLE);
                    pulsatorLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    void init(){
        btnOpenQueue = (TextView) findViewById(R.id.btn_open_queue);
        lyricView = (LyricView) findViewById(R.id.lyricView);
        pulsatorLayout = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsatorLayout.startEffect();
        songName = (TextView) findViewById(R.id.lbl_songName);

        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_close = (ImageView) findViewById(R.id.img_close);

        loadingNewSingerLayout = (RelativeLayout) findViewById(R.id.loading_new_singer_layout);
        singerLayout = (RelativeLayout) findViewById(R.id.singer_layout);
        settingLayout = (LinearLayout) findViewById(R.id.setting_layout);

    }

    void loadData(){
        User u = new User();
        u.id = 0;
        u.role = User.ROLE_USER;
        u.type = User.TYPE_SINGER;
        u.name = "Ca sĩ đang hát";
        u.beatInfo.title = "Bài hát ca sĩ đang hát";
        if (currentUser.id == u.id){
            currentUser.type = u.type;
        }
        queueData.add(u);

        for (int i = 1; i < 10; i++){
            u = new User();
            u.id = i;
            u.role = User.ROLE_USER;
            u.type = User.TYPE_WAITTING;
            u.name = "Ca sĩ đang đợi " + i;
            u.beatInfo.title = "Bài hát đang đợi " + i;
            if (currentUser.id == u.id){
                currentUser.type = u.type;
            }
            queueData.add(u);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        KaraPersonalManager.getInstance().onActionStopSong();
        KaraPersonalManager.getInstance().deleteAllFile();
        KaraPersonalManager.getInstance().cancelDownload();
    }
}
