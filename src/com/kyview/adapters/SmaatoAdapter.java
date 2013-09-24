package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.smaato.SOMA.AdDownloader;
import com.smaato.SOMA.AdListener;
import com.smaato.SOMA.ErrorCode;
import com.smaato.SOMA.SOMABanner;
import com.smaato.SOMA.SOMAReceivedBanner;



public class SmaatoAdapter extends AdViewAdapter implements AdListener{
	private SOMABanner banner=null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_SMAATO;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.smaato.SOMA.SOMAReceivedBanner") != null) {
				registry.registerClass(networkType(), SmaatoAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public SmaatoAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		try {
			Activity activity = adViewLayout.activityReference.get();
			if(activity == null) {
				return;
			}
		banner=new SOMABanner (activity);
		banner.addAdListener(this);
		banner.setPublisherId(Integer.valueOf(ration.key).intValue());
		banner.setAdSpaceId(Integer.valueOf(ration.key2).intValue());
		banner.asyncLoadNewBanner();
		
		     
		}
		catch (Exception e) {               
			adViewLayout.rollover();                       
		}
		
		
	}

	@Override
	public void onFailedToReceiveAd(AdDownloader arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Smaato fail");
		banner.removeAdListener(this);
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	    	return;
	    }
	    super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
		
	}

	@Override
	public void onReceiveAd(AdDownloader arg0, SOMAReceivedBanner arg1) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Smaato success");
		banner.removeAdListener(this);
		
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	      return;
	    }
	    super.onSuccessed(adViewLayout, ration);
	    adViewLayout.adViewManager.resetRollover();
	    adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, banner));
	    adViewLayout.rotateThreadedDelayed();
		
	}



}
