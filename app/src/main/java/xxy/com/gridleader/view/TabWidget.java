package xxy.com.gridleader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import xxy.com.gridleader.R;


public class TabWidget extends LinearLayout {

	private static final String TAG = TabWidget.class.getSimpleName();
	private int[] mDrawableIds = new int[] { R.drawable.bg_shouye, R.drawable.bg_static,R.drawable.bg_wode};

	private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
	private List<View> mViewList = new ArrayList<View>();
	private List<ImageView> mIndicateImgs = new ArrayList<ImageView>();

	private CharSequence[] mLabels;

	public TabWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TabWidget, defStyle, 0);

		mLabels = a.getTextArray(R.styleable.TabWidget_bottom_labels);

		a.recycle();

		init(context);
	}

	public TabWidget(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TabWidget(Context context) {
		super(context);
		init(context);
	}

	private void init(final Context context) {
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundColor(Color.parseColor("#6495ED"));

		LayoutInflater inflater = LayoutInflater.from(context);


		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) {

			final int index = i;

			final View view = inflater.inflate(R.layout.tab_item, null);

			final CheckedTextView itemName = (CheckedTextView) view
					.findViewById(R.id.item_name);
			itemName.setCompoundDrawablesWithIntrinsicBounds(null, context
					.getResources().getDrawable(mDrawableIds[i]), null, null);
			itemName.setText(mLabels[i]);


			this.addView(view, params);

			itemName.setTag(index);

			mCheckedList.add(itemName);
			mViewList.add(view);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					setTabsDisplay(context, index);

					if (null != mTabListener) {
						mTabListener.onTabSelected(index);
					}
				}
			});

			if (i == 0) {
				itemName.setChecked(true);
				itemName.setTextColor(getResources().getColor(R.color.tabbar_text_color));
				view.setBackgroundColor(getResources().getColor(R.color.tabbar_backgroud));
			} else {
				itemName.setChecked(false);
//				itemName.setTextColor(Color.rgb(19, 12, 14));
//				itemName.setTextColor(Color.LTGRAY);
				itemName.setTextColor(getResources().getColor(R.color.tabbar_text_color));
				view.setBackgroundColor(getResources().getColor(R.color.tabbar_backgroud));
			}

		}
	}

	public void setIndicateDisplay(Context context, int position,
                                   boolean visible) {
		int size = mIndicateImgs.size();
		if (size <= position) {
			return;
		}
		ImageView indicateImg = mIndicateImgs.get(position);
		indicateImg.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	public void setTabsDisplay(Context context, int index) {
		int size = mCheckedList.size();
		for (int i = 0; i < size; i++) {
			CheckedTextView checkedTextView = mCheckedList.get(i);
			if ((Integer) (checkedTextView.getTag()) == index) {

				checkedTextView.setChecked(true);
				checkedTextView.setTextColor(Color.parseColor("#15AB92"));
				mViewList.get(i).setBackgroundColor(getResources().getColor(R.color.tabbar_backgroud));
			} else {
				checkedTextView.setChecked(false);
//				checkedTextView.setTextColor(Color.rgb(19, 12, 14));
				checkedTextView.setTextColor(getResources().getColor(R.color.tabbar_text_color));
				mViewList.get(i).setBackgroundColor(getResources().getColor(R.color.tabbar_backgroud));
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthSpecMode != MeasureSpec.EXACTLY) {
			widthSpecSize = 0;
		}

		if (heightSpecMode != MeasureSpec.EXACTLY) {
			heightSpecSize = 0;
		}

		if (widthSpecMode == MeasureSpec.UNSPECIFIED
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
		}

		int width;
		int height;
		width = Math.max(getMeasuredWidth(), widthSpecSize);
		height = Math.max(this.getBackground().getIntrinsicHeight(),
				heightSpecSize);
		setMeasuredDimension(width, height);
	}

	private OnTabSelectedListener mTabListener;

	public interface OnTabSelectedListener {
		void onTabSelected(int index);
	}

	public void setOnTabSelectedListener(OnTabSelectedListener listener) {
		this.mTabListener = listener;
	}

}
