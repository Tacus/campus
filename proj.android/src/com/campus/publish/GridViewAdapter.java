package com.campus.publish;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.campus.R;

public class GridViewAdapter extends CursorAdapter {

	private Bitmap mPlaceHolderBitmap;
	private Cursor cursor;
	private String Tag = "GirdViewAdapter";

	public GridViewAdapter(Context context, Cursor c, int flag) {
		super(context, c, flag);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	private void loadBitmap(ImageView imageView, Cursor cursor) {
		// if (cancelPotentialWork(resId, imageView)) {
		// final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
		// final AsyncDrawable asyncDrawable = new AsyncDrawable(
		// context.getResources(), mPlaceHolderBitmap, task);
		// imageView.setImageDrawable(asyncDrawable);
		// task.execute(resId);
		// }
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	public static boolean cancelPotentialWork(int data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final int bitmapData = bitmapWorkerTask.data;
			// If bitmapData is not yet set or it differs from the new data
			if (bitmapData == 0 || bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private int data = 0;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			return decodeSampledBitmapFromResource(mContext.getResources(),
					data, 100, 100);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	class ViewHolder {
		ImageView view;
		RelativeLayout mask;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) view.getTag();

		for (int i = 0; i < cursor.getColumnCount(); i++) {
			Log.e(Tag, cursor.getColumnNames()[i]);
		}

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		View convertView = LayoutInflater.from(mContext).inflate(
				R.layout.view_img_select_mask, arg2);
		viewHolder = new ViewHolder();
		viewHolder.mask = (RelativeLayout) convertView
				.findViewById(R.id.rel_mask);
		viewHolder.view = (ImageView) convertView.findViewById(R.id.imgview);
		convertView.setTag(viewHolder);
		return convertView;
	}

}
