package jery.kara.karaqueue.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import jery.kara.R;

/**
 * Created by CPU11341-local on 29-May-18.
 */

public class WattingSingerLayout extends RelativeLayout {
    ImageView userAvatar;
    TextView notiLabel;

    public WattingSingerLayout(Context context) {
        this(context, null);
    }

    public WattingSingerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WattingSingerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.watting_singer_layout, this, true);

        init(view);
    }

    void init(View view){
        userAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        notiLabel = (TextView) view.findViewById(R.id.noti_label);
    }

    public void setAvatarUrl(String avatarUrl) {
        Glide.with(getContext())
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_microphone).error(R.drawable.ic_microphone))
                .into(userAvatar);
    }

    public void setNotify(String notiContent){
        notiLabel.setText(notiContent);
    }
}
