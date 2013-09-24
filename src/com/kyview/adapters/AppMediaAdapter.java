package com.kyview.adapters;

import android.app.Activity;
import cn.appmedia.ad.AdManager;
import cn.appmedia.ad.AdViewListener;
import cn.appmedia.ad.BannerAdView;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AppMediaAdapter extends AdViewAdapter implements AdViewListener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_APPMEDIA;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("cn.appmedia.ad.BannerAdView") != null) {
				registry.registerClass(networkType(), AppMediaAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public AppMediaAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
		AdManager.setAid(ration.key);
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into AppMedia");
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }
		 	
		  Activity activity = adViewLayout.activityReference.get();
		  if(activity == null) {
			  return;
		  }
		  BannerAdView adView = new BannerAdView(activity);
		  adView.setAdListener(this);
		  adView.requestAd();
	}

	@Override
	public void onReceiveAdFailure(BannerAdView arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AppMedia failure");
	    
		arg0.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
		
	}

	@Override
	public void onReceiveAdSuccess(BannerAdView arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AppMedia success");
		AdViewLayout adViewLayout = adViewLayoutReference.get();

		arg0.setAdListener(null);
	
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new AdViewLayout.ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();

	}



}
