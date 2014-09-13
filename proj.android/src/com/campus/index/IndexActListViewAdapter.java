package com.campus.index;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.R;
import com.campus.domain.TradeInfo;

public class IndexActListViewAdapter extends BaseAdapter {

	private List<TradeInfo> tradeInfos;

	private LayoutInflater layoutInflate;
	private Context mContext;

	public IndexActListViewAdapter(Context context) {
		this.mContext = context;
		layoutInflate = LayoutInflater.from(mContext);
		tradeInfos = new ArrayList<TradeInfo>();
	}

	public void setTradeInfos(List<TradeInfo> tradeInfos) {
		if (tradeInfos != null && tradeInfos.size() > 0)
			this.tradeInfos.addAll(tradeInfos);

	}

	public List<TradeInfo> getTradeInfos() {
		return this.tradeInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (tradeInfos != null)
			count = tradeInfos.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tradeInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		TradeInfo tradeInfo = tradeInfos.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflate.inflate(R.layout.view_list_item, null);
			holder.publishDate = (TextView) convertView
					.findViewById(R.id.publishdate);
			holder.newreate = (TextView) convertView.findViewById(R.id.newrate);
			holder.place = (TextView) convertView.findViewById(R.id.tradeplace);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.goodsImage = (ImageView) convertView
					.findViewById(R.id.goodsImage);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		// holder.goodsImage.setImageResource(R.drawable.default_goodsimage);
		holder.newreate.setText(tradeInfo.getNewRate());
		holder.place.setText(tradeInfo.getTradePlace());
		holder.price.setText(tradeInfo.getPrice() + "元");
		holder.publishDate.setText(tradeInfo.getPublishDate());
		holder.title.setText(tradeInfo.getTitle());
		return convertView;
	}

	class ViewHolder {

		ImageView goodsImage;
		TextView title;
		TextView price;
		TextView newreate;
		TextView place;
		TextView publishDate;
	}
}