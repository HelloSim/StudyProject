package com.sim.test.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一个动画演示的Activity类
 */
@SuppressLint("Registered")
public class AnimationActivity extends BaseActivity {

    @BindView(R.id.animation_frame_iv)
    ImageView animation_frame_iv;
    @BindView(R.id.animation_frame)
    Button animation_frame;
    @BindView(R.id.animation_translate)
    Button animation_translate;
    @BindView(R.id.animation_scale)
    Button animation_scale;
    @BindView(R.id.animation_rotate)
    Button animation_rotate;
    @BindView(R.id.animation_alpha)
    Button animation_alpha;
    @BindView(R.id.animation_set)
    Button animation_set;
    @BindView(R.id.animation_custom_tween)
    Button animation_custom_tween;
    @BindView(R.id.animation_property)
    Button animation_property;

    @BindView(R.id.animation_accelerate_interpolator)
    Button animation_accelerate_interpolator;
    @BindView(R.id.animation_accelerate_decelerate_interpolator)
    Button animation_accelerate_decelerate_interpolator;
    @BindView(R.id.animation_anticipate_interpolator)
    Button animation_anticipate_interpolator;
    @BindView(R.id.animation_anticipate_overshoot_interpolator)
    Button animation_anticipate_overshoot_interpolator;
    @BindView(R.id.animation_bounce_interpolator)
    Button animation_bounce_interpolator;
    @BindView(R.id.animation_cycle_interpolator)
    Button animation_cycle_interpolator;
    @BindView(R.id.animation_decelerate_interpolator)
    Button animation_decelerate_interpolator;
    @BindView(R.id.animation_overshoot_interpolator)
    Button animation_overshoot_interpolator;

    private ValueAnimator valueAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrice = new DisplayMetrics();
        display.getMetrics(metrice);
        animation_frame_iv.setAnimation(new MyAnimation(metrice.xdpi / 2, metrice.ydpi / 2, 3500));
    }

    @OnClick({R.id.animation_frame,
            R.id.animation_translate, R.id.animation_scale, R.id.animation_rotate, R.id.animation_alpha, R.id.animation_set, R.id.animation_custom_tween,
            R.id.animation_property,
            R.id.animation_accelerate_interpolator, R.id.animation_accelerate_decelerate_interpolator,
            R.id.animation_anticipate_interpolator, R.id.animation_anticipate_overshoot_interpolator,
            R.id.animation_bounce_interpolator, R.id.animation_cycle_interpolator,
            R.id.animation_decelerate_interpolator, R.id.animation_overshoot_interpolator})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.animation_frame:
                AnimationDrawable animationDrawable = (AnimationDrawable) animation_frame_iv.getBackground();
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                } else {
                    animationDrawable.start();//开启动画
                }
                break;

            case R.id.animation_translate:
                animation_translate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_translate));
                break;
            case R.id.animation_scale:
                animation_scale.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_scale));
                break;
            case R.id.animation_rotate:
                if (animation_rotate.getAnimation() == null) {
                    animation_rotate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_rotate));
                } else {
                    animation_rotate.clearAnimation();
                }
                break;
            case R.id.animation_alpha:
                animation_alpha.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_alpha));
                break;
            case R.id.animation_set:
                animation_set.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_set));
                break;
            case R.id.animation_custom_tween:
                break;
            case R.id.animation_property:
                valueAnim = ValueAnimator.ofInt(0, 360);
                valueAnim.setDuration(1000);
                valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator p1) {
                        int curvalue = (int) p1.getAnimatedValue();
                        animation_property.setRotationX(curvalue);
                    }
                });
                valueAnim.start();
                break;

            case R.id.animation_accelerate_interpolator:
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(animation_accelerate_interpolator, "translationX", 0f, 500f);
                oa1.setDuration(500);
                //AccelerateInterpolator： 在动画开始的地方速率改变比较慢，然后开始加速
                oa1.setInterpolator(new AccelerateInterpolator(5)); //设置加速插值器---参数值越大，加速度越大
                oa1.start();
                break;
            case R.id.animation_accelerate_decelerate_interpolator:
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(animation_accelerate_decelerate_interpolator, "translationX", 0f, 500f);
                oa2.setDuration(500);
                //AccelerateDecelerateInterpolator ：在动画开始与结束的地方速率改变比较慢，在中间的时候加速
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa2.start();
                break;
            case R.id.animation_anticipate_interpolator:
                ObjectAnimator oa3 = ObjectAnimator.ofFloat(animation_anticipate_interpolator, "translationX", 0f, 500f);
                oa3.setDuration(500);
                //AnticipateInterpolator :开始的时候向后移动，再向前移动，类似荡秋千，回荡再往前
                oa3.setInterpolator(new AnticipateInterpolator(5));//设置回荡秋千插值器---参数值越大，回荡效果越大
                oa3.start();
                break;
            case R.id.animation_anticipate_overshoot_interpolator:
                ObjectAnimator oa4 = ObjectAnimator.ofFloat(animation_anticipate_overshoot_interpolator, "translationX", 0f, 500f);
                oa4.setDuration(500);
                //AnticipateOvershootInterpolator ：开始的时候向后然后向前甩一定值后返回最后的值
                oa4.setInterpolator(new AnticipateOvershootInterpolator());
                oa4.start();
                break;
            case R.id.animation_bounce_interpolator:
                ObjectAnimator oa5 = ObjectAnimator.ofFloat(animation_bounce_interpolator, "translationX", 0f, 500f);
                oa5.setDuration(500);
                //BounceInterpolator：动画结束的时候弹起
                oa5.setInterpolator(new BounceInterpolator());
                oa5.start();
                break;
            case R.id.animation_cycle_interpolator:
                ObjectAnimator oa6 = ObjectAnimator.ofFloat(animation_cycle_interpolator, "translationX", 0f, 500f);
                oa6.setDuration(500);
                //CycleInterpolator：动画循环播放特定的次数，速率改变沿着正弦曲线
                oa6.setInterpolator(new CycleInterpolator(4));//设置正弦周期变化插值器---参数值为循环次数
                oa6.start();
                break;
            case R.id.animation_decelerate_interpolator:
                ObjectAnimator oa7 = ObjectAnimator.ofFloat(animation_decelerate_interpolator, "translationX", 0f, 500f);
                oa7.setDuration(500);
                //CycleInterpolator：动画循环播放特定的次数，速率改变沿着正弦曲线
                oa7.setInterpolator(new DecelerateInterpolator());
                oa7.start();
                break;
            case R.id.animation_overshoot_interpolator:
                ObjectAnimator oa8 = ObjectAnimator.ofFloat(animation_overshoot_interpolator, "translationX", 0f, 500f);
                oa8.setDuration(500);
                //DecelerateInterpolator ：在动画开始的地方先快后慢减速结束
                oa8.setInterpolator(new OvershootInterpolator());
                oa8.start();
                break;
        }
    }

    /**
     * 一个自定义补间动画类
     */
    class MyAnimation extends Animation {
        private float centerX;
        private float centerY;
        private int duration;
        private Camera camera = new Camera();

        public MyAnimation(float x, float y, int duration) {
            this.centerX = x;
            this.centerY = y;
            this.duration = duration;
        }
        public void initialize(int width, int height, int parentWidth, int parentHeight){
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(duration);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
        }
        protected void applyTransformation(float interpolatedTime, Transformation t){
            camera.save();
            camera.translate(100.0f - 100.0f * interpolatedTime, 150.0f * interpolatedTime, 80.0f - 80.0f * interpolatedTime);
            camera.rotateY(360 * interpolatedTime);
            camera.rotateX(360 * interpolatedTime);
            Matrix matrix = t.getMatrix();
            camera.getMatrix(matrix);
            matrix.preTranslate(-centerY, -centerY);
            matrix.postTranslate(centerX, centerX);
            camera.restore();
        }
    }

}
