package com.example.feedreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.example.feedreader.FeedReaderXmlParser.Entry;

public class LoadXmlFromNetwork {

	private ArrayList<Entry> arrlist;
	Context myContext;

	public ArrayList<Entry> loadXmlFromNetwork(String urlString, Context context)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		// Instantiate the parser
		myContext = context;
		FeedReaderXmlParser feedReaderXmlParser = new FeedReaderXmlParser();
		List<Entry> entries = null;
		try {
			stream = new DownloadUrl().downloadUrl(urlString); // gets the
																// InputStream
																// handler for
																// the given URL
			entries = feedReaderXmlParser.parse(stream);// gets the useful tags
														// from XML, each entry
														// corresponding to one
														// news(feed) item
			// Close the InputStream after using it
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		// Appending all the feeds in a html text to view it in webview
		arrlist = new ArrayList<Entry>();
		for (Entry entry : entries) {
			arrlist.add(entry);
		}
		return arrlist;
	}
}
