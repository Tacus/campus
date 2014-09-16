package com.campus.utils;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.campus.R;

public class BitmapWorkerTask extends AsyncTask<String, Integer, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private String data = "";
	private int mWidth;
	private int mHeight;
	private Context mContext;
	private static Bitmap mPlaceHolderBitmap;

	public BitmapWorkerTask(Context context, ImageView imageView, int width,
			int height) {
		// Use a WeakReference to ensure the ImageView can be garbage
		// collected
		mContext = context;
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.mWidth = width;
		this.mHeight = height;
	}

	// Decode image in background.
	@Override
	protected Bitmap doInBackground(String... params) {
		data = params[0];
		return CommonUtil.decodeSampledBitmapFromResource(
				mContext.getResources(), data, mWidth, mHeight);
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

	public static void loadBitmap(Context context, ImageView imageView,
			String filePath) {
		loadBitmap(context, imageView, filePath, context.getResources()
				.getInteger(R.integer.defautl_thumbnail_width), context
				.getResources().getInteger(R.integer.default_thumbnail_height));
	}

	public static void loadBitmap(Context context, ImageView imageView,
			String filePath, int width, int height) {
		if (cancelPotentialWork(filePath, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(context,
					imageView, width, height);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					context.getResources(), mPlaceHolderBitmap, task);
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

	private static boolean cancelPotentialWork(String data, ImageView imageView) {
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

}
