package com.example.saleem.testgithub.Gallery;

import android.net.Uri;

public class Image {

	public Uri mUri;
	public int mOrientation;
	public int isSend;

	public Image(Uri uri, int orientation, int issend) {
		mUri = uri;
		mOrientation = orientation;
		isSend = issend;
	}
}
