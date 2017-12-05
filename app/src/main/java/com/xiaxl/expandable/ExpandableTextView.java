package com.xiaxl.expandable;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 展开 收缩 的TextView
 */
public class ExpandableTextView extends RelativeLayout {


    private TextView introText = null;
    private ImageView halfView = null;

    private TextView expandButton = null;


    /**
     * 数据
     */
    // 文字颜色
    private int mTextColor;
    // 文字size
    private int mTextSize;
    // 收起时，有几行
    private int mCollapselines;
    // 水平间距
    private int mMarginHorizontal;
    // 按钮是否可点击
    private boolean mBtnClickable;

    //


    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //
        initAttrs(context, attrs, defStyleAttr);
        //
        initData();
        //
        initUI(context);
    }

    private int mScreenWidth = 1080;

    private void initData() {
        try {
            Activity activity = getActivity();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        // 获取自定义属性
        TypedArray a = null;
        // 文字颜色
        mTextColor = 0x4a4a4a;
        // 文字size
        mTextSize = (int) (getResources().getDisplayMetrics().density * 14);
        // 收起时，有几行
        mCollapselines = 2;
        //
        mMarginHorizontal = 0;
        try {
            // 获取自定义属性
            a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
            // 文字颜色
            mTextColor = a.getColor(R.styleable.ExpandableTextView_expandable_textcolor, mTextColor);
            // 文字大小
            mTextSize = a.getDimensionPixelSize(R.styleable.ExpandableTextView_expandable_textsize, mTextSize);
            // 收起时，有几行
            mCollapselines = a.getInteger(R.styleable.ExpandableTextView_expandable_text_collapselines, mCollapselines);
            // 文字大小
            mMarginHorizontal = a.getDimensionPixelSize(R.styleable.ExpandableTextView_expandable_margin_horizontal, mMarginHorizontal);
            // 是否可点击
            mBtnClickable = a.getBoolean(R.styleable.ExpandableTextView_expandable_btn_clickable, true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
    }


    /**
     * @param context
     */
    private void initUI(Context context) {
        // 移除所有的子View
        this.removeAllViews();
        // 添加子View
        LayoutInflater.from(context).inflate(R.layout.expandable_tv_layout, this);
        // 默认为收起状态
        introText = (TextView) findViewById(R.id.intro);
        introText.setMaxLines(mCollapselines);
        // 渐变的图片
        halfView = (ImageView) findViewById(R.id.half_trans_top);
        halfView.setVisibility(View.GONE);
        // 展开 收起 按钮
        expandButton = (TextView) findViewById(R.id.expand);
        expandButton.setVisibility(View.GONE);
    }


    private String mContent;

    /**
     * 默认的显示为收起状态
     *
     * @param content 显示文字内容
     */
    public void updateUI(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        mContent = content;

        // ############显示内容设置##############
        // 颜色
        introText.setTextColor(mTextColor);
        // 字体大小
        introText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        // ############根据行数显示内容##############
        // 如果 测量行数 < 临界行数
        if (getLineCount(mContent) <= mCollapselines) {
            // 隐藏“查看更多”按钮
            expandButton.setVisibility(View.GONE);
            expandButton.setClickable(false);
            // 隐藏"渐变"图片
            halfView.setVisibility(View.GONE);
            halfView.setClickable(false);
        } else {
            // 显示“查看更多”按钮
            expandButton.setVisibility(View.VISIBLE);
            // 显示"渐变"图片
            halfView.setVisibility(View.VISIBLE);
            // 点击事件处理
            if (mBtnClickable) {
                halfView.setOnClickListener(expandBtnClickListener);
                expandButton.setOnClickListener(expandBtnClickListener);
            }
        }
        // ############显示文字##############
        /**
         * 显示文字
         */
        introText.setText(mContent);
    }

    private int getLineCount(CharSequence text) {
        if (text != null) {
            int mViewWidth = mScreenWidth - mMarginHorizontal;
            try {
                Activity activity = getActivity();
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                mViewWidth = dm.widthPixels - mMarginHorizontal;
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            StaticLayout staticLayout = new StaticLayout(text, introText.getPaint(), mViewWidth, Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
            int lineCount = staticLayout.getLineCount();
            return lineCount;
        }
        return 0;
    }

    public void setExpandBtnGravity(int gravity) {
        if (expandButton != null) {
            expandButton.setGravity(gravity);
        }
    }


    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        //
        updateUI(mContent);
    }


    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        //
        updateUI(mContent);
    }


    public void setCollapselines(int mCollapselines) {
        this.mCollapselines = mCollapselines;
        //
        updateUI(mContent);
    }


    //#########################回调方法###############################
    // 默认为收起的
    private boolean isExpand = false;

    View.OnClickListener expandBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 当前为收起状态,点击则是会展开
            if (isExpand == false) {
                // 展开
                // 文字展开
                introText.setMaxLines(Integer.MAX_VALUE);
                // 渐变透明度消失
                halfView.setVisibility(View.GONE);
                // “收起”文字显示
                expandButton.setText("收起");
                // 重新layout
                requestLayout();
                // 回调当前状态为展开
                if (listener != null) {
                    listener.onExpandStateChanged(true);
                }
            } else {
                // 文字收起
                introText.setMaxLines(mCollapselines);
                // 渐变 显示
                halfView.setVisibility(View.VISIBLE);
                // 文字变为"查看更多"
                expandButton.setText("查看更多");
                // 重新布局
                requestLayout();
                // 当前状态为收起
                if (listener != null) {
                    listener.onExpandStateChanged(false);
                }
            }

            isExpand = !isExpand;
        }
    };


    //#########################回调方法###############################

    private ExpandableTextView.OnExpandableChangeListener listener;

    public void setOnExpandableChangeListener(ExpandableTextView.OnExpandableChangeListener listener) {
        this.listener = listener;
    }


    public interface OnExpandableChangeListener {
        // 按时间
        void onExpandStateChanged(boolean isExpand);
    }


    //#########################回调方法###############################
    private Activity getActivity() throws Exception {
        Context context = getContext();
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (context instanceof Activity) {
            return (Activity) context;
        }
        throw new Exception("Unable to get Activity.");
    }

}
