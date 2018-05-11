package jery.kara.karapersonal.myview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import jery.kara.R;

/**
 * Created by CPU11341-local on 10-Jan-18.
 */

public class DonutProgress extends View implements Runnable{
    private Paint finishedPaint;
    private Paint unfinishedPaint;
    protected Paint textPaint;
    private Paint innerCirclePaint;

    private int innerBackgroundColor;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float finishedStrokeWidth;
    private float unfinishedStrokeWidth;
    private float textSize;
    private int textColor;

    private RectF finishedOuterRect = new RectF();
    private RectF unfinishedOuterRect = new RectF();

    private final float default_stroke_width;
    private final int default_finished_color = Color.YELLOW;
    private final int default_unfinished_color = Color.rgb(204, 204, 204);
    private final int default_text_color = Color.rgb(255, 255, 255);

    private final float default_text_size;

    private float progress = 0;
    private int max = 100;
    private int startingDegree = 270;

    private int duration;
    private long startTime = -1;
    private boolean mStop = false;
    private boolean isLooping = false;
    private boolean showText = false;

    public DonutProgress(Context context) {
        this(context, null);
    }

    public DonutProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DonutProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = dp2px(getResources(), 15);
        default_stroke_width = dp2px(getResources(), 3);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DonutProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    private static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    protected void initPainters() {
        finishedPaint = new Paint();
        finishedPaint.setColor(finishedStrokeColor);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setAntiAlias(true);
        finishedPaint.setStrokeWidth(finishedStrokeWidth);

        unfinishedPaint = new Paint();
        unfinishedPaint.setColor(unfinishedStrokeColor);
        unfinishedPaint.setStyle(Paint.Style.STROKE);
        unfinishedPaint.setAntiAlias(true);
        unfinishedPaint.setStrokeWidth(unfinishedStrokeWidth);

        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(innerBackgroundColor);
        innerCirclePaint.setAntiAlias(true);
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.DonutProgress_donut_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.DonutProgress_donut_unfinished_color, default_unfinished_color);

        finishedStrokeWidth = attributes.getDimension(R.styleable.DonutProgress_donut_finished_stroke_width, default_stroke_width);
        unfinishedStrokeWidth = attributes.getDimension(R.styleable.DonutProgress_donut_unfinished_stroke_width, default_stroke_width);

        textColor = attributes.getColor(R.styleable.DonutProgress_donut_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.DonutProgress_donut_text_size, default_text_size);

        setProgress(attributes.getFloat(R.styleable.DonutProgress_donut_progress, 0));
        setBackgroundResource(R.drawable.stop);
    }

    public float getFinishedStrokeWidth() {
        return finishedStrokeWidth;
    }

    public void setFinishedStrokeWidth(float finishedStrokeWidth) {
        this.finishedStrokeWidth = finishedStrokeWidth;
    }

    public float getUnfinishedStrokeWidth() {
        return unfinishedStrokeWidth;
    }

    public void setUnfinishedStrokeWidth(float unfinishedStrokeWidth) {
        this.unfinishedStrokeWidth = unfinishedStrokeWidth;
    }

    private float getProgressAngle() {
        return getProgress() / (float) max * 360f;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
        }
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
    }

    public int getStartingDegree() {
        return startingDegree;
    }

    public void setStartingDegree(int startingDegree) {
        this.startingDegree = startingDegree;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        if (showText){
            innerBackgroundColor = Color.BLACK;
        } else {
            innerBackgroundColor = Color.TRANSPARENT;
        }
        innerCirclePaint.setColor(innerBackgroundColor);
        this.showText = showText;
    }

    public void start(){
        mStop = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        mStop = true;
        setProgress(0);
        startTime = -1;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public boolean isLooping() {
        return isLooping;
    }

    private void repeat() {
        setProgress(0);
        startTime = System.currentTimeMillis();
    }


    @Override
    public void run() {
        if (startTime == -1){
            startTime = System.currentTimeMillis();
        }

        long ts = System.currentTimeMillis() - startTime;
        while(true){
            if (mStop){
                return;
            }
            if (duration != 0) {
                setProgress(ts * 100 / duration);
            }
            ts = System.currentTimeMillis() - startTime;
            if (ts > duration) {
                if (isLooping){
                    repeat();
                }else {
                    mStop = true;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float delta = Math.max(finishedStrokeWidth, unfinishedStrokeWidth);
        finishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);
        unfinishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);

        float innerCircleRadius = (getWidth() - Math.min(finishedStrokeWidth, unfinishedStrokeWidth) + Math.abs(finishedStrokeWidth - unfinishedStrokeWidth)) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, innerCirclePaint);
        canvas.drawArc(finishedOuterRect, getStartingDegree(), getProgressAngle(), false, finishedPaint);
        canvas.drawArc(unfinishedOuterRect, getStartingDegree() + getProgressAngle(), 360 - getProgressAngle(), false, unfinishedPaint);

        if (showText) {
            String text = (int) progress + "%";
            if (!TextUtils.isEmpty(text)) {
                float textHeight = textPaint.descent() + textPaint.ascent();
                canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
            }
        }

        invalidate();
    }
}
