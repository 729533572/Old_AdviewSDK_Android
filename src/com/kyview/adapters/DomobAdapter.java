package com.kyview.adapters;

import android.app.Activity;
import android.content.Context;
import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdManager;
import cn.domob.android.ads.DomobAdView;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
//import android.graphics.Color;
//import cn.domob.android.ads.DomobAdListener;
//import com.kyview.obj.Extra;

public class DomobAdapter extends AdViewAdapter implements DomobAdEventListener {

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_DOMOB;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("cn.domob.android.ads.DomobAdView") != null) {
				registry.registerClass(networkType(), DomobAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public DomobAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		}

	@Override
	public void handle() {
		// TODO Auto-generated method stub

		AdViewUtil.logInfo("Into Domob");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		// Extra extra = adViewLayout.extra;
		// int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		// int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		// Instantiate an ad view and add it to the view

		DomobAdView ad = new DomobAdView(activity, ration.key, ration.key2,
				DomobAdView.INLINE_SIZE_320X50,false);
		
		// DomobAdManager.setPublisherId(ration.key);
		/*
		 * if(AdViewTargeting.getRunMode()==RunMode.TEST)
		 * DomobAdManager.setIsTestMode(true); else
		 * if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
		 * DomobAdManager.setIsTestMode(false); else{
		 * DomobAdManager.setIsTestMode(false); }
		 */
		ad.setAdEventListener(this);
		// ad.setBackgroundColor(bgColor);
		// ad.setPrimaryTextColor(fgColor);

		// ad.requestAdForAggregationPlatform() ;
		// ad.setRequestInterval(0);
		adViewLayout.AddSubView(ad);
	}

	// @Override
	public void onDomobAdFailed(DomobAdView adView,
			DomobAdManager.ErrorCode paramErrorCode) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Domob failure, ErrorCode="
					+ paramErrorCode);

		// adView.setAdEventListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);

	}

	@Override
	public void onDomobAdReturned(DomobAdView adView) {
		// TODO Auto-generated method stub

		AdViewUtil.logInfo("Domob success");
		// adView.setAdEventListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
//		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();

	}

	public void onDomobAdClicked(DomobAdView adView) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Domob onDomobAdClicked");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	public void onDomobLeaveApplication(DomobAdView adView) {
		// TODO Auto-generated method stub

		AdViewUtil.logInfo("Domob onDomobLeaveApplication");
	}

	@Override
	public void onDomobAdOverlayPresented(DomobAdView adView) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("overlayPresented");
	}

	@Override
	public void onDomobAdOverlayDismissed(DomobAdView adView) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Overrided be dismissed");

	}

	@Override
	public Context onDomobAdRequiresCurrentContext() {
		// TODO Auto-generated method stub
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		Context mContext = (Context) adViewLayout.activityReference.get();

		return mContext;
	}

}
