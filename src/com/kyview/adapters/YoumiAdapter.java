package com.kyview.adapters;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewLinstener;
import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class YoumiAdapter extends AdViewAdapter implements AdViewLinstener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_YOUMI;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("net.youmi.android.banner.AdView") != null) {
				registry.registerClass(networkType(), YoumiAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public YoumiAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub

		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			  return;
		 }
		if(AdViewTargeting.getRunMode()==RunMode.TEST){
			if(adViewLayout.adViewManager.getYoumiInit()){
				AdManager.getInstance(activity).init(ration.key, ration.key2, true);
				adViewLayout.adViewManager.setYoumiInit(false);
			}
		}
		else{
			if(adViewLayout.adViewManager.getYoumiInit()){
			
				AdManager.getInstance(activity).init(ration.key, ration.key2, false);
				adViewLayout.adViewManager.setYoumiInit(false);
			}
		}
	}
	
	 	public void handle() {
	 		AdViewUtil.logInfo("Into Youmi");
	 		AdViewLayout adViewLayout = adViewLayoutReference.get();
	 		if(adViewLayout == null) {
	 			return;
	 	 	}
	    		Activity activity = adViewLayout.activityReference.get();
			if(activity == null) {
				return;
			}
			
			AdView adView=new AdView(activity,AdSize.SIZE_320x50);  
			adView.setAdListener(this);

			adViewLayout.AddSubView(adView);
	}

	@Override
	public void onSwitchedAd(AdView adView)
	{
		AdViewUtil.logInfo("onSwitchedAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		adView.setAdListener(null);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onReceivedAd(AdView adView)
	{
		AdViewUtil.logInfo("onReceivedAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }
		  super.onSuccessed(adViewLayout, ration);
		
	}
	
	@Override
	public void onFailedToReceivedAd(AdView adView)
	{
		AdViewUtil.logInfo("onFailedToReceivedAd");
		adView.setAdListener(null);
	
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}


	
}
