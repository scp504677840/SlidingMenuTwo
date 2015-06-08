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
 * �໬�˵�
 * 
 * @author �δ���
 *
 */
public class SlidingMenu extends HorizontalScrollView {
	private ViewGroup mMenu;// �˵�����
	private ViewGroup mContent;// ��������
	private LinearLayout mWapper;
	private int mScreenWidth;// ��Ļ�Ŀ��
	private int mMenuWidth;// �໬�˵����
	private int mMenuRightPadding = 50;// �໬�˵�����Ļ�Ҳ�ľ��룬��λdp
	private boolean once;// ֻ����һ����View�Ŀ�͸�
	private boolean isOpen;// �Ƿ�򿪲໬�˵�

	public SlidingMenu(Context context) {
		super(context, null);// �������������Ĺ��췽��
	}

	/**
	 * δʹ���Զ�������ʱ�Զ�����
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);// �������������Ĺ��췽��

	}

	/**
	 * ��ʹ�����Զ�������ʱ�����Զ����ô˹��췽��
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// ��ȡ�����Զ��������
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
		typedArray.recycle();// �ͷ�
		// ��ȡ��Ļ�Ŀ�͸�
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
	}

	/**
	 * ������View�Ŀ�͸ߣ������Լ��Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			// ���ò໬�˵��Ŀ��
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			// �������ݵĿ��
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * ͨ������ƫ������Menu����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	/**
	 * ������ָ�����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// �жϵ�ǰ�໬�˵��Ŀ���Ƿ��������������������Ĳ��֣�������ʾ�໬�˵�����������
			int scrollX = getScrollX();// ����������������Ĳ���
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
	 * �򿪲໬�˵�
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
	 * �رղ໬�˵�
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
	 * �л��໬�˵�
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	/**
	 * ��������ʱ�Զ����ô˷��� l:getScrollX
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;// 1~0
		// �������Զ���������TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);
	}

}
