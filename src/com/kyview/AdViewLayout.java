package com.kyview;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kyview.AdViewTargeting.SwitcherMode;
import com.kyview.adapters.AdViewAdapter;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewReqManager;
import com.kyview.util.AdViewUtil;
import com.kyview.util.MD5;

//import com.kyview.AdViewTargeting.Channel;

public class AdViewLayout extends RelativeLayout {
	public final WeakReference<Activity> activityReference;

	public final Handler handler;

	public static ScheduledExecutorService scheduler;
	public static boolean isTest;
	public boolean isTerminated;
	public String keyAdView;
	public static String appName;
	public static String keyDev = new String("000000000000000");
	public static String servicePro = new String("46000");
	public String typeDev = new String("SDK");
	public String osVer = new String("2.1-update1");
	public String resolution = new String("320*480");
	public String netType = new String("2G/3G");
	public String channel = new String("unknown");
	public String platform = new String("android");
	public static String bundle = "";
	public static int appVersion;

	public Extra extra;
	private int imageWidth = 75;
	private int imageHeight = 75;
	public static boolean isadFill = false;
	public RelativeLayout umengView = null;
	public static int refreashTime = 15 * 1000;
	private boolean isClosed = false;
	private boolean isStoped = false;

	// public RelativeLayout baiduView=null;
	public WeakReference<RelativeLayout> superViewReference;

	public Ration activeRation;
	public Ration nextRation;

	public double mDensity = 0D;

	public AdViewInterface adViewInterface;

	public AdViewManager adViewManager;

	private boolean hasWindow;
	private boolean isScheduled;

	private static String mDefaultChannel[] = { "EOE", "GOOGLEMARKET",
			"APPCHINA", "HIAPK", "GFAN", "GOAPK", "NDUOA", "91Store", "LIQUCN",
			"ANDROIDAI", "ANDROIDD", "YINGYONGSO", "IMOBILE", "MUMAYI",
			"PAOJIAO", "AIBALA", "COOLAPK", "ANFONE", "APKOK", "360MARKET",
			"OTHER" };

	private int maxWidth;

	public boolean isClosed() {
		return isClosed;
	}

	// 关闭广告
	private void closedAd() {
		this.removeAllViews();
		isTerminated = true;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
		this.isStoped = false;
		if (isClosed)
			closedAd();
	}

	public void setMaxWidth(int width) {
		maxWidth = width;
	}

	private int maxHeight;

	public void setMaxHeight(int height) {
		maxHeight = height;
	}

	public AdViewLayout(final Activity context, final String keyAdView) {
		super(context);
		this.activityReference = new WeakReference<Activity>(context);
		this.superViewReference = new WeakReference<RelativeLayout>(this);

		this.keyAdView = keyAdView;

		this.hasWindow = true;
		this.isTerminated = false;
		handler = new Handler();

		scheduler = Executors.newScheduledThreadPool(1);
		this.isScheduled = true;

		scheduler.schedule(new InitRunnable(this, keyAdView), 0,
				TimeUnit.SECONDS);

		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
		getAppInfo(context);
		// appReport();
		this.maxWidth = 0;
		this.maxHeight = 0;
	}

	public AdViewLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		String key = getAdViewSDKKey(context);
		if (key == null) {
			key = "";
		}
		Activity activity = (Activity) context;
		this.activityReference = new WeakReference<Activity>(activity);
		this.superViewReference = new WeakReference<RelativeLayout>(this);
		this.keyAdView = key;
		this.hasWindow = true;
		this.isTerminated = false;
		handler = new Handler();
		scheduler = Executors.newScheduledThreadPool(1);
		this.isScheduled = true;
		scheduler.schedule(new InitRunnable(this, keyAdView), 0,
				TimeUnit.SECONDS);
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
		getAppInfo(context);
		// appReport();
		this.maxWidth = 0;
		this.maxHeight = 0;

	}

	public void addCloseButton(AdViewLayout adViewLayout) {
		if (AdViewTargeting.getSwitcherMode() != SwitcherMode.CANCLOSED)
			return;
		String resoursePath = null;

		if (0D == mDensity) {
			DisplayMetrics dm = new DisplayMetrics();
			activityReference.get().getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			mDensity = dm.density;
		}
		resoursePath = "/com/kyview/assets/close_75.png";
		if (mDensity > 1.6) {
			resoursePath = "/com/kyview/assets/close_160.png";
			imageWidth = 160;
			imageHeight = 160;
		} else if (mDensity > 2.0) {
			resoursePath = "/com/kyview/assets/close_224.png";
			imageWidth = 224;
			imageHeight = 224;
		}
		ImageView closeButton = new ImageView(adViewLayout.getContext());
		closeButton.setClickable(true);
		BitmapDrawable btnClose = new BitmapDrawable(getClass()
				.getResourceAsStream(resoursePath));
		closeButton.setBackgroundDrawable(btnClose);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		adViewLayout.addView(closeButton, lp);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (adViewInterface != null) {
					adViewInterface.onClosedAd();
					isStoped = true;
				}

			}
		});
	}

	private String getAdViewSDKKey(Context ctx) {
		String packageName = ctx.getPackageName();
		String activityName = ctx.getClass().getName();
		PackageManager packageManager = ctx.getPackageManager();
		Bundle bd = null;
		String key = "";
		try {
			ActivityInfo info = packageManager.getActivityInfo(
					new ComponentName(packageName, activityName),
					PackageManager.GET_META_DATA);
			bd = info.metaData;
			if (bd != null) {
				key = bd.getString("ADVIEW_SDK_KEY");
			}
		} catch (PackageManager.NameNotFoundException exception) {
		}
		try {
			ApplicationInfo info = packageManager.getApplicationInfo(
					packageName, PackageManager.GET_META_DATA);
			bd = info.metaData;
			if (bd != null) {
				key = bd.getString("ADVIEW_SDK_KEY");
			}
		} catch (PackageManager.NameNotFoundException exception) {
		}
		return key;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (maxWidth > 0 && widthSize > maxWidth) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth,
					MeasureSpec.AT_MOST);
		}

		if (maxHeight > 0 && heightSize > maxHeight) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
					MeasureSpec.AT_MOST);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void changeVisibility(int visibility) {
		if (visibility == VISIBLE) {
			AdViewReqManager.getInstance(getContext()).loadPendingReqInfos(
					getContext());
			this.hasWindow = true;
			if (!this.isScheduled) {
				this.isScheduled = true;
				if (this.extra != null) {
					rotateThreadedPri(0);
					if (null != adViewManager
							&& adViewManager.needUpdateConfig())
						fetchConfigThreadedDelayed(10);
				} else
					scheduler.schedule(new InitRunnable(this, keyAdView), 0,
							TimeUnit.SECONDS);
			}
		} else if (visibility == 8) {
			AdViewReqManager.getInstance(getContext()).savePendingReqInfos(
					getContext());
			this.hasWindow = false;
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		changeVisibility(visibility);
	}

	@Override
	public void setVisibility(int visibility) {
		changeVisibility(visibility);
		super.setVisibility(visibility);
	}

	public void rotateAd() {
		if (isTerminated)
			return;
		if (!this.hasWindow) {
			this.isScheduled = false;
			return;
		}
		AdViewUtil.logInfo("Rotating Ad");
		nextRation = adViewManager.getRation();
		handler.post(new HandleAdRunnable(this));
	}

	public void rotatePriAd() {
		if (isTerminated)
			return;
		if (!this.hasWindow) {
			this.isScheduled = false;
			return;
		}
		AdViewUtil.logInfo("Rotating Pri Ad");
		nextRation = adViewManager.getRollover();// adViewManager.getRollover_pri();
		handler.post(new HandleAdRunnable(this));
	}

	public static boolean isConnectInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (ni != null)
			return ni.isAvailable();
		else
			return false;
	}

	private boolean isScreenLocked() {
		KeyguardManager mKeyguardManager = (KeyguardManager) getContext()
				.getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode() ? true : false;

	}

	// Initialize the proper ad view from nextRation
	private void handleAd() {
		// We shouldn't ever get to a state where nextRation is null unless all
		// networks fail
		if (nextRation == null) {
			AdViewUtil.logInfo("nextRation is null!");
			rotateThreadedDelayed();
			// rotateThreadedPri(extra.cycleTime);
			return;
		}
		if (isScreenLocked()) {
			AdViewUtil.logInfo("screen is locked");
			rotateThreadedPri(5);
			// scheduler.schedule(new RotateAdRunnable(this), 5,
			// TimeUnit.SECONDS);
			return;
		}
		if (isStoped) {
			// 停止请求
			AdViewUtil.logInfo("stop required");
			rotateThreadedPri(5);
			// scheduler.schedule(new RotateAdRunnable(this), 5,
			// TimeUnit.SECONDS);
			return;
		}

		if (isConnectInternet(this.getContext()) == false) {
			AdViewUtil.logInfo("network is unavailable");
			rotateThreadedPri(5);
			// scheduler.schedule(new RotateAdRunnable(this), 5,
			// TimeUnit.SECONDS);
			return;
		}
		AdViewUtil.logInfo("Showing ad:\nname: " + nextRation.name);

		try {
			AdViewAdapter.handleOne(this, nextRation);
		} catch (Throwable t) {
			AdViewUtil.logWarn("Caught an exception in adapter:", t);
			rollover();
			return;
		}
	}

	public void rotateThreadedPri(int seconds) {
		scheduler.schedule(new RotatePriAdRunnable(this), seconds,
				TimeUnit.SECONDS);
	}

	// Rotate in extra.cycleTime seconds
	public void rotateThreadedDelayed() {
		AdViewUtil.logInfo("Will call rotateAd() in " + extra.cycleTime
				+ " seconds");
		scheduler.schedule(new RotateAdRunnable(this), extra.cycleTime,
				TimeUnit.SECONDS);

	}

	// Fetch config from server after defined time
	public void fetchConfigThreadedDelayed(int seconds) {
		scheduler.schedule(new GetConfigRunnable(this), seconds,
				TimeUnit.SECONDS);
	}

	// Remove old views and push the new one
	public void pushSubView(ViewGroup subView) {
		RelativeLayout superView = superViewReference.get();
		if (superView == null) {
			return;
		}
		superView.removeAllViews();
		RelativeLayout.LayoutParams layoutParams;
		if (nextRation == null) {
			return;
		}
		if (nextRation.type == AdViewUtil.NETWORK_TYPE_ADWO
				|| nextRation.type == AdViewUtil.NETWORK_TYPE_MOBWIN)
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		else if (nextRation.type == AdViewUtil.NETWORK_TYPE_ADVIEWAD
				|| nextRation.type == AdViewUtil.NETWORK_TYPE_CASEE)
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		else
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		superView.addView(subView, layoutParams);
		if (AdViewTargeting.getSwitcherMode() == SwitcherMode.CANCLOSED)
			addCloseButton(this);
		AdViewUtil.logInfo("Added subview");
		this.activeRation = nextRation;
		countImpression();
	}

	public void pushSubViewForIzp(ViewGroup subView) {
		RelativeLayout superView = superViewReference.get();
		if (superView == null) {
			return;
		}

		superView.removeAllViews();
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				320, 48);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		superView.addView(subView, layoutParams);
		AdViewUtil.logInfo("Added subview");
		if (AdViewTargeting.getSwitcherMode() == SwitcherMode.CANCLOSED)
			addCloseButton(this);
		this.activeRation = nextRation;
		countImpression();
	}

	public void AddSubView(RelativeLayout subView) {
		RelativeLayout superView = superViewReference.get();
		if (superView == null) {
			return;
		}
		superView.removeAllViews();
		RelativeLayout.LayoutParams layoutParams;
		layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		superView.addView(subView, layoutParams);
		addCloseButton(this);
		this.activeRation = nextRation;
		// countImpression();
	}

	public void reportImpression() {
		this.activeRation = nextRation;
		countImpression();
	}

	// 目前这个函数已经没用了，百度的新版本流程变了，onshow不会被调用了
	// public void reportBaiduImpression() {
	// String url = String.format(AdViewUtil.urlImpression,
	// adViewManager.keyAdView, activeRation.nid,
	// AdViewUtil.NETWORK_TYPE_BAIDU, 0, "hello", appVersion,
	// adViewManager.mSimulator, keyDev, AdViewUtil.currentSecond(),
	// AdViewUtil.VERSION, adViewManager.configVer);
	// PingUrlRunnable pingUrlRunnable = new PingUrlRunnable(url);
	// scheduler.schedule(pingUrlRunnable, 0, TimeUnit.SECONDS);
	// pingUrlRunnable = null;
	// if (adViewInterface != null)
	// adViewInterface.onDisplayAd();
	// }

	public void reportClick() {
		countClick();
	}

	public void rollover() {
		nextRation = adViewManager.getRollover();
		handler.post(new HandleAdRunnable(this));
	}

	private void countImpression() {
		// AdViewUtil.storeInfo(activityReference.get(),
		// adViewManager.keyAdView, activeRation.type, "show");
		if (activeRation != null) {
			String url = String.format(AdViewUtil.urlImpression,
					adViewManager.keyAdView, activeRation.nid,
					activeRation.type, 0, "hello", appVersion,
					adViewManager.mSimulator, keyDev,
					AdViewUtil.currentSecond(), AdViewUtil.VERSION,
					AdViewUtil.configVer);
			scheduler.schedule(new PingUrlRunnable(url), 0, TimeUnit.SECONDS);
			AdViewReqManager.getInstance(this.getContext()).storeInfo(this,
					activeRation.type, AdViewUtil.COUNTSHOW);
			if (activeRation.type != 0)
				AdViewUtil.common_count += 1;

			if (adViewInterface != null)
				adViewInterface.onDisplayAd();
			// AdViewUtil.logInfo(url);
		}
	}

	private void countClick() {
		if (activeRation != null) {
			String url = String.format(AdViewUtil.urlClick,
					adViewManager.keyAdView, activeRation.nid,
					activeRation.type, 0, "hello", appVersion,
					adViewManager.mSimulator, keyDev,
					AdViewUtil.currentSecond(), AdViewUtil.VERSION,
					AdViewUtil.configVer);
			scheduler.schedule(new PingUrlRunnable(url), 0, TimeUnit.SECONDS);
			AdViewReqManager.getInstance(activityReference.get()).storeInfo(
					this, activeRation.type, AdViewUtil.COUNTCLICK);

			// AdViewUtil.logInfo(url);
			if (adViewInterface != null)
				adViewInterface.onClickAd();
		}
	}

	public void appReport() {
		String url = String.format(AdViewUtil.appReport, keyAdView, keyDev,
				typeDev, osVer, resolution, servicePro, netType, channel,
				platform, AdViewUtil.currentSecond(), AdViewUtil.VERSION,
				AdViewUtil.configVer);
		// AdViewUtil.logInfo(url);
		scheduler.schedule(new PingUrlRunnable(url), 0, TimeUnit.SECONDS);
	}

	// public void kyAdviewReport(String url, String content) {
	//
	// scheduler.schedule(new PingKyAdviewRunnable(url, content), 1,
	// TimeUnit.SECONDS);
	// }

	public static String getChannel(Context ctx) {
		String packageName = ctx.getPackageName();
		String activityName = ctx.getClass().getName();
		PackageManager packageManager = ctx.getPackageManager();
		Bundle bd = null;
		String key = "";
		try {
			ActivityInfo info = packageManager.getActivityInfo(
					new ComponentName(packageName, activityName),
					PackageManager.GET_META_DATA);
			bd = info.metaData;
			if (bd != null) {
				key = bd.getString("AdView_CHANNEL");
			}
		} catch (PackageManager.NameNotFoundException exception) {
		}
		try {
			ApplicationInfo info = packageManager.getApplicationInfo(
					packageName, PackageManager.GET_META_DATA);
			bd = info.metaData;
			if (bd != null) {
				key = bd.getString("AdView_CHANNEL");
			}
		} catch (PackageManager.NameNotFoundException exception) {
		}

		if (key == null || key.length() == 0)
			key = "OTHER";
		else {
			int i = 0;
			for (i = 0; i < mDefaultChannel.length; i++) {
				if (key.compareTo(mDefaultChannel[i]) == 0) {
					break;
				}
			}
			if (i == mDefaultChannel.length) {
				key = "OTHER";
			}
		}

		return key;
	}

	private void getAppInfo(Context context) {
		if (context == null) {
			return;
		}
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null)
			return;
		String devid = tm.getDeviceId();
		if (devid != null && devid.length() > 0) {
			keyDev = new String(devid);
		}
		typeDev = new String(Build.MODEL);
		typeDev = typeDev.replaceAll(" ", "");
		osVer = new String(Build.VERSION.RELEASE);
		osVer = osVer.replaceAll(" ", "");
		DisplayMetrics dm = new DisplayMetrics();
		activityReference.get().getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		mDensity = dm.density;
		resolution = new String(Integer.toString(dm.widthPixels) + "*"
				+ Integer.toString(dm.heightPixels));

		if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
			String service = tm.getSimOperator();
			if (service != null && service.length() > 0) {
				servicePro = new String(service);
			}
		}

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			String networkType = ni.getTypeName();
			if (networkType.equalsIgnoreCase("mobile")) {
				netType = new String("2G/3G");
			} else if (networkType.equalsIgnoreCase("wifi")) {
				netType = new String("Wi-Fi");
			} else {
				netType = new String("Wi-Fi");
			}
		} else {
			netType = new String("Wi-Fi");
		}
		bundle = context.getPackageName();
		channel = getChannel(context);
		appVersion = getAppVersion(context);
		appName = getAppName(context);
	}

	public String getTokenMd5(long time) {
		return MD5.MD5Encode(keyAdView + "0" + keyDev + AdViewUtil.configVer
				+ time);
	}

	public static int getAppVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;// packInfo.versionName;
	}

	private String getAppName(Context context) {
		String appName = null;
		PackageInfo packInfo = null;
		try {
			packInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appName = packInfo.applicationInfo.loadLabel(
					context.getPackageManager()).toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			appName=URLEncoder.encode(appName,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			appName="";
		}
		
		return appName;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean closeAble = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			closeAble = iscloseBtn(event);
			// AdViewUtil.logInfo("Intercepted ACTION_DOWN event");
			if (activeRation != null) {

				if (activeRation.type == AdViewUtil.NETWORK_TYPE_BAIDU
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_WIYUN
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_DOMOB
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_SMARTAD
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_ADSAGE
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_ADVIEWAD
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_PUNCHBOX)
					return false;

				AdViewUtil
						.logInfo("Intercepted ACTION_DOWN event 2, activeRation.type="
								+ activeRation.type);
				if (activeRation.type == AdViewUtil.NETWORK_TYPE_SUIZONG
						|| activeRation.type == AdViewUtil.NETWORK_TYPE_ADFILL)// ||
				// activeRation.type
				// ==
				// AdViewUtil.NETWORK_TYPE_INMOBI)
				{
					try {
						if (AdViewTargeting.getSwitcherMode() == SwitcherMode.DEFAULT
								|| !closeAble) {
							AdViewAdapter.onClickAd(isMissTouch(event));
						}
					} catch (Throwable e) {
						AdViewUtil.logError("onClick", e);
					}
					if(activeRation.type != AdViewUtil.NETWORK_TYPE_ADFILL)
					return false;
				}
				if (AdViewTargeting.getSwitcherMode() == SwitcherMode.DEFAULT
						|| !closeAble) {
					countClick();
				}
				break;
			}
		}
		// Return false so subViews can process event normally.
		return false;
	}

	private int isMissTouch(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		AdViewUtil.logInfo("eventY: " + event.getY() + "\neventX: "
				+ event.getX());
		return x >= this.getWidth() / 16 && x <= this.getWidth() * 15 / 16
				&& y >= this.getHeight() / 6 && y <= this.getHeight() * 5 / 6 ? 0
				: 1;
	}

	private boolean iscloseBtn(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int screenWidth = 0;
		int screenHeight = 0;
		int height = 0;
		int width = 0;

		DisplayMetrics dm = new DisplayMetrics();
		activityReference.get().getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		mDensity = dm.density;
		screenWidth = dm.widthPixels;
		screenHeight = this.getHeight();
		height = (int) (imageHeight / mDensity);
		width = (int) (imageWidth / mDensity);
		return AdViewTargeting.getSwitcherMode() == SwitcherMode.DEFAULT ? true
				: (x >= (screenWidth - width)
						&& y >= (screenHeight - height) / 2 && y <= screenHeight
						- (screenHeight - height) / 2) ? true : false;
	}

	public void setAdViewInterface(AdViewInterface adViewInterface) {
		this.adViewInterface = adViewInterface;
	}

	private class InitRunnable implements Runnable {
		private WeakReference<AdViewLayout> adViewLayoutReference;
		private String keyAdView;

		public InitRunnable(AdViewLayout adViewLayout, String keyAdView) {
			adViewLayoutReference = new WeakReference<AdViewLayout>(
					adViewLayout);
			this.keyAdView = keyAdView;
		}

		@SuppressWarnings("static-access")
		public void run() {
			// 用户不开启广告，就return
			if (isClosed)
				return;
			AdViewLayout adViewLayout = adViewLayoutReference.get();

			if (adViewLayout != null) {
				Activity activity = adViewLayout.activityReference.get();
				if (activity == null) {
					return;
				}

				if (adViewLayout.adViewManager == null) {
					adViewLayout.adViewManager = new AdViewManager(
							new WeakReference<Context>(
									activity.getApplicationContext()),
							keyAdView);
				}

				if (!adViewLayout.hasWindow) {
					adViewLayout.isScheduled = false;
					return;
				}

				adViewLayout.adViewManager.fetchConfig(adViewLayout);

				adViewLayout.extra = adViewLayout.adViewManager.getExtra();
				if (adViewLayout.extra == null) {
					adViewLayout.scheduler.schedule(this, 30, TimeUnit.SECONDS);
				} else {
					if (null != adViewManager
							&& adViewManager.needUpdateConfig())
						adViewLayout.fetchConfigThreadedDelayed(10);
					else
						adViewLayout
								.fetchConfigThreadedDelayed(adViewLayout.adViewManager
										.getConfigExpiereTimeout());

					adViewLayout.appReport();
					if (!isTest)
						adViewLayout.rotateAd();
				}
			}
		}
	}

	private static class HandleAdRunnable implements Runnable {
		private WeakReference<AdViewLayout> adViewLayoutReference;

		public HandleAdRunnable(AdViewLayout adViewLayout) {
			adViewLayoutReference = new WeakReference<AdViewLayout>(
					adViewLayout);
		}

		public void run() {
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if (adViewLayout != null) {
				adViewLayout.handleAd();
			}
		}
	}

	public static class ViewAdRunnable implements Runnable {
		private WeakReference<AdViewLayout> adViewLayoutReference;
		private ViewGroup nextView;

		public ViewAdRunnable(AdViewLayout adViewLayout, ViewGroup nextView) {
			adViewLayoutReference = new WeakReference<AdViewLayout>(
					adViewLayout);
			this.nextView = nextView;
		}

		public void run() {
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if (adViewLayout != null) {
				adViewLayout.pushSubView(nextView);
			}
		}
	}

	private static class RotateAdRunnable implements Runnable {
		private WeakReference<AdViewLayout> adViewLayoutReference;

		public RotateAdRunnable(AdViewLayout adViewLayout) {
			adViewLayoutReference = new WeakReference<AdViewLayout>(
					adViewLayout);
		}

		public void run() {
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if (adViewLayout != null) {
				adViewLayout.rotateAd();
			}
		}
	}

	private static class RotatePriAdRunnable implements Runnable {
		private WeakReference<AdViewLayout> adViewLayoutReference;

		public RotatePriAdRunnable(AdViewLayout adViewLayout) {
			adViewLayoutReference = new WeakReference<AdViewLayout>(
					adViewLayout);
		}

		public void run() {
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if (adViewLayout != null) {
				adViewLayout.rotatePriAd();
			}
		}
	}

	private static class GetConfigRunnable implements Runnable {
		private WeakReference<AdViewLayout> adViewLayoutReference;

		public GetConfigRunnable(AdViewLayout adViewLayout) {
			adViewLayoutReference = new WeakReference<AdViewLayout>(
					adViewLayout);
		}

		public void run() {
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if (adViewLayout != null) {
				if (adViewLayout.hasWindow == false)
					return;

				if (adViewLayout.adViewManager != null) {
					adViewLayout.adViewManager
							.fetchConfigFromServer(adViewLayout);
				}
				adViewLayout
						.fetchConfigThreadedDelayed(adViewLayout.adViewManager
								.getConfigExpiereTimeout());
			}
		}
	}

	private static class PingUrlRunnable implements Runnable {
		private String url;

		public PingUrlRunnable(String url) {
			this.url = url;
		}

		public void run() {

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			// AdViewUtil.logInfo("PingUrlRunnable, url="+url);

			try {
				httpClient.execute(httpGet);

			} catch (Exception e) {
				AdViewUtil.logError("Caught Exception in PingUrlRunnable", e);

			} finally {

				httpClient.getConnectionManager().shutdown();
			}
		}
	}

	// private boolean isTimeToFill() {
	// boolean isAllDisplayed = false;
	// if (!isadFill)
	// return false;
	//
	// if (AdViewUtil.common_count != 0) {
	// double total = (AdViewUtil.common_count+AdViewUtil.adfill_count);
	// if (AdViewUtil.adfill_count / total > 0.1) {
	// if (null != AdViewUtil.displayRec)
	// if (adViewManager.getRationList().size()-1 == AdViewUtil.displayRec
	// .size())
	// isAllDisplayed = true;
	// } else
	// isAllDisplayed = true;
	// } else
	// isAllDisplayed = false;
	// // isAllDisplayed = true;
	// return isAllDisplayed;
	// }
	// private static class PingKyAdviewRunnable implements Runnable {
	// private String url;
	// private String content;
	//
	// public PingKyAdviewRunnable(String url, String content) {
	// this.url = url;
	// this.content = content;
	// }
	//
	// public void run() {
	// HttpPost httpRequest = new HttpPost(url);
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("name", content));
	// HttpClient httpClient = new DefaultHttpClient();
	// try {
	// httpRequest.setEntity(new UrlEncodedFormEntity(params,
	// HTTP.UTF_8));
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	//
	// EntityUtils.toString(httpResponse.getEntity(), "UTF_8");
	// }
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// httpClient.getConnectionManager().shutdown();
	// }
	// }
	// }
}
