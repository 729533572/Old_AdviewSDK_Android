package com.kyview.adapters;

import android.app.Activity;
import android.content.Context;

import com.donson.momark.AdManager;
import com.donson.momark.util.AdViewListener;
import com.donson.momark.view.view.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
//import android.graphics.Color;

public class MomarkAdapter extends AdViewAdapter implements AdViewListener{
	AdView adView;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_MOMARK;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.donson.momark.view.view.AdView") != null) {
				registry.registerClass(networkType(), MomarkAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public MomarkAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub

		String key=new String(ration.key);
		String key2=new String(ration.key2);
		
		AdManager.init(key2, key);
		adView = null;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		AdViewUtil.logInfo("Into Momark");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	
	    Activity activity = adViewLayout.activityReference.get();
		  if(activity == null) {
			  return;
		  }  	     		
	    adView = new AdView((Context)activity, "adview");		
		adView.setAdViewListener(this);
	}

	@Override
	public void onConnectFailed(AdView paramAdView) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Momark failure");
		 paramAdView.setAdViewListener(null);

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		  super.onFailed(adViewLayout, ration);
		  //adViewLayout.rotateThreadedPri(1);
		
	}

	@Override
	public void onAdViewSwitchedAd(AdView paramAdView) {
		// TODO Auto-generated method stub
	
		AdViewUtil.logInfo("Momark success");
		paramAdView.setAdViewListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }
		  super.onSuccessed(adViewLayout, ration);
		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, paramAdView));
		  adViewLayout.rotateThreadedDelayed();
		
	}




}
