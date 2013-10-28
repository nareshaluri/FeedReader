package com.example.feedreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class FeedReaderXmlParser {
	private static final String ns = null;// namespace

	// Initiating the parsing, calls readFeed() to process the feeds
	public List parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		}
		// Close the stream after using it
		finally {
			in.close();
		}
	}

	// Processes the feed tag, calls readEntry() upon encountering any entry tag
	// inside feed tag
	private List readFeed(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		List entries = new ArrayList();
		parser.require(XmlPullParser.START_TAG, ns, "feed");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("entry")) {
				entries.add(readEntry(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	public static class Entry {
		public final String title;
		public final String link;
		public final String summary;

		private Entry(String title, String summary, String link) {
			this.title = title;
			this.summary = summary;
			this.link = link;
		}
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// feedburner:origLink tag, and return them back
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Entry readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "entry");
		String title = null;
		String summary = null;
		String link = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = readTitle(parser);
			} else if (name.equals("content")) {
				summary = readSummary(parser);
			} else if (name.equals("feedburner:origLink")) {
				link = readLink(parser);
			} else {
				skip(parser);
			}
		}
		return new Entry(title, summary, link);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	// Processes link tags in the feed.
	private String readLink(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String link = "";
		parser.require(XmlPullParser.START_TAG, ns, "feedburner:origLink");
		link = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "feedburner:origLink");
		return link;
	}

	// Processes summary tags in the feed.
	private String readSummary(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "content");
		String summary = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "content");
		return summary;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	// Skips the tags which are of no use
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}