package com.scp.slidingmenu.view;

import com.nineoldandroids.view.ViewHelper;
import com.scp.slidingmenu.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 侧滑菜单
 * 
 * @author 宋春鹏
 *
 */
public class SlidingMenu extends HorizontalScrollView {
	private ViewGroup mMenu;// 菜单区域
	private ViewGroup mContent;// 内容区域
	private LinearLayout mWapper;
	private int mScreenWidth;// 屏幕的宽度
	private int mMenuWidth;// 侧滑菜单宽度
	private int mMenuRightPadding = 50;// 侧滑菜单与屏幕右侧的距离，单位dp
	private boolean once;// 只设置一次子View的宽和高
	private boolean isOpen;// 是否打开侧滑菜单

	public SlidingMenu(Context context) {
		super(context, null);// 调用两个参数的构造方法
	}

	/**
	 * 未使用自定义属性时自动调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);// 调用三个参数的构造方法

	}

	/**
	 * 当使用了自定义属性时，会自动调用此构造方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 获取我们自定义的属性
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.SlidingMenu, defStyleAttr, 0);
		int indexCount = typedArray.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = typedArray.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		typedArray.recycle();// 释放
		// 获取屏幕的宽和高
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
	}

	/**
	 * 设置子View的宽和高，设置自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			// 设置侧滑菜单的宽度
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			// 设置内容的宽度
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 通过设置偏移量将Menu隐藏
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	/**
	 * 监听手指滑动事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// 判断当前侧滑菜单的宽度是否大于内容区域左侧多出来的部分，是则显示侧滑菜单，否则隐藏
			int scrollX = getScrollX();// 内容区域左侧多出来的部分
			if (scrollX >= mMenuWidth / 2) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 打开侧滑菜单
	 */
	public void openMenu() {
		if (isOpen) {
			return;
		} else {
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}
	}

	/**
	 * 关闭侧滑菜单
	 */
	public void closeMenu() {
		if (!isOpen) {
			return;
		} else {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换侧滑菜单
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	/**
	 * 滚动发生时自动调用此方法 l:getScrollX
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;// 1~0
		// 调用属性动画，设置TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);
	}

}
