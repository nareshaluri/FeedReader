package com.example.feedreader;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.feedreader.FeedReaderXmlParser.Entry;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView listivew;
	private static final String URL = "YOUR URL TO FEEDS";
	private ArrayList<com.example.feedreader.FeedReaderXmlParser.Entry> aa;

	private GetXMLforURL getXMLForUrl;

	CustomAdapter mRssAdap;

	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listivew = (ListView) findViewById(R.id.listView_LL);
		aa = new ArrayList<com.example.feedreader.FeedReaderXmlParser.Entry>();
	}

	private void processing() {

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);// Connectivity
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		// Checking for network connectivity
		if (networkInfo != null && networkInfo.isConnected()) {
			try {
				getXMLForUrl = new GetXMLforURL();
				aa = getXMLForUrl.execute(URL).get();
				mRssAdap = new CustomAdapter(MainActivity.this,
						R.layout.rss_list_item, aa);
				listivew.setAdapter(mRssAdap);
				listivew.setOnItemClickListener(this);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			alert.showAlertDialog(MainActivity.this, "NetWork Connection..",
					"Please check Internet Connection", false);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent in = new Intent(this, WebViewPage.class);
		in.putExtra("url", aa.get(arg2).link);
		startActivity(in);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		processing();
	}

	class GetXMLforURL extends AsyncTask<String, Void, ArrayList<Entry>> {

		private ProgressDialog progress;
		public LoadXmlFromNetwork loadxmlNetWork;

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(MainActivity.this);
			progress.show();
			progress.setMessage("Please Wait...");
		}

		@Override
		protected ArrayList<Entry> doInBackground(String... urls) {
			try {
				loadxmlNetWork = new LoadXmlFromNetwork();
				return loadxmlNetWork.loadXmlFromNetwork(urls[0],
						MainActivity.this);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(ArrayList<Entry> result) {

			progress.dismiss();
		}
	}
}
