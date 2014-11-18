package com.roger.bounceinup_demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.roger.bounceinup_demo.R;
import com.roger.bounceinup_demo.MainActivity.MyAdapter.ViewHold;

public class MainActivity extends Activity {
	private Button button;
	private Button button1;
	private BounceInUpWidget widget;
	private GridView gridview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		widget = (BounceInUpWidget) findViewById(R.id.widget);
		button = (Button) findViewById(R.id.button);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyAdapter(this));
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				widget.beginShowUp();
			}
		});
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				widget.beginFallDown();
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				for (int i = 0; i < parent.getChildCount(); i++) {//收起已展示的item
					ViewHold mViewHold = (ViewHold) parent.getChildAt(i).getTag();
					if (mViewHold.mBounceInUpWidget.isShown()) {
						mViewHold.mBounceInUpWidget.beginFallDown();
					}
				}

				ViewHold mViewHold = (ViewHold) view.getTag();
				if (!mViewHold.mBounceInUpWidget.isShown()) {
					mViewHold.mBounceInUpWidget.beginShowUp();
				}
			}
		});

	}

	class MyAdapter extends BaseAdapter {
		// 上下文对象
		private Context context;

		MyAdapter(Context context) {
			this.context = context;
		}

		public Object getItem(int item) {
			return item;
		}

		public long getItemId(int id) {
			return id;
		}

		// 创建View方法
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold mViewHold;
			if (convertView == null) {
				mViewHold = new ViewHold();
				convertView = LayoutInflater.from(context).inflate(R.layout.layout_gridview_item, null);
				mViewHold.mBounceInUpWidget = (BounceInUpWidget) convertView.findViewById(R.id.widget);
				convertView.setTag(mViewHold);
			} else {
				mViewHold = (ViewHold) convertView.getTag();
			}
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		class ViewHold {
			BounceInUpWidget mBounceInUpWidget;
		}
	}
}
