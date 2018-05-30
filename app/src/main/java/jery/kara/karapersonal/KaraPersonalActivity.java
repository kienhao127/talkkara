package jery.kara.karapersonal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import jery.kara.R;
import jery.kara.karapersonal.manager.KaraPersonalManager;
import jery.kara.karapersonal.view.DonutProgress;
import jery.kara.lyric.view.LyricView;

/**
 * Created by CPU11296-local on 1/8/2018.
 */

public class KaraPersonalActivity extends AppCompatActivity {

    static final int STATE_NONE = 1;
    static final int STATE_DOWNLOADING = 2;
    static final int STATE_PLAYING = 3;

    private ImageView karaBtn;
    private DonutProgress beatProgress;
    private LyricView lyricView;

    private KaraPersonalManager karaPersonalManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kara_personal);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        karaPersonalManager = new KaraPersonalManager();
        init();
    }

    private void init() {
        beatProgress = (DonutProgress) findViewById(R.id.beatProgress);
        karaBtn = (ImageView) findViewById(R.id.karaBtn);
        lyricView = (LyricView) findViewById(R.id.lyricView);

        karaPersonalManager.setOnStateChangeLister(new KaraPersonalManager.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == STATE_NONE){
                    beatProgress.setDownloading(false);
                    beatProgress.setProgress(0);
                    beatProgress.stop();
                    beatProgress.setVisibility(View.INVISIBLE);
                    karaBtn.setVisibility(View.VISIBLE);
                }
                if (state == STATE_DOWNLOADING){
                    beatProgress.setDownloading(true);
                    beatProgress.setVisibility(View.VISIBLE);
                    karaBtn.setVisibility(View.INVISIBLE);
                }
                if (state == STATE_PLAYING){
                    beatProgress.setDownloading(false);
                    beatProgress.setVisibility(View.VISIBLE);
                    karaBtn.setVisibility(View.INVISIBLE);
                    beatProgress.setProgress(0);
                }
            }
        });
        karaPersonalManager.setOnProgressChangeListener(new KaraPersonalManager.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int iProgress) {
                beatProgress.setProgress(iProgress);
            }
        });

        karaPersonalManager.setLyricView(lyricView);
        karaPersonalManager.setContext(this);

        karaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                karaPersonalManager.onControlViewClick();
            }
        });
        beatProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                karaPersonalManager.onControlViewClick();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        karaPersonalManager.onActionStopSong();
        karaPersonalManager.deleteAllFile();
        karaPersonalManager.cancelDownload();
    }
}
