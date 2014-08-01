package com.campus.index;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.R;
import com.campus.domain.TradeInfo;
import com.campus.utils.CommonUtil;
import com.campus.utils.Constant;
import com.campus.utils.HttpCallback;
import com.campus.utils.HttpRequestUtil;
import com.campus.utils.MD5Util;
import com.campus.widgets.AutoLoadListView;
import com.campus.widgets.AutoLoadScrollListener;

public class IndexActivity extends Activity {

	private GridView gridView;
	private MyGridViewAdapter gridViewAdapter;
	private AutoLoadListView autoLoadView;
	private IndexActListViewAdapter adapter;
	private String Tag = IndexActivity.class.getSimpleName();
	private TextView curSort;
	private int curPageIndex = -1;
	private static int pageSize = 15;
	private TextView textEmptyData;
	private View footView;
	private int curState = -1;
	private LayoutInflater layoutInflate;

	private static final int STATE_EMPTY = 1;
	private static final int STATE_REQUEST_FAILED = 2;
	private static final int STATE_REQUEST_SUS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_index);
		System.out.println("currentThreadId:"
				+ Thread.currentThread().getName());
		curPageIndex = 1;
		initView();
		requestGoodList();
	}

	private void initView() {

		layoutInflate = LayoutInflater.from(this);
		textEmptyData = (TextView) findViewById(R.id.text_empty_data);
		textEmptyData.setVisibility(View.GONE);

		gridView = (GridView) findViewById(R.id.gridview);
		gridViewAdapter = new MyGridViewAdapter(this);
		gridView.setAdapter(gridViewAdapter);

		curSort = (TextView) findViewById(R.id.curSort);
		curSort.setTag(1);

		autoLoadView = (AutoLoadListView) findViewById(R.id.autoListView);
		footView = this.getLayoutInflater().inflate(
				R.layout.view_autoload_footview, null);
		autoLoadView.addFooterView(footView);
		adapter = new IndexActListViewAdapter(this);
		autoLoadView.setAdapter(adapter);
		autoLoadView.setAutoLoadListenner(listenner);
		listenner.setLoading(true);
		autoLoadView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("item click" + position);

			}
		});

	}

	private void requestGoodList() {
		textEmptyData.setVisibility(View.INVISIBLE);
		long time = System.currentTimeMillis();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", Constant.ACTION_GET_GOODS));
		params.add(new BasicNameValuePair("time", time + ""));
		params.add(new BasicNameValuePair("category", "0"));
		params.add(new BasicNameValuePair("filter", curSort.getTag() + ""));
		params.add(new BasicNameValuePair("pageindex", curPageIndex + ""));
		params.add(new BasicNameValuePair("pagesize", pageSize + ""));
		params.add(new BasicNameValuePair(
				"sign",
				MD5Util.generateMd5((Constant.ACTION_GET_GOODS
						+ CommonUtil.getSpfString("accountid") + time + Constant.KEY)
						.getBytes())));
		HttpRequestUtil.httpClientPost(Constant.baseUrl, params,
				new HttpCallback() {

					@Override
					public void onSuccess(String msg) {
						// TODO Auto-generated method stub
						Log.e(Tag, "requestGoodList list:onsuccess:" + msg);
						try {
							JSONObject jObject = new JSONObject(msg);
							if (jObject.getInt("state") == 0) {
								JSONArray jarray = jObject.getJSONArray("data");
								if (jarray.length() == 0) {
									curState = STATE_EMPTY;
									changeFootViewByState(null);
								} else {
									List<TradeInfo> tradeInfos = new ArrayList<>();
									for (int i = 0; i < jarray.length(); ++i) {
										TradeInfo tradeInfo = new TradeInfo();
										JSONObject single = jarray
												.getJSONObject(i);
										int id = single.getInt("ID");
										String name = single.getString("Name");
										String title = single
												.getString("Title");
										String category = single
												.getString("CategoryName");
										String newRate = single
												.getString("NewRate");
										double price = single
												.getDouble("Price");
										String publisher = single
												.getString("Publisher");
										String publisherName = single
												.getString("PublisherName");
										String tradePlace = single
												.getString("TradePlace");
										String goodsImage = single
												.getString("GoodsImage");
										int praiseCount = single
												.getInt("PraiseCount");
										int commentCount = single
												.getInt("CommentCount");
										Calendar cal = Calendar.getInstance();
										cal.setTimeInMillis(single
												.getLong("CreateDate"));
										String createDate = cal
												.get(Calendar.MONTH)
												+ "月"
												+ cal.get(Calendar.DAY_OF_MONTH)
												+ "日";
										tradeInfo.setId(id);
										tradeInfo.setName(name);
										tradeInfo.setTitle(title);
										tradeInfo.setCategory(category);
										tradeInfo.setNewRate(newRate);
										tradeInfo.setPrice((float) price);
										tradeInfo.setPublisher(publisher);
										tradeInfo.setPublishName(publisherName);
										tradeInfo.setPraiseCount(praiseCount);
										tradeInfo.setCommentCount(commentCount);
										tradeInfo.setImage(goodsImage);
										tradeInfo.setPublishDate(createDate);
										tradeInfo.setTradePlace(tradePlace);
										tradeInfos.add(tradeInfo);
									}
									curState = STATE_REQUEST_SUS;
									changeFootViewByState(tradeInfos);
								}
							} else {
								curState = STATE_REQUEST_FAILED;
								changeFootViewByState(null);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							curState = STATE_REQUEST_FAILED;
							changeFootViewByState(null);
						}
					}

					@Override
					public void onFailure(int type, String msg) {
						// TODO Auto-generated method stub
						Log.e(Tag, "requestGoodList list:onFailure:" + msg
								+ "type:" + type);
						curState = STATE_REQUEST_FAILED;
						changeFootViewByState(null);
					}
				});
	}

	protected void changeFootViewByState(final List<TradeInfo> tradeInfos) {
		// TODO Auto-generated method stub
		System.out.println("changeFootViewByState1 currentThreadId:"
				+ Thread.currentThread().getName());
		IndexActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("changeFootViewByState2 currentThreadId:"
						+ Thread.currentThread().getName());
				switch (curState) {
				case STATE_EMPTY:
					textEmptyData.setText("还没有交易数据~");
					break;
				case STATE_REQUEST_FAILED:
					textEmptyData.setText("查询失败~");
					break;
				case STATE_REQUEST_SUS:
					textEmptyData.setText("");
					break;
				default:
					break;
				}
				if (adapter.getTradeInfos().size() == 0)
					textEmptyData.setVisibility(View.VISIBLE);
				autoLoadView.removeFooterView(footView);
				adapter.setTradeInfos(tradeInfos);
				adapter.notifyDataSetChanged();
				listenner.setLoading(false);
				if (tradeInfos != null && tradeInfos.size() > 0)
					curPageIndex++;
			}
		});

	}

	private void requestCategoryList() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		long time = System.currentTimeMillis();
		params.add(new BasicNameValuePair("action", "6"));
		params.add(new BasicNameValuePair("time", time + ""));
		params.add(new BasicNameValuePair("sign", MD5Util.generateMd5(("6"
				+ CommonUtil.getSpfString("accountid") + time + Constant.KEY)
				.getBytes())));
		HttpRequestUtil.httpClientPost(Constant.baseUrl, params,
				new HttpCallback() {

					@Override
					public void onSuccess(String msg) {
						// TODO Auto-generated method stub
						Log.e(Tag, "requestcategory list:onsuccess:" + msg);
					}

					@Override
					public void onFailure(int type, String msg) {
						// TODO Auto-generated method stub
						Log.e(Tag, "requestcategory list:onFailure:" + msg
								+ "type:" + type);
					}
				});
	}

	private AutoLoadScrollListener listenner = new AutoLoadScrollListener() {

		@Override
		public void loadData() {
			// TODO Auto-generated method stub
			autoLoadView.addFooterView(footView);
			requestGoodList();
			this.setLoading(true);
		}
	};

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
