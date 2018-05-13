package jery.kara.searchbeat;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jery.kara.MainActivity;
import jery.kara.R;
import jery.kara.karaqueue.KaraQueueActivity;
import jery.kara.searchbeat.helper.FetchData;

/**
 * Created by CPU11341-local on 06-Apr-18.
 */

public class SearchBeatDialog extends DialogFragment implements View.OnClickListener{

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    RecyclerView rv;
    SearchBeatAdapter adapter;
    private List<BeatInfo> data = new ArrayList<>();
    boolean isSearch = false;
    EditText searchBox;
    Toolbar toolbar;
    public TextView lbl_loading;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.search_beat_layout, null);
        Dialog a = new Dialog(getActivity());
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(view);
        a.getWindow().setGravity(Gravity.BOTTOM);
        a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        a.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (getActivity() instanceof KaraQueueActivity){
            a.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        a.setCanceledOnTouchOutside(true);

        init(view);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        FetchData fetchData = new FetchData(this);
        fetchData.execute("http://120.138.76.165/cgi-bin/karaoke/hot-songs");

        return a;
    }

    private void init(View view) {
        searchBox = view.findViewById(R.id.search_box);
        rv = view.findViewById(R.id.rv);
        view.findViewById(R.id.img_search).setOnClickListener(this);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        int resId = R.anim.layout_anim_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
        rv.setLayoutAnimation(animation);

        adapter = new SearchBeatAdapter((AppCompatActivity) getActivity(), data);
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new SearchBeatAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(View view, BeatInfo beatInfo) {
                if (checkPermission()) {
                    isSearch = false;
                    searchSongListener.onSongSelected(beatInfo);
                    dismiss();
                } else {
                    requestPermission();
                }

            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        lbl_loading = (TextView) view.findViewById(R.id.lbl_loading);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }

    public void updateData(List<BeatInfo> data){
        this.data.clear();
        if (data.size() == 0){
            BeatInfo b = new BeatInfo();
            b.type = BeatInfo.TYPE_RESULT;
            b.title = "KHÔNG TÌM THẤY BÀI HÁT NÀO";
            this.data.add(b);
        } else {
            if (!isSearch) {
                BeatInfo b = new BeatInfo();
                b.type = BeatInfo.TYPE_RESULT;
                b.title = "ĐỀ XUẤT";
                this.data.add(b);
            } else {
                BeatInfo b = new BeatInfo();
                b.type = BeatInfo.TYPE_RESULT;
                b.title = "KẾT QUẢ";
                this.data.add(b);
            }
            this.data.addAll(data);
        }
        adapter.notifyDataSetChanged();
        rv.scheduleLayoutAnimation();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.img_search) {
            doSearch();
        }
    }
    private void doSearch() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
        searchBox.clearFocus();
        isSearch = true;
        String keyword = String.valueOf(searchBox.getText());
        String url = "http://apimobi.talktv.vn/cgi-bin/karaoke/search-songs?keyword=";
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        FetchData fetchData = new FetchData(this);
        fetchData.execute(url + keyword);
    }

    private SearchSongListener searchSongListener;

    public void setSearchSongListener(SearchSongListener searchSongListener) {
        this.searchSongListener = searchSongListener;
    }

    public interface SearchSongListener {
        void onSongSelected(BeatInfo beatInfo);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
}
