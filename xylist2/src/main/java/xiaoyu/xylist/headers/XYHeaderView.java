package xiaoyu.xylist.headers;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import xiaoyu.xylist.R;

/**
 * Created by lee on 16/10/9.
 */

public class XYHeaderView extends FrameLayout implements PtrUIHandler {

    private ImageView imgHat;
    private ImageView imgHatShadow;

    /**
     * The circle should be drawn and animated only if this variable is set to true.
     */
    private boolean isRefreshing;
    /**
     * records the percent for drawing, from {@link #onUIPositionChange(PtrFrameLayout, boolean,
     * byte, PtrIndicator)} method.
     */
    private float ptrPullDownPercent;
    private float circleScaleX = 0;
    private float circleTranslateY = 0;

    //-----   Customize properties   -----
    private int circleAnimDuration;

    private AnimatorSet circleAnimators;

    public XYHeaderView(Context context) {
        super(context);
        init(context, null);
    }

    public XYHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XYHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public XYHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //merge label must be used with attachToRoot=true
        LayoutInflater.from(context).inflate(R.layout.xylist_xyheader, this);
        imgHat = (ImageView) findViewById(R.id.iv_hat);
        imgHatShadow = (ImageView) findViewById(R.id.iv_hat_shadow);

        circleAnimDuration = 1000;

        circleAnimators = new AnimatorSet();
        ValueAnimator scaleXAnimator = ValueAnimator.ofFloat(1, 0.5F, 1, 0.5F, 1);
        setAnimators(scaleXAnimator);
        scaleXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleScaleX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(0F, -0.5F, 0F, 0.5F, 0F);
        setAnimators(translateYAnimator);
        translateYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleTranslateY = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        circleAnimators.playTogether(scaleXAnimator, translateYAnimator);
    }

    private void setAnimators(ValueAnimator animator) {
        animator.setDuration(circleAnimDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (imgHat != null && imgHatShadow != null) {
            canvas.save();
            canvas.translate(imgHat.getX(), imgHat.getY());
            float ballScale = 0.7F * ptrPullDownPercent + 0.3F;
            canvas.scale(ballScale, ballScale, imgHat.getWidth() / 2.0F, 0.0F);
            imgHat.draw(canvas);
            canvas.restore();

            if (isRefreshing) {
                canvas.save();
                float translateY = ((imgHat.getHeight() - imgHatShadow.getHeight()) / 2.0F) *
                        circleTranslateY;
                float scaleX = 0.7F * circleScaleX + 0.3F;
                canvas.translate(imgHatShadow.getX(), imgHatShadow.getY() + translateY);
                canvas.scale(scaleX, scaleX, imgHatShadow.getWidth() / 2.0F, imgHatShadow.getHeight() / 2.0F);
                imgHatShadow.draw(canvas);
                canvas.restore();
            }
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
                                   PtrIndicator ptrIndicator) {
        float percent = ptrIndicator.getCurrentPercent();
        if (percent <= 1) {
            this.ptrPullDownPercent = percent;
            postInvalidate();
        }
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        //empty implementation
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        //empty implementation
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        isRefreshing = true;
        circleAnimators.start();
        postInvalidate();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        isRefreshing = false;
        circleAnimators.end();
        postInvalidate();
    }
}