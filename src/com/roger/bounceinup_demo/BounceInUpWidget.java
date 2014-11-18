package com.roger.bounceinup_demo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.roger.bounceinup_demo.R;
import com.roger.bounceinup_demo.library.AnimationHelper;
import com.roger.bounceinup_demo.library.Techniques;

public class BounceInUpWidget extends LinearLayout implements OnClickListener {
	private static final int SHOWUP_DURATION = 500;
	private static final int FALLDOWN_DURATION = 100;

	private static final int[] TAB_NAMES = new int[] { R.string.bounceinup_text1, R.string.bounceinup_text2, R.string.bounceinup_text3 };
	// private static final int[] TAB_BACKGROUND = new int[] {
	// R.drawable.bg_conditiontab_nomal, R.drawable.bg_conditiontab_nomal,
	// R.drawable.bg_conditiontab_nomal };

	public int selectedIndex;
	int textSize;
	int textSizeSelected;
	List<View> mViewList;
	private boolean isShowing;// 是否已显示卡片

	public BounceInUpWidget(Context context) {
		this(context, null);
	}

	public BounceInUpWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOrientation(HORIZONTAL);
		init();

	}

	@SuppressLint("NewApi")
	private void init() {
		mViewList = new ArrayList<View>();
		setBackgroundColor(Color.parseColor("#33000000"));
		LayoutInflater inflater = LayoutInflater.from(getContext());

		int leftMargin = dip2px(getContext(), 4);
		int bottomPadding = dip2px(getContext(), 4);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.leftMargin = leftMargin;
		params.gravity = Gravity.CENTER_VERTICAL;

		for (int i = 0; i < 3; i++) {
			TextView tabView = (TextView) inflater.inflate(R.layout.layout_bounceinup_widget_card_item, null);
			tabView.setText(TAB_NAMES[i]);
			tabView.setTag(i);
			tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			// tabView.setBackgroundResource(TAB_BACKGROUND[i]);
			tabView.setLayoutParams(params);
			tabView.setPadding(0, 0, 0, bottomPadding);
			tabView.setOnClickListener(this);
			tabView.setAlpha(0);
			mViewList.add(tabView);
			addView(tabView);
		}
		setSelected(0);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		long id = position;
		setSelected(position);
		if (mListener != null) {
			mListener.onItemClick(null, v, position, id);
		}
		selectedIndex = position;
	}

	public void setSelected(int index) {
		int count = getChildCount();
		if ((index + 1) <= count) {
			for (int i = 0; i < count; i++) {
				TextView child = (TextView) getChildAt(i);
				if (index == i) {
					child.setSelected(true);
					child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
					child.setTextColor(Color.parseColor("#35a96f"));
				} else {
					child.setSelected(false);
					child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					child.setTextColor(Color.parseColor("#7E7E7E"));
				}
			}
		}
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener listener) {
		mListener = listener;
	}

	public boolean isShown() {
		return isShowing;
	}

	/** 重置控件
	 * @param target
	 * @author lj
	 * @createTime 2014-11-14 下午2:52:47
	 */
	public void reset(View target) {
		ViewHelper.setAlpha(target, 1);
		ViewHelper.setScaleX(target, 1);
		ViewHelper.setScaleY(target, 1);
		ViewHelper.setTranslationX(target, 0);
		ViewHelper.setTranslationY(target, 0);
		ViewHelper.setRotation(target, 0);
		ViewHelper.setRotationY(target, 0);
		ViewHelper.setRotationX(target, 0);
		ViewHelper.setPivotX(target, target.getMeasuredWidth() / 2.0f);
		ViewHelper.setPivotY(target, target.getMeasuredHeight() / 2.0f);
	}

	/** 开始弹出动画
	 * 
	 * @author lj
	 * @createTime 2014-11-14 上午9:53:27
	 */
	public void beginShowUp() {
		isShowing = true;
		this.setVisibility(View.VISIBLE);
		reset(this);
		begInAnimator(BounceInUp, mViewList.get(2), 0L);
		begInAnimator(BounceInUp, mViewList.get(1), 50L);
		begInAnimator(BounceInUp, mViewList.get(0), 100L);
	}

	/** 开始坠落动画
	 * 
	 * @author lj
	 * @createTime 2014-11-14 下午1:56:45
	 */
	public void beginFallDown() {
		begInAnimator(FadeOutDown, mViewList.get(2), 0L);
		begInAnimator(FadeOutDown, mViewList.get(1), 50L);
		begInAnimator(FadeOutDown, mViewList.get(0), 100L);
		begInAnimator(FadeOut, this, 200L, new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				isShowing = false;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// ---add by lj

	public final static int BounceInUp = 0;
	public final static int FadeOutDown = 1;
	public final static int FadeOut = 2;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case BounceInUp:
					AnimationHelper.with(Techniques.BounceInUp).duration(SHOWUP_DURATION).interpolate(new AccelerateDecelerateInterpolator()).playOn(((DataHolder) msg.obj).mView);
					break;
				case FadeOutDown:
					AnimationHelper.with(Techniques.FadeOutDown).duration(FALLDOWN_DURATION).interpolate(new AccelerateDecelerateInterpolator())
							.playOn(((DataHolder) msg.obj).mView);
					break;
				case FadeOut:
					AnimationHelper.with(Techniques.FadeOutDown).duration(FALLDOWN_DURATION).interpolate(new AccelerateDecelerateInterpolator())
							.withListener(((DataHolder) msg.obj).mAnimatorListener).playOn(((DataHolder) msg.obj).mView);
					break;

				default:
					break;
			}
		}
	};

	protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
		Log.i("TAG", "onFocusChanged:" + gainFocus);
		if (!gainFocus) {
			beginFallDown();
		}
	};

	/** 开始动画
	 * @param type 动画类型
	 * @param mView 对象
	 * @param delay 延迟时间
	 * @author lj
	 * @createTime 2014-11-13 下午5:48:35
	 */
	public void begInAnimator(int type, View mView, Long delay) {
		DataHolder mDataHolder = new DataHolder();
		mDataHolder.mAnimatorListener = null;
		mDataHolder.mView = mView;
		Message msg = mHandler.obtainMessage();
		msg.what = type;
		msg.obj = mDataHolder;
		mHandler.sendMessageDelayed(msg, delay);
	}

	/** 开始动画（添加结束监听）
	 * @param type 动画类型
	 * @param mView 对象
	 * @param delay 延迟时间
	 * @param mAnimatorListener 动画监听
	 * @author lj
	 * @createTime 2014-11-14 上午10:57:08
	 */
	public void begInAnimator(int type, View mView, Long delay, AnimatorListener mAnimatorListener) {
		DataHolder mDataHolder = new DataHolder();
		mDataHolder.mAnimatorListener = mAnimatorListener;
		mDataHolder.mView = mView;
		Message msg = mHandler.obtainMessage();
		msg.what = type;
		msg.obj = mDataHolder;
		mHandler.sendMessageDelayed(msg, delay);
	}

	class DataHolder {
		public View mView;
		public AnimatorListener mAnimatorListener;
	}

}
