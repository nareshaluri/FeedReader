package com.example.feedreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
 
public class WebViewPage extends Activity {
	WebView web;
	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Hide Title Bar	- This Should be done before SetContentView
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.webviewpage);
		// Inflation
		
		web = (WebView) findViewById(R.id.webview);
		web.setWebViewClient(new WebViewClient() {

			// This method will be triggered when the Page Started Loading

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				/*dialog = ProgressDialog.show(WebViewPage.this, null,
						"Please Wait...Page is Loading...");
				dialog.setCancelable(true);*/
				super.onPageStarted(view, url, favicon);
			}

			// This method will be triggered when the Page loading is completed

			public void onPageFinished(WebView view, String url) {
				//dialog.dismiss();
				super.onPageFinished(view, url);
			}

			// This method will be triggered when error page appear

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				//dialog.dismiss();
				// You can redirect to your own page instead getting the default
				// error page
				Toast.makeText(WebViewPage.this,
						"The Requested Page Does Not Exist", Toast.LENGTH_LONG).show();
				web.loadUrl(getIntent().getStringExtra("url"));
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		web.loadUrl(getIntent().getStringExtra("url"));
		web.getSettings().setLoadWithOverviewMode(true);
		web.getSettings().setUseWideViewPort(true);
	}
}