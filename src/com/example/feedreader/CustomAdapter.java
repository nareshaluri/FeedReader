package com.example.feedreader;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.feedreader.FeedReaderXmlParser.Entry;

// setting up the listview for feeds
class CustomAdapter extends ArrayAdapter<Entry> {
	private List<Entry> rssFeedLst;
	Context context;
	Entry e;

	public CustomAdapter(Context context, int textViewResourceId,
		List<Entry> rssFeedLst) {
		super(context, textViewResourceId, rssFeedLst);
		this.rssFeedLst = rssFeedLst;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		RssHolder rssHolder = null;
		if (convertView == null) {
			view = View.inflate(context, R.layout.rss_list_item, null);
			rssHolder = new RssHolder();
			rssHolder.rssTitleView = (TextView) view
					.findViewById(R.id.rss_title_view);
			view.setTag(rssHolder);
		} else {
			rssHolder = (RssHolder) view.getTag();
		}
		e = rssFeedLst.get(position);
		rssHolder.rssTitleView.setText(e.title);
		return view;
	}

	static class RssHolder {
		public TextView rssTitleView;
	}
}