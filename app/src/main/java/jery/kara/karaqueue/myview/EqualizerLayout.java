package jery.kara.karaqueue.myview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CPU11341-local on 24-May-18.
 */

public class EqualizerLayout extends LinearLayout {

    public static final int INTERP_LINEAR = 0;
    public static final int INTERP_ACCELERATE = 1;
    public static final int INTERP_DECELERATE = 2;
    public static final int INTERP_ACCELERATE_DECELERATE = 3;

    private static final int DEFAULT_COLOR = Color.rgb(0, 116, 193);

    private int mColor;
    private AnimatorSet mAnimatorSet;
    private Paint mPaint;

    public EqualizerLayout(Context context) {
        this(context, null);
    }

    public EqualizerLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualizerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.BOTTOM);
        mColor = DEFAULT_COLOR;

        // create paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);

        // create views
        build();
    }

    void build(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(2,2,2,2);
        layoutParams.gravity = Gravity.BOTTOM;

        List<Animator> animators = new ArrayList<>();

            // setup view
        EqualizerView equalizerView1 = new EqualizerView(getContext());
        setPivots(equalizerView1);
        this.addView(equalizerView1, 0, layoutParams);
        ObjectAnimator scaleYAnimator1 = ObjectAnimator.ofFloat(equalizerView1, "scaleY",  0.2f, 0.8f, 0.1f, 0.1f, 0.3f, 0.1f, 0.2f, 0.8f, 0.7f, 0.2f, 0.4f, 0.9f, 0.7f, 0.6f, 0.1f, 0.3f, 0.1f, 0.4f, 0.1f, 0.8f, 0.7f, 0.9f, 0.5f, 0.6f, 0.3f, 0.1f);
        scaleYAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        animators.add(scaleYAnimator1);

        EqualizerView equalizerView2 = new EqualizerView(getContext());
        setPivots(equalizerView2);
        this.addView(equalizerView2, 1, layoutParams);
        ObjectAnimator scaleYAnimator2 = ObjectAnimator.ofFloat(equalizerView2, "scaleY", 0.2f, 0.5f, 1.0f, 0.5f, 0.3f, 0.1f, 0.2f, 0.3f, 0.5f, 0.1f, 0.6f, 0.5f, 0.3f, 0.7f, 0.8f, 0.9f, 0.3f, 0.1f, 0.5f, 0.3f, 0.6f, 1.0f, 0.6f, 0.7f, 0.4f, 0.1f);
        scaleYAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        animators.add(scaleYAnimator2);

        EqualizerView equalizerView3 = new EqualizerView(getContext());
        setPivots(equalizerView3);
        this.addView(equalizerView3, 2, layoutParams);
        ObjectAnimator scaleYAnimator3 = ObjectAnimator.ofFloat(equalizerView3, "scaleY", 0.6f, 0.5f, 1.0f, 0.6f, 0.5f, 1.0f, 0.6f, 0.5f, 1.0f, 0.5f, 0.6f, 0.7f, 0.2f, 0.3f, 0.1f, 0.5f, 0.4f, 0.6f, 0.7f, 0.1f, 0.4f, 0.3f, 0.1f, 0.4f, 0.3f, 0.7f);
        scaleYAnimator3.setRepeatCount(ValueAnimator.INFINITE);
        animators.add(scaleYAnimator3);


        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorSet.setDuration(3000);
        mAnimatorSet.start();
    }

    private class EqualizerView extends View {

        public EqualizerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRect(0,0, EqualizerLayout.this.getWidth(), EqualizerLayout.this.getBottom(), mPaint);
        }
    }

    private void setPivots(final EqualizerView equalizerView) {
        equalizerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                equalizerView.setPivotY(equalizerView.getHeight());
            }
        });
    }
}
