package com.example.cycleview.view;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cycleview.cache.BitmapHelper;
import com.example.cycleview.tools.DrawableUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 无限轮播 ViewPager
 *
 * @author SvenHe
 * @version 1.0
 */
public class CycleViewPager extends RelativeLayout {

	/* 主 ViewPager */
	private ViewPager mViewPager;
	/* 主 ViewPager 下的标题 */
	private TextView mTvTitle;
	/* 标题数组 */
	private List<String> mTitles;
	/* 图片资源 id 数组 */
	private List<Integer> mResIds;
	/* 图片 URL 数组 */
	private List<String> mURLs;
	/* 图片总数 */
	private int mCount;

	/* Title - URL 对应 map */
	private LinkedHashMap<String, String> mURLMap;
	/* Title - 资源 Id 对应 map */
	private LinkedHashMap<String, Integer> mResIdMap;;

	/* 图片空间集合 */
	private List<ImageView> mImageViews;
	/* 底部布局容器 */
	private LinearLayout mBottomContainer;
	/* 底部红点指示器的父容器 */
	private LinearLayout mRedPointContainer;
	/* 图片自动轮询器 */
	private ViewPagerAutoRunTask mAutoRunTask;


	private int mBottomBackgroundColor = Color.argb(54, 0, 0, 0);
	private int mUnSelectedColor = Color.rgb(61, 59, 59);
	private int mSelectedColor = Color.rgb(255, 0, 0);
	// 图片轮播间隔时间，默认 3000
	private int mDuration = 3000;
	// 小红点组的方向
	private IndicatorDirection mIndicatorDirection = IndicatorDirection.CENTER;
	// 小红点组的大小
	private int mIndicatorPointSize = 16;
	private int mIndicatorRadius = mIndicatorPointSize / 2;
	private int mIndicatorPointMargin = mIndicatorPointSize / 2;
	private boolean isShowTitle = true;
	private float mTitleTextSize = 12;
	private int mTitleTextColor = Color.rgb(255, 255, 255);
	private Handler mHandler;

	public CycleViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 初始化组件
		initView(attrs);
	}

	public CycleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化组件
		initView(attrs);
	}

	public CycleViewPager(Context context) {
		super(context);
		// 初始化组件
		initView(null);
	}

	/* 初始化组件 */
	private void initView(AttributeSet attrs) {

		// ViewPager 组件(轮播器)
		mViewPager = new ViewPager(getContext());
		LayoutParams viewPagerParams = new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		mViewPager.setLayoutParams(viewPagerParams);
		addView(mViewPager);

		// TextView 组件(标题)
		mTvTitle = new TextView(getContext());

		// 初始化标题和指示器容器父容器
		mBottomContainer = new LinearLayout(getContext());

		// 初始化指示器的容器
		mRedPointContainer = new LinearLayout(getContext());

		// 添加至主容器
		mBottomContainer.addView(mTvTitle);
		addView(mBottomContainer);
		mBottomContainer.addView(mRedPointContainer);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 如果外界没传入handler，则自己创建
		if (mHandler == null){
			mHandler = new Handler();
		}


		// 验证合法性(非空)
		valid();

		// 获得总数
		mCount = mURLMap != null ? mURLMap.size() : mResIdMap.size();

		// 从 Map 转换至 set
		convertMap2Set();

		// 创建对应数量的 ImageView 控件，以显示图片
		mImageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mCount; i++) {
			mImageViews.add(new ImageView(getContext()));
		}

		// 设置标题样式
		mTvTitle.setText("我是标题");
		mTvTitle.setTextColor(mTitleTextColor );
		mTvTitle.setTextSize(mTitleTextSize );
		mTvTitle.setVisibility( isShowTitle ? View.VISIBLE : View.INVISIBLE);

		// 设置标题背景颜色
		RelativeLayout.LayoutParams layoutParams = new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBottomContainer.setBackgroundColor(mBottomBackgroundColor );
		mBottomContainer.setLayoutParams(layoutParams);
		mBottomContainer.setOrientation(LinearLayout.VERTICAL);

		// 设置指示器容器的样式
		LinearLayout.LayoutParams redPointContainerParams = new android.widget.LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		redPointContainerParams.gravity = mIndicatorDirection._value;
		redPointContainerParams.bottomMargin = 5;
		redPointContainerParams.rightMargin = 5;
		redPointContainerParams.leftMargin = 5;
		redPointContainerParams.topMargin = 2;
		mRedPointContainer.setOrientation(LinearLayout.HORIZONTAL);
		mRedPointContainer.setLayoutParams(redPointContainerParams);

		// 设置指示器红点的样式
		for  (int i = 0; i < mCount; i++) {
			View view = new View(getContext());
			LinearLayout.LayoutParams redPointParams = new LinearLayout.LayoutParams(
					mIndicatorPointSize , mIndicatorPointSize);

			if (i > 0) {
				redPointParams.leftMargin = mIndicatorPointMargin ;
			}

			view.setBackground(DrawableUtils.createEnableDisableSelector(mIndicatorRadius ,
					mUnSelectedColor , mSelectedColor ));
			view.setEnabled(false);
			view.setLayoutParams(redPointParams);
			mRedPointContainer.addView(view);
		}

		// 设置标题
		if (mTitles != null && mTitles.size() > 0) {
			if (mTitles.get(0) != null) {
				mTvTitle.setText(mTitles.get(0));
			}
		}
		// 设置 ViewPager 的适配器
		mViewPager.setAdapter(new MyPagerAdater());
		mRedPointContainer.getChildAt(0).setEnabled(true);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						int pos = position % mCount;
						for (int i = 0; i < mCount; i++) {
							View childAt = mRedPointContainer.getChildAt(i);
							childAt.setEnabled(i == pos);
						}

						if (mTitles.get(pos) != null) {
							mTvTitle.setText(mTitles.get(pos));
						}
					}
				});

		mViewPager.setCurrentItem(10000 * mCount);

		// 设置手指按下自动暂停
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						cancel();
						break;
					case MotionEvent.ACTION_UP:
						start();
						break;
					case MotionEvent.ACTION_CANCEL:
						start();
						break;
				}
				// 必须返回 false， 返回 true，表示事件不往后传递，那么滑动功能将失效
				return false;
			}
		});
	}

	/* 从 Map 转换至 set */
	private void convertMap2Set() {
		mTitles = new ArrayList<String>();
		if (mURLMap != null) {
			mURLs = new ArrayList<String>();

			Iterator<Map.Entry<String, String>> iterator = mURLMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();

				mURLs.add(value);
				mTitles.add(key);
			}
		}

		if (mResIdMap != null) {
			mResIds = new ArrayList<Integer>();

			Iterator<Map.Entry<String, Integer>> iterator = mResIdMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Integer> next = iterator.next();
				String key = next.getKey();
				Integer value = next.getValue();

				mResIds.add(value);
				mTitles.add(key);
			}
		}
	}

	/* 非空验证 */
	private void valid() {
		if (mURLMap == null && mResIdMap == null) {
			throw new NullPointerException("未设置需要显示的图片");
		}

		if (mURLMap != null && mURLMap.size() <= 0) {
			throw new NullPointerException("图片资源数量必须大于0");
		}

		if (mResIdMap != null && mResIdMap.size() <= 0) {
			throw new NullPointerException("图片资源数量必须大于0");
		}
	}

	private class MyPagerAdater extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int pos = position % mCount;
			ImageView imageView = mImageViews.get(pos);

			// 使用 ImageView 之前，先将其从父容器移除
			removeFromParent(imageView);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			if (mResIds != null && mResIds.size() > 0) {
				imageView.setImageResource(mResIds.get(pos));
			} else if (mURLs != null && mURLs.size() > 0) {
				BitmapHelper.getInstance(getContext(), mHandler).display(imageView, mURLs.get(pos));
			}

			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((ImageView) object);
		}

		/* 将子View从父容器移除 */
		private void removeFromParent(View view) {
			ViewParent parent = view.getParent();
			if (parent != null && parent instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) parent;
				group.removeView(view);
			}
		}
	}

	/* 自动轮播器 */
	private class ViewPagerAutoRunTask implements Runnable {
		private ViewPager mViewPager;

		public ViewPagerAutoRunTask(ViewPager viewPager) {
			this.mViewPager = viewPager;
		}

		@Override
		public void run() {
			int currItem = mViewPager.getCurrentItem();
			currItem++;
			if (currItem >= mViewPager.getAdapter().getCount()) {
				currItem = 0;
			}
			mViewPager.setCurrentItem(currItem);

			postDelayed(this, mDuration);
		}

		public void start(int duration) {
			cancel(this);
			postDelayed(this, mDuration);
		}

		public void cancel() {
			cancel(this);
		}

		private  void cancel(Runnable r) {
			if (mHandler != null && r != null){
				mHandler.removeCallbacks(r);
			}
		}

		private void postDelayed( Runnable r, int delayMillis) {
			if (mHandler != null && r != null){
				mHandler.postDelayed(r, delayMillis);
			}
		}
	}

	/**
	 * 开启自动轮播，默认3000ms
	 */
	public void start() {
		// 开启自动轮播
		if (mAutoRunTask != null) {
			mAutoRunTask.cancel();
			mAutoRunTask = null;
		}
		mAutoRunTask = new ViewPagerAutoRunTask(mViewPager);
		mAutoRunTask.start(mDuration);
	}

	/**
	 * 取消自动轮播
	 */
	public void cancel() {
		if (mAutoRunTask != null) {
			mAutoRunTask.cancel();
			mAutoRunTask = null;
		}
	}

	/** 获取"标题 - URL"对应 map */
	public Map<String, String> getURLMap() {
		return mURLMap;
	}

	/** 设置"标题 - URL"对应 map */
	public CycleViewPager setURLMap(LinkedHashMap<String, String> mURLMap) {
		this.mURLMap = mURLMap;
		return this;
	}

	/** 获取"标题 -资源 id"对应 map */
	public Map<String, Integer> getResIdMap() {
		return mResIdMap;
	}

	/** 设置"标题 -资源 id"对应 map */
	public CycleViewPager setResIdMap(LinkedHashMap<String, Integer> mResIdMap) {
		this.mResIdMap = mResIdMap;
		return this;
	}

	public enum IndicatorDirection {
		LEFT(Gravity.LEFT), CENTER(Gravity.CENTER), RIGHT(Gravity.RIGHT);
		IndicatorDirection(int value) {
			this._value = value;
		}

		private int _value;

		public int value() {
			return _value;
		}
	}

	/**
	 * 获取底部背景颜色
	 * @return
	 */
	public int getBottomBackgroundColor() {
		return mBottomBackgroundColor;
	}

	/**
	 * 获取指示器的圆角值
	 * @return
	 */
	public int getIndicatorRadius() {
		return mIndicatorRadius;
	}

	/**
	 * 获取指示器未选中的颜色
	 * @return
	 */
	public int getUnSelectedColor() {
		return mUnSelectedColor;
	}

	/**
	 * 获取指示器选中的颜色
	 * @return
	 */
	public int getSelectedColor() {
		return mSelectedColor;
	}

	/**
	 * 获取轮播器执行时间间隔
	 * @return
	 */
	public int getDuration() {
		return mDuration;
	}

	/**
	 * 获取指示器显示位置
	 * @return
	 */
	public IndicatorDirection getIndicatorDirection() {
		return mIndicatorDirection;
	}

	/**
	 * 获取指示器点的尺寸
	 * @return
	 */
	public int getIndicatorPointSize() {
		return mIndicatorPointSize;
	}

	/**
	 * 获取指示器间隔距离
	 * @return
	 */
	public int getIndicatorPointMargin() {
		return mIndicatorPointMargin;
	}

	/**
	 * 是否显示标题
	 * @return
	 */
	public boolean isShowTitle() {
		return isShowTitle;
	}

	/**
	 * 获取标题字体大小
	 * @return
	 */
	public float getTitleTextSize() {
		return mTitleTextSize;
	}

	/**
	 * 获取标题字体颜色
	 * @return
	 */
	public int getTitleTextColor() {
		return mTitleTextColor;
	}

	/**
	 * 设置底部背景颜色
	 * @param bottomBackgroundColor
	 */
	public CycleViewPager setBottomBackgroundColor(int bottomBackgroundColor) {
		this.mBottomBackgroundColor = bottomBackgroundColor;
		return this;
	}

	/**
	 * 设置指示器的圆角值
	 * @param indicatorRadius
	 */
	public CycleViewPager setIndicatorRadius(int indicatorRadius) {
		this.mIndicatorRadius = indicatorRadius;
		return this;
	}

	/**
	 * 设置指示器未选中的颜色
	 * @param unSelectedColor
	 */
	public CycleViewPager setUnSelectedColor(int unSelectedColor) {
		this.mUnSelectedColor = unSelectedColor;
		return this;
	}

	/**
	 * 设置指示器选中的颜色
	 * @param selectedColor
	 */
	public CycleViewPager setSelectedColor(int selectedColor) {
		this.mSelectedColor = selectedColor;
		return this;
	}

	/**
	 * 设置轮播器执行时间间隔
	 * @param duration
	 */
	public CycleViewPager setDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	/**
	 * 设置指示器显示位置
	 * @param indicatorDirection
	 */
	public CycleViewPager setIndicatorDirection(IndicatorDirection indicatorDirection) {
		this.mIndicatorDirection = indicatorDirection;
		return this;
	}

	/**
	 * 设置指示器点的尺寸
	 * @param indicatorPointSize
	 */
	public CycleViewPager setIndicatorPointSize(int indicatorPointSize) {
		this.mIndicatorPointSize = indicatorPointSize;
		return this;
	}

	/**
	 * 设置指示器间隔距离
	 * @param indicatorPointMargin
	 */
	public CycleViewPager setIndicatorPointMargin(int indicatorPointMargin) {
		this.mIndicatorPointMargin = indicatorPointMargin;
		return this;
	}

	/**
	 * 设置是否显示标题
	 * @param isShowTitle
	 */
	public CycleViewPager setShowTitle(boolean isShowTitle) {
		this.isShowTitle = isShowTitle;
		return this;
	}

	/**
	 * 设置标题字体大小
	 * @param titleTextSize
	 */
	public CycleViewPager setTitleTextSize(float titleTextSize) {
		this.mTitleTextSize = titleTextSize;
		return this;
	}

	/**
	 * 设置标题字体颜色
	 * @param titleTextColor
	 */
	public CycleViewPager setTitleTextColor(int titleTextColor) {
		this.mTitleTextColor = titleTextColor;
		return this;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public CycleViewPager setHandler(Handler handler) {
		this.mHandler = handler;
		return this;
	}
}
