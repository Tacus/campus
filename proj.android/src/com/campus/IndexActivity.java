package com.campus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class IndexActivity extends Activity {

	private GridView gridView;
	private MyGridViewAdapter gridViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_index);
		initView();
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridViewAdapter = new MyGridViewAdapter(this);
		gridView.setAdapter(gridViewAdapter);
	}

	class MyGridViewAdapter extends BaseAdapter {

		private Context mContext;

		public MyGridViewAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categories.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				// imageView.setLayoutParams(new GridView.LayoutParams(200,
				// 200));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setAdjustViewBounds(true);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(categories[position]);
			return imageView;
		}

		private int[] categories = new int[] { R.drawable.category_daily,
				R.drawable.category_book, R.drawable.category_electric,
				R.drawable.category_more, R.drawable.category_more };

	}
}
