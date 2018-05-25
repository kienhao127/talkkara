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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.adapter.QueueAdapter;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.QueueItem;
import jery.kara.karaqueue.model.User;

/**
 * Created by CPU11341-local on 08-May-18.
 */

public class KaraQueueDialog extends DialogFragment {
    TextView btnChooseSong;
    RecyclerView karaQueue;
    QueueAdapter adapter;
    List<QueueItem> queueData = new ArrayList<>();
    TextView lbl_listQueue;
    User currentUser = new User();
    TextView lbl_loadingQueue;

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
        init(view);

        KaraQueueManager.getInstance().setBtnChooseSong(btnChooseSong);

        //Update Queue
        KaraQueueManager.getInstance().setOnQueueChangeListener(new KaraQueueManager.OnQueueChangeListener() {
            @Override
            public void onQueueChange() {
                adapter.notifyDataSetChanged();
                karaQueue.scheduleLayoutAnimation();
                updateQueueSize(queueData.size());
            }
        });

        KaraQueueManager.getInstance().setOnUserTypeChangeListener(new KaraQueueManager.OnUserTypeChangeListener() {
            @Override
            public void onUserTypeChange(int userType) {
                setButtonChooseSongType();
            }
        });

        //RecyclerView
        karaQueue.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        int resId = R.anim.layout_anim_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
        karaQueue.setLayoutAnimation(animation);
        adapter = new QueueAdapter((AppCompatActivity) getActivity(), queueData);
        karaQueue.setAdapter(adapter);

        btnChooseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               KaraQueueManager.getInstance().onChooseSongClicked();
            }
        });

        setButtonChooseSongType();

        return a;
    }

    private void init(View view) {
        lbl_loadingQueue = (TextView) view.findViewById(R.id.loading_queue);
        lbl_listQueue = (TextView) view.findViewById(R.id.lbl_listQueue);
        updateQueueSize(queueData.size());
        btnChooseSong = (TextView) view.findViewById(R.id.lbl_chooseSong);
        karaQueue = view.findViewById(R.id.karaQueue);
    }

    void updateQueueSize(int size){
        lbl_listQueue.setText("Lượt cầm mic (" + size + ")");
        if (queueData == null || queueData.size() == 0){
            lbl_loadingQueue.setVisibility(View.VISIBLE);
        } else {
            lbl_loadingQueue.setVisibility(View.INVISIBLE);
        }
    }

    void setButtonChooseSongType(){
        switch (currentUser.type){
            case User.TYPE_VIWER:
                btnChooseSong.setText("Chọn bài hát");
                btnChooseSong.setBackgroundResource(R.drawable.radius_choosesong_background);
                btnChooseSong.setEnabled(true);
                break;
            case User.TYPE_SINGER:
                btnChooseSong.setText("Ngừng biểu diễn");
                btnChooseSong.setBackgroundResource(R.drawable.radius_choosesong_background);
                btnChooseSong.setEnabled(true);
                break;
            case User.TYPE_WAITTING:
                btnChooseSong.setText("Rời khỏi hàng");
                btnChooseSong.setBackgroundResource(R.drawable.radius_choosesong_background);
                btnChooseSong.setEnabled(true);
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
