package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.mt.airad.AirAD;
import com.mt.airad.AirADListener;
//import com.kyview.AdViewLayout.ViewAdRunnable;

//import android.widget.RelativeLayout;

public class AirAdAdapter extends AdViewAdapter implements AirADListener {
	
	private AirAD airAD=null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_AIRAD;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.mt.airad.AirAD") != null) {
				registry.registerClass(networkType(), AirAdAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public AirAdAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			AirAD.setAppID(AirAD.TEST_APP_ID);
		else
			AirAD.setAppID(ration.key);
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			AirAD.setDebugMode(AirAD.DebugMode_ON);
		AirAD.setGPSMode(AirAD.GPSMode_ON);
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into AirAD");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}
		  
		airAD=new AirAD(activity);

		airAD.setBannerBGMode(AirAD.BANNER_BG_ON);
		airAD.setRefreshMode(AirAD.REFRESH_MODE_MANUAL);
		//airAD.setIntervalTime(20);
		
		airAD.setAirADListener(this);

		adViewLayout.AddSubView(airAD);
		airAD.refreshAD();
	}

	@Override
	public void onAdReceived() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AirAD success");
		airAD.setAirADListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, airAD));
		adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void onAdReceivedFailed(int errorCode) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AirAD failure, errorCode="+errorCode);

		airAD.setAirADListener(null);
		AirAD.onDestroy();
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
		
	}

            @Override
            public void onAdContentShow() {
		AdViewUtil.logInfo("onAdContentShow");
            }

            @Override
            public void onAdContentLoadFinished() {
            	AdViewUtil.logInfo("onAdContentLoadFinished");

            }

            @Override
            public void onAdContentClose() {
            	AdViewUtil.logInfo("onAdContentClose");
            }

            @Override
            public void onAdBannerShown() {
            	AdViewUtil.logInfo("onAdBannerShown");

            }

	
}
