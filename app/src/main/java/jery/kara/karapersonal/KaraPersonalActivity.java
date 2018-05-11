package jery.kara.karapersonal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import jery.kara.R;
import jery.kara.manager.KaraManager;
import jery.kara.karapersonal.myview.DonutProgress;
import jery.kara.lyric.myview.LyricView;

/**
 * Created by CPU11296-local on 1/8/2018.
 */

public class KaraPersonalActivity extends AppCompatActivity {

    static final int STATE_NONE = 1;
    static final int STATE_DOWNLOADING = 2;
    static final int STATE_PLAYING = 3;

    ImageView karaBtn;
    DonutProgress beatProgress;
    LyricView lyricView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kara_personal_activity);
        init();
    }

    private void init() {
        beatProgress = (DonutProgress) findViewById(R.id.beatProgress);
        karaBtn = (ImageView) findViewById(R.id.karaBtn);
        lyricView = (LyricView) findViewById(R.id.lyricView);

        final KaraManager karaManager = KaraManager.getInstance();
        karaManager.setOnStateChangeLister(new KaraManager.OnStateChangeLister() {
            @Override
            public void onStateChange(int state) {
                if (state == STATE_NONE){
                    beatProgress.setProgress(0);
                    beatProgress.stop();
                    beatProgress.setVisibility(View.INVISIBLE);
                    karaBtn.setVisibility(View.VISIBLE);
                }
                if (state == STATE_DOWNLOADING){
                    beatProgress.setVisibility(View.VISIBLE);
                    karaBtn.setVisibility(View.INVISIBLE);
                }
                if (state == STATE_PLAYING){
                    beatProgress.setProgress(0);
                }
            }
        });
        karaManager.setOnProgressChangeListener(new KaraManager.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int iProgress) {
                beatProgress.setProgress(iProgress);
            }
        });

        karaManager.setLyricView(lyricView);
        karaManager.setContext(this);

        karaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                karaManager.onControlViewClick();
            }
        });
        beatProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                karaManager.onControlViewClick();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        KaraManager.getInstance().onActionStopSong();
        KaraManager.getInstance().deleteAllFile();
        KaraManager.getInstance().cancelDownload();
    }
}
