package com.jmarkstar.turisthelper.extensions;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jmarkstar.turisthelper.R;
import com.jmarkstar.turisthelper.helpers.HttpClientHelper;
import com.jmarkstar.turisthelper.models.Session;
import com.jmarkstar.turisthelper.models.AccessToken;
import com.jmarkstar.turisthelper.utils.Constant;
import com.jmarkstar.turisthelper.utils.LogUtils;

import java.util.UUID;

import io.realm.Realm;

/** Dialog to login.
 * */
public class FoursquareDialog extends Dialog {

	private static final String TAG = "FoursquareDialog";

    private String mUrl;
    private FoursquareDialogListener mFoursquareDialogListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;
	static final int MARGIN = 4;
	static final int PADDING = 2;

	public FoursquareDialog(Context context, FoursquareDialogListener listener) {
		super(context);
		mUrl = Constant.AUTH_BASE + "/authenticate?response_type=code" + "&client_id=" + Constant.CLIENT_ID + "&redirect_uri=" + Constant.MY_REGISTERED_REDIRECT_URI;
		mFoursquareDialogListener = listener;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage(getContext().getString(R.string.foursquare_dialog_loading));
		loadingContentDialog();
		clearCookies();
    }

	@SuppressWarnings("deprecation")
	private void clearCookies() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			CookieManager.getInstance().removeAllCookies(null);
			CookieManager.getInstance().flush();
		} else {
			CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(getContext());
			cookieSyncMngr.startSync();
			CookieManager cookieManager=CookieManager.getInstance();
			cookieManager.removeAllCookie();
			cookieManager.removeSessionCookie();
			cookieSyncMngr.stopSync();
			cookieSyncMngr.sync();
		}
	}

	/** Loads the foursquare page.
	 * */
	private void loadingContentDialog(){

		final float[] DIMENSIONS_LANDSCAPE = {460, 260};
		final float[] DIMENSIONS_PORTRAIT = {280, 420};

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);

		setupTitle();
		setUpWebView();

		Display display 	= getWindow().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		final float scale 	= getContext().getResources().getDisplayMetrics().density;
		float[] dimensions 	= (size.x < size.y) ? DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;

		addContentView(mContent, new FrameLayout.LayoutParams((int) (dimensions[0] * scale + 0.5f),
				(int) (dimensions[1] * scale + 0.5f)));
	}

	/** Prepares the title for the dialog.
	 * */
	private void setupTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Drawable icon = ContextCompat.getDrawable(getContext(), R.mipmap.ic_foursquare);
		mTitle = new TextView(getContext());
		mTitle.setText(R.string.foursquare_dialog_title);
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		mContent.addView(mTitle);
	}

	/** prepare the webview and loads the page.
	 * */
	private void setUpWebView() {
		final FrameLayout.LayoutParams MATCH = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new FoursquareWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(MATCH);
		mContent.addView(mWebView);
	}

	private class FoursquareWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtils.info(TAG, "Redirecting URL " + url);
			if (url.startsWith(Constant.MY_REGISTERED_REDIRECT_URI)) {
				String urls[] = url.split(Constant.EQUALS);

				final String code =  urls[1];
				LogUtils.info(TAG, "CODE = " +code);

				//IMPORTANT:
				//I have create a HttpClientHelper using the Builder Pattern to use it with this Asynctask
				//because I had problems with Retrofit 2.
				//I couldnt send my foursquare code because It has a '#' character and Retrofit 2 encode it
				// I think It is a bug of the library.
				//https://github.com/square/retrofit/issues/1407
				//https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Query.html
				new AsyncTask<Void, Void, AccessToken>() {

					@Override
					protected AccessToken doInBackground(Void... voids) {
						AccessToken accessTokenResponse = null;
						try{
							HttpClientHelper httpClientHelper = new HttpClientHelper.Builder(Constant.AUTH_BASE)
								.method(HttpClientHelper.Method.GET)
								.resource("access_token")
								.query("grant_type", Constant.GRANT_TYPE_ACCESS_TOKEN)
								.query("client_id", Constant.CLIENT_ID)
								.query("client_secret", Constant.CLIENT_SECRET)
								.query("redirect_uri", Constant.MY_REGISTERED_REDIRECT_URI)
								.query("code", code)
								.create();

							String response = httpClientHelper.getResponse();

							LogUtils.info(TAG, "request url = "+httpClientHelper.getURL());
							LogUtils.info(TAG, "response code = "+httpClientHelper.getResponseCode());
							LogUtils.info(TAG, "response body= "+response);

							accessTokenResponse = new Gson().fromJson(response, AccessToken.class);
						}catch (Exception e){
							LogUtils.error(TAG, e.getMessage());
						}
						return accessTokenResponse;
					}

					@Override
					protected void onPostExecute(AccessToken accessTokenResponse) {
						super.onPostExecute(accessTokenResponse);
						if(accessTokenResponse != null){
							if(accessTokenResponse.getAccessToken() != null){
								String accessToken = accessTokenResponse.getAccessToken();
								LogUtils.info(TAG, "access Token = " +accessToken);

								//I'm setting the token befoe than the user information
								//because I need the token to generalize my retrofit objetc
								//and set it the token, because I need thee token to get the user info.
								Realm realm = Realm.getDefaultInstance();
								realm.beginTransaction();
								Session session = realm.createObject(Session.class);
								session.setId(UUID.randomUUID().toString());
								session.setToken(accessToken);
								realm.commitTransaction();
								realm.close();

								mFoursquareDialogListener.onComplete(accessToken);
							}else{
								LogUtils.info(TAG, "ERROR " );
								mFoursquareDialogListener.onError(accessTokenResponse.getError());
							}
						}else{
							mFoursquareDialogListener.onError("Error");
						}
						FoursquareDialog.this.dismiss();
					}
				}.execute();
				return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			LogUtils.info(TAG, "Page error: " + description);
			super.onReceivedError(view, errorCode, description, failingUrl);
			mFoursquareDialogListener.onError(description);
			FoursquareDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			LogUtils.info(TAG, "Loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				mTitle.setText(title);
			}
			mSpinner.dismiss();
		}

	}
	    
	public interface FoursquareDialogListener {
		void onComplete(String accessToken);
		void onError(String error);
	}
}