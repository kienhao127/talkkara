package jery.kara.lyric.myview;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jery.kara.R;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class LyricView extends LinearLayout{

    ArrayList<TextView> cacheTextView = new ArrayList<>();
    TextView highlghtTextView;
    TextView waitingTextView;
    static int MAX_ANIM_DURATION = 1000;
    static int DEFAULT_HIGHLIGHT_COLOR = Color.parseColor("#76D572");
    static int DEFAULT_WAITTING_COLOR = Color.parseColor("#FFFFFF");
    String tmpContent = null;
    View parentView;
    FrameLayout frameLayout;
    int duration = 0;
    boolean isStarting = false;
    int highlightColor;
    int wattingColor;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LyricView, defStyleAttr, 0);
        highlightColor = attributes.getColor(R.styleable.LyricView_highlightColor, DEFAULT_HIGHLIGHT_COLOR);
        wattingColor = attributes.getColor(R.styleable.LyricView_normalColor, DEFAULT_WAITTING_COLOR);
    }

    void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        parentView = inflater.inflate(R.layout.lyric_view_layout, this);
        frameLayout = (FrameLayout) parentView.findViewById(R.id.container);
    }

    void reuseTextView(TextView view){
        frameLayout.removeView(view);
        cacheTextView.add(view);
    }

    TextView getAvailableTextView(){
        TextView view = null;
        if (cacheTextView.size() <= 0){
            view = new TextView(getContext());
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            cacheTextView.add(view);

            view = new TextView(getContext());
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            cacheTextView.add(view);

            view = new TextView(getContext());
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            cacheTextView.add(view);
        }

        if (cacheTextView.size() > 0){
            view = cacheTextView.get(0);
            cacheTextView.remove(0);
            return view;
        }
        return view;
    }

    void addToView(TextView view) {
        float py;
        ViewGroup.LayoutParams params = frameLayout.getLayoutParams();

        if (highlghtTextView != null) {
            py = highlghtTextView.getHeight() + 10;
            params.height = highlghtTextView.getMeasuredHeight() + 50 + waitingTextView.getMeasuredHeight();;
        } else {
            py = 0;
            params.height = waitingTextView.getMeasuredHeight();
        }
        view.setY(py);
        Log.d("Layout height: ", String.valueOf(params.height));
        frameLayout.setLayoutParams(params);
        frameLayout.addView(view);
    }

    long getAnimDuration() {
        if (duration < MAX_ANIM_DURATION){
            return duration;
        }
        return MAX_ANIM_DURATION;
    }

    void moveOut(){
        Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onMoveOutEnd(highlghtTextView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
        highlghtTextView.animate()
                .setListener(animationListener)
                .translationX(highlghtTextView.getX())
                .translationY(-highlghtTextView.getHeight())
                .setDuration(getAnimDuration())
                .start();
    }

    void moveUp(){
        waitingTextView.setTextColor(highlightColor);
        Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onMoveUpEnd(waitingTextView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
        waitingTextView.animate()
                .setListener(animationListener)
                .translationX(waitingTextView.getX())
                .translationY(0)
                .setDuration(getAnimDuration())
                .start();
    }

    void onMoveOutEnd(TextView view){
        view.setText("");
        reuseTextView(view);
    }

    void onMoveUpEnd(TextView view){
        highlghtTextView = view;
        pushWaitingTextView();
    }

    void pushWaitingTextView(){
        if (tmpContent != null){
            waitingTextView = getAvailableTextView();
            waitingTextView.setText(tmpContent);
            waitingTextView.setTextColor(wattingColor);
            waitingTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            waitingTextView.setTextSize(20);
            waitingTextView.setShadowLayer(1, 1,1,Color.BLACK);
            waitingTextView.setPadding(50,1,50,1);
            waitingTextView.measure(0, 0);
            addToView(waitingTextView);
            tmpContent = null;
        }
    }

    public void receiveNewContent(String content, int withDuration){
        if (!isStarting){
            return;
        }

        tmpContent = content;
        duration = withDuration;
        if (highlghtTextView != null){
            moveOut();
        }

        if (waitingTextView != null){
            moveUp();
        }


        if (highlghtTextView == null && waitingTextView == null){
            pushWaitingTextView();
        }
    }

    public void start() {
        isStarting = true;
    }

    public void stop() {
        isStarting = false;
        cacheTextView.clear();
        frameLayout.removeAllViews();
        highlghtTextView = null;
        waitingTextView = null;
    }

    float dX, dY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View parentView = (View) getParent();
        parentView.getWidth();
        parentView.getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //getX getY your coordinate
                dX = this.getX() - event.getRawX();
                dY = this.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x, y = 0;
                x = event.getRawX() + dX;
                y = event.getRawY() + dY;

                if (event.getRawY() + dY < 0) {
                    y = this.getY();
                }
                if (event.getRawY() + dY + this.getHeight() > parentView.getHeight()) {
                    y = parentView.getHeight() - this.getHeight();
                }

                if (event.getRawX() + dX < 0 - this.getWidth() / 2) {
                    x = 0 - this.getWidth() / 2;
                }
                if (event.getRawX() + dX + this.getWidth() / 2 > parentView.getHeight()) {
                    x = parentView.getHeight() - this.getWidth() / 2;
                }

                animate()
                        .x(x)
                        .y(y)
                        .setDuration(0)
                        .start();

                break;
            default:
                return false;
        }
        return true;
    }
}
