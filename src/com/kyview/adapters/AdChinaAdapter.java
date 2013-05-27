package com.kyview.adapters;

import android.view.View;

import com.adchina.android.ads.AdBannerListener;
import com.adchina.android.ads.AdManager;
import com.adchina.android.ads.views.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdChinaAdapter extends AdViewAdapter implements AdBannerListener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADCHINA;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.adchina.android.ads.views.AdView") != null) {
				registry.registerClass(networkType(), AdChinaAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public AdChinaAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into AdChina");

	 	AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	if(adViewLayout == null) {
	 		return;
	 	}

		AdManager.setRefershinterval(-1);
		AdManager.setRelateScreenRotate(adViewLayout.getContext(),true);	
		AdView mAdView=new AdView(adViewLayout.getContext(), ration.key, true, false);
		mAdView.setAdBannerListener(this);
		
		mAdView.setVisibility(View.VISIBLE);	
		mAdView.start();
	}
	
	public void onFailedToReceiveAd(AdView arg0) {
		AdViewUtil.logInfo("onFailedToReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri(); 
	}

	public void onReceiveAd(AdView adView) {
		
		AdViewUtil.logInfo("onReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
		return;
		}

		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public boolean OnRecvSms(AdView arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClickBanner(AdView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailedToRefreshAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefreshAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	

}
