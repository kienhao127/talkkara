package jery.kara.karaqueue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.adapter.QueueAdapter;
import jery.kara.karaqueue.fragment.KaraQueueDialog;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.User;
import jery.kara.karaqueue.myview.PulsatorLayout;
import jery.kara.lyric.myview.LyricView;

public class KaraQueueActivity extends AppCompatActivity {
    PulsatorLayout pulsatorLayout;
    LyricView lyricView;
    TextView kara_queue;

    ImageView img_camera;
    ImageView img_setting;
    ImageView img_close;

    boolean isCameraOn = false;

    User currentUser = new User();
    private List<User> queueData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kara_queue);
        init();

        loadData();

        KaraQueueManager.getInstance().setContext(this);
        KaraQueueManager.getInstance().setLyricView(lyricView);
        KaraQueueManager.getInstance().setQueueData(queueData);

        currentUser.id = 10;
        currentUser.type = User.TYPE_VIWER;
        KaraQueueManager.getInstance().setCurrentUser(currentUser);

        kara_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueDialog karaQueueDialog = new KaraQueueDialog();
                karaQueueDialog.show(getFragmentManager(), "karaQueueDialog");
            }
        });
        kara_queue.setText("Cầm mic (" + queueData.size() + ")");

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
    }

    void init(){
        kara_queue = (TextView) findViewById(R.id.kara_queue);
        lyricView = (LyricView) findViewById(R.id.lyricView);
        pulsatorLayout = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsatorLayout.startEffect();
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_close = (ImageView) findViewById(R.id.img_close);
    }

    void loadData(){
        User u = new User();
        u.id = 0;
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
            u.type = User.TYPE_WAITTING;
            u.name = "Ca sĩ đang đợi " + i;
            u.beatInfo.title = "Bài hát đang đợi " + i;
            if (currentUser.id == u.id){
                currentUser.type = u.type;
            }
            queueData.add(u);
        }
    }
}
