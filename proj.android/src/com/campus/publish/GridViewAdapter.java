package com.campus.publish;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.campus.R;
import com.campus.utils.BitmapWorkerTask;
import com.campus.utils.CommonUtil;

public class GridViewAdapter extends CursorAdapter {

	private String Tag = "GirdViewAdapter";
	private ArrayList<String> selectedIds;

	public GridViewAdapter(Context context, Cursor c, int flag) {
		super(context, c, flag);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public void setSelectedIds(ArrayList<String> seletedIds) {
		this.selectedIds = seletedIds;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		String filePath = cursor.getString(cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		if (view.getWidth() == 0) {
			int width = (CommonUtil.getScreenSize().x - 20 * 2 - 4 * 5) / 3;
			viewHolder.view.setLayoutParams(new FrameLayout.LayoutParams(width,
					width));
			viewHolder.mask.setLayoutParams(new FrameLayout.LayoutParams(width,
					width));
		}
		String str_id = String.valueOf(cursor.getInt(cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
		if (selectedIds != null && selectedIds.contains(str_id)) {

			viewHolder.mask.setVisibility(View.VISIBLE);
		} else
			viewHolder.mask.setVisibility(View.INVISIBLE);
		BitmapWorkerTask.loadBitmap(context, viewHolder.view, filePath);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		View convertView = LayoutInflater.from(mContext).inflate(
				R.layout.view_img_select_mask, null);
		viewHolder = new ViewHolder();
		viewHolder.view = (ImageView) convertView.findViewById(R.id.imgview);
		viewHolder.mask = (ImageView) convertView.findViewById(R.id.img_mask);
		convertView.setTag(viewHolder);
		return convertView;
	}

	class ViewHolder {
		ImageView view;
		ImageView mask;
	}
}
