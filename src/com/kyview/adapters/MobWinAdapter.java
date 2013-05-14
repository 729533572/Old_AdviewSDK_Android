package com.kyview.adapters;

import android.app.Activity;
import android.content.Context;
//import android.graphics.Color;
import android.util.Log;



import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
//import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import com.tencent.exmobwin.MobWINManager;
import com.tencent.exmobwin.Type;
import com.tencent.exmobwin.banner.AdListener;
import com.tencent.exmobwin.banner.TAdView;


public class MobWinAdapter extends AdViewAdapter implements AdListener{
	private TAdView adView;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_MOBWIN;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.tencent.exmobwin.banner.TAdView") != null) {
				registry.registerClass(networkType(), MobWinAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public MobWinAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into MobWin");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	//Extra extra = adViewLayout.extra;
	    //int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	      
	    Activity activity = adViewLayout.activityReference.get();
		  if(activity == null) {
			  return;
		  }  	     
	   Context mcontext=(Context)activity;
	   MobWINManager.init(mcontext,  ration.key, "adview", "ben1574leo",Type.MOBWIN_BANNER);
	   adView = new TAdView(activity); 
	   adView.setAdListener(this);	
	   //adView.setBackgroundColor(bgColor);

	   adViewLayout.AddSubView(adView);
	}

	@Override
	public void onReceiveFailed(int errorId) {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onReceiveFailed, errorId="+errorId);
	    
		  adView.setAdListener(null);

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		 adViewLayout.adViewManager.resetRollover_pri();
		  adViewLayout.rotateThreadedPri();
		
	}

	@Override
	public void onReceiveAd() {
		// TODO Auto-generated method stub
	
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onReceiveAd");

		adView.setAdListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		adViewLayout.reportImpression();	
		  adViewLayout.adViewManager.resetRollover();
		  //adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		  adViewLayout.rotateThreadedDelayed();
		
	}

	//@Override
	public void onAdClick() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onAdClick");
		
	}


	

}
