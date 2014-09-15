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
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.campus.R;
import com.campus.utils.CommonUtil;

public class GridViewAdapter extends CursorAdapter {

	private Bitmap mPlaceHolderBitmap;
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

	private void loadBitmap(ImageView imageView, String filePath) {
		loadBitmap(
				imageView,
				filePath,
				mContext.getResources().getInteger(
						R.integer.defautl_thumbnail_width),
				mContext.getResources().getInteger(
						R.integer.default_thumbnail_height));
	}

	private void loadBitmap(ImageView imageView, String filePath, int width,
			int height) {
		if (cancelPotentialWork(filePath, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView,
					width, height);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					mContext.getResources(), mPlaceHolderBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(filePath);
		}
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

	public static boolean cancelPotentialWork(String data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.data;
			// If bitmapData is not yet set or it differs from the new data
			if (TextUtils.isEmpty(bitmapData) || !bitmapData.equals(data)) {
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

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private String data = "";
		private int mWidth;
		private int mHeight;

		public BitmapWorkerTask(ImageView imageView, int width, int height) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.mWidth = width;
			this.mHeight = height;
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];
			return decodeSampledBitmapFromResource(mContext.getResources(),
					data, mWidth, mHeight);
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
			String filePath, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
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

		loadBitmap(viewHolder.view, filePath);
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
