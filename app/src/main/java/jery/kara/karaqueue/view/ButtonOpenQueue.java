package jery.kara.karaqueue.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jery.kara.R;

/**
 * Created by CPU11341-local on 29-May-18.
 */

public class ButtonOpenQueue extends RelativeLayout {
    TextView btnContent;

    public ButtonOpenQueue(Context context) {
        this(context, null);
    }

    public ButtonOpenQueue(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonOpenQueue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.button_open_queue_layout, this, true);

        btnContent = (TextView) view.findViewById(R.id.btn_content);
    }

    public void setQueueSize(int size){
        btnContent.setText("Cầm mic (" + size + ")");
    }
}
