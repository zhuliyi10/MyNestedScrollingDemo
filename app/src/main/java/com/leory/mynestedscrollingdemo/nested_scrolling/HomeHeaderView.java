package com.leory.mynestedscrollingdemo.nested_scrolling;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leory.mynestedscrollingdemo.R;

/**
 * @Description: 首页的头部
 * @Author: leory
 * @Time: 2020/12/7
 */
public class HomeHeaderView extends FrameLayout {
    private static final String TAG=HomeHeaderView.class.getSimpleName();
    public static final int HEADER_STATUS_EXPANDED = 1;//展开
    public static final int HEADER_STATUS_EXPANDING = 2;//正在展开
    public static final int HEADER_STATUS_CLOSED = 3;//折叠
    public static final int HEADER_STATUS_CLOSING = 4;//正在折叠
    public int mHeaderStatus = HEADER_STATUS_EXPANDED;
    private int mHeaderMinHeight;
    private int mBgInitWidth;
    private int mBgInitHeight;
    private int mImageMaxSize;
    private int mImageMinSize;
    private float mTextMaxSize = 14f;
    private float mTextMinSize = 12f;
    private View mBgChange;
    private TextView mTxtVideo;
    private TextView mTxtCamera;
    private TextView mTxtImage;
    private ImageView mImgVideo;
    private ImageView mImgCamera;
    private ImageView mImgImage;
    private LinearLayout mLlVideo;
    private LinearLayout mLlCamera;
    private LinearLayout mLlImage;


    private ValueAnimator mHeightAnimator;
    private ValueAnimator mWidthAnimator;


    public HomeHeaderView(@NonNull Context context) {
        this(context, null);
    }

    public HomeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mImageMaxSize = dp2px(50);
        mImageMinSize = dp2px(44);
        LayoutInflater.from(context).inflate(R.layout.head_home, this);
        mBgChange = findViewById(R.id.bg_change);
        mTxtVideo = findViewById(R.id.txt_video);
        mTxtCamera = findViewById(R.id.txt_camera);
        mTxtImage = findViewById(R.id.txt_image);
        mImgVideo = findViewById(R.id.img_video);
        mImgCamera = findViewById(R.id.img_camera);
        mImgImage = findViewById(R.id.img_image);
        mLlVideo=findViewById(R.id.ll_video);
        mLlCamera=findViewById(R.id.ll_camera);
        mLlImage=findViewById(R.id.ll_image);
        mBgChange.post(new Runnable() {
            @Override
            public void run() {
                mBgInitHeight = mBgChange.getMeasuredHeight();
                mBgInitWidth = mBgChange.getMeasuredWidth();
            }
        });

    }

    /**
     * 设置头部收缩动画
     */
    public void setHeaderShrink() {
        if (mHeaderStatus == HEADER_STATUS_EXPANDED) {
            if (mWidthAnimator != null && mWidthAnimator.isRunning()) {
                mWidthAnimator.cancel();
            }
            if (mHeightAnimator != null && mHeightAnimator.isRunning()) {
                mHeightAnimator.cancel();
            }
            mHeaderStatus = HEADER_STATUS_CLOSING;
            int width = getMeasuredWidth();
            float maxHeightScale = mHeaderMinHeight * 1f / mBgInitHeight;
            mHeightAnimator = ValueAnimator.ofFloat(1f, maxHeightScale);
            mHeightAnimator.setDuration(200);
            mHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    mBgChange.setPivotY(mBgInitHeight);
                    mBgChange.setScaleY(scale);
                    if (scale >= maxHeightScale) {
                        mHeaderStatus = HEADER_STATUS_CLOSED;
                    }
                }

            });
            mHeightAnimator.start();
            float maxWidthScale = width * 1f / mBgInitWidth;
            mWidthAnimator = ValueAnimator.ofFloat(1f, maxWidthScale);
            mWidthAnimator.setDuration(200);
            mWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    mBgChange.setScaleX(scale);
                }
            });
            mWidthAnimator.start();
        }
    }

    /**
     * 展开头部动画
     */
    public void setHeaderExpand() {
        if (mHeaderStatus == HEADER_STATUS_CLOSED) {
            if (mWidthAnimator != null && mWidthAnimator.isRunning()) {
                mWidthAnimator.cancel();
            }
            if (mHeightAnimator != null && mHeightAnimator.isRunning()) {
                mHeightAnimator.cancel();
            }
            mHeaderStatus = HEADER_STATUS_EXPANDING;
            int width = getMeasuredWidth();
            float maxHeightScale = mHeaderMinHeight * 1f / mBgInitHeight;
            mHeightAnimator = ValueAnimator.ofFloat(maxHeightScale, 1f);
            mHeightAnimator.setDuration(200);
            mHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    mBgChange.setPivotY(mBgInitHeight);
                    mBgChange.setScaleY(scale);
                    if (scale >= maxHeightScale) {
                        mHeaderStatus = HEADER_STATUS_EXPANDED;
                    }
                }

            });
            mHeightAnimator.start();
            float maxWidthScale = width * 1f / mBgInitWidth;
            mWidthAnimator = ValueAnimator.ofFloat(maxWidthScale, 1f);
            mWidthAnimator.setDuration(200);
            mWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    mBgChange.setScaleX(scale);
                }
            });
            mWidthAnimator.start();
        }
    }

    /**
     * 设置文字和图片缩放
     *
     * @param factor 标签滚动的百分比 0代表标签完全显示，1代表标签完全隐藏
     */
    public void setTextAndImageFactor(float factor) {

        float deltaTextSize = mTextMaxSize - mTextMinSize;
        float currentTextSize = mTextMaxSize - factor * deltaTextSize;
//        mTxtVideo.setTextSize(currentTextSize);
//        mTxtCamera.setTextSize(currentTextSize);
//        mTxtImage.setTextSize(currentTextSize);
//        Log.d(TAG, "setTextAndImageFactor: factor="+factor);
//        int deltaImageSize = mImageMaxSize - mImageMinSize;
//        int currentImageSize = (int) (mImageMaxSize - factor * deltaImageSize);
//        setImageSize(currentImageSize, mImgVideo);
//        setImageSize(currentImageSize, mImgCamera);
//        setImageSize(currentImageSize, mImgImage);

        //上面直接设置字体大小和图片大小会有抖动
        float scale=currentTextSize/mTextMaxSize;
        mLlVideo.setScaleX(scale);
        mLlVideo.setScaleY(scale);
        mLlCamera.setScaleX(scale);
        mLlCamera.setScaleY(scale);
        mLlImage.setScaleX(scale);
        mLlImage.setScaleY(scale);
    }

    /**
     * 设置图片的大小
     */
    private void setImageSize(int currentImageSize, ImageView target) {
        ViewGroup.LayoutParams lp = target.getLayoutParams();
        lp.width = currentImageSize;
        lp.height = currentImageSize;
        target.setLayoutParams(lp);
    }

    /**
     * 设置最小高度
     *
     * @param headerMinHeight
     */
    public void setHeaderMinHeight(int headerMinHeight) {
        mHeaderMinHeight = headerMinHeight;
    }


    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getResources().getDisplayMetrics());
    }
}
