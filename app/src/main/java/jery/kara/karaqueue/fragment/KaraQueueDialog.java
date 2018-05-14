package jery.kara.karaqueue.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.KaraQueueActivity;
import jery.kara.karaqueue.adapter.QueueAdapter;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.User;
import jery.kara.karaqueue.myview.PulsatorLayout;
import jery.kara.lyric.myview.LyricView;
import jery.kara.searchbeat.BeatInfo;
import jery.kara.searchbeat.SearchBeatDialog;

/**
 * Created by CPU11341-local on 08-May-18.
 */

public class KaraQueueDialog extends DialogFragment {
    TextView choose_song;
    RecyclerView rv;
    QueueAdapter adapter;
    List<User> queueData = new ArrayList<>();
    TextView lbl_listQueue;
    User currentUser = new User();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_kara_queue, null);
        Dialog a = new Dialog(getActivity());
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(view);
        a.getWindow().setGravity(Gravity.BOTTOM);
        a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        a.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        a.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight()*3/4);
        a.setCanceledOnTouchOutside(true);

        queueData = KaraQueueManager.getInstance().getQueueData();
        currentUser = KaraQueueManager.getInstance().getCurrentUser();
        KaraQueueManager.getInstance().setOnQueueChangeListener(new KaraQueueManager.OnQueueChangeListener() {
            @Override
            public void onQueueChange() {
                adapter.notifyDataSetChanged();
                rv.scheduleLayoutAnimation();
            }
        });

        init(view);

        choose_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               KaraQueueManager.getInstance().onChooseSongClicked();
            }
        });

        setButtonChooseSongType();

        return a;
    }

    private void init(View view) {
        lbl_listQueue = (TextView) view.findViewById(R.id.lbl_listQueue);
        lbl_listQueue.setText("Lượt cầm mic (" + queueData.size() + ")");
        choose_song = (TextView) view.findViewById(R.id.lbl_chooseSong);
        rv = view.findViewById(R.id.listQueue);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        int resId = R.anim.layout_anim_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
        rv.setLayoutAnimation(animation);
        adapter = new QueueAdapter((AppCompatActivity) getActivity(), queueData);
        rv.setAdapter(adapter);
    }

    void setButtonChooseSongType(){
        switch (currentUser.type){
            case User.TYPE_BANNED:
                choose_song.setText("Bạn đã bị cấm cầm mic");
                choose_song.setBackgroundResource(R.drawable.radius_choosesong_banned_background);
                choose_song.setEnabled(false);
                break;
            case User.TYPE_VIWER:
                choose_song.setText("Chọn bài hát");
                choose_song.setBackgroundResource(R.drawable.radius_choosesong_background);
                choose_song.setEnabled(true);
                break;
            case User.TYPE_SINGER:
                choose_song.setText("Ngừng biểu diễn");
                choose_song.setBackgroundResource(R.drawable.radius_choosesong_background);
                choose_song.setEnabled(true);
                break;
            case User.TYPE_WAITTING:
                choose_song.setText("Rời khỏi hàng");
                choose_song.setBackgroundResource(R.drawable.radius_choosesong_background);
                choose_song.setEnabled(true);
                break;
        }
    }

    int getScreenHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }
}
