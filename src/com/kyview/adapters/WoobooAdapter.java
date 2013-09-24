package com.kyview.adapters;

import android.graphics.Color;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.wooboo.adlib_android.AdListener;
import com.wooboo.adlib_android.WoobooAdView;

public class WoobooAdapter extends AdViewAdapter implements AdListener {
	
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_WOOBOO;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.wooboo.adlib_android.WoobooAdView") != null) {
				registry.registerClass(networkType(), WoobooAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public WoobooAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	
	public void handle() {
		AdViewUtil.logInfo("Into Wooboo");
	 	 AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	 if(adViewLayout == null) {
	 		 return;
	 	 }
	 	Extra extra = adViewLayout.extra;
	    int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	    int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
	    WoobooAdView adView=null;
	    if(AdViewTargeting.getRunMode()==RunMode.TEST)
	    	adView=new WoobooAdView(adViewLayout.getContext(), ration.key,bgColor, fgColor,120);
	    else if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
	    	adView=new WoobooAdView(adViewLayout.getContext(), ration.key,bgColor, fgColor,120);
	    else{
	    	adView=new WoobooAdView(adViewLayout.getContext(), ration.key,bgColor, fgColor,120);
	    }
	 
	    adView.setHorizontalScrollBarEnabled(false);
	    adView.setVerticalScrollBarEnabled(false);
	    adView.setAdListener(this);
	   // adViewLayout.adViewManager.resetRollover();
	 //	adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
	//	adViewLayout.rotateThreadedDelayed();
	   
	}

	@Override
	public void onFailedToReceiveAd(Object arg0) {
		// TODO Auto-generated method stub
		WoobooAdView adView=(WoobooAdView)arg0;
		AdViewUtil.logInfo("Woboo failure");
	    
		  adView.setAdListener(null);
	
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		  super.onFailed(adViewLayout, ration);
		  //adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onReceiveAd(Object arg0) {
		// TODO Auto-generated method stub
		WoobooAdView adView=(WoobooAdView)arg0;
		AdViewUtil.logInfo("Wooboo success");
		adView.setAdListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }
		  super.onSuccessed(adViewLayout, ration);
		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		  adViewLayout.rotateThreadedDelayed();
		
	}

	public void onPlayFinish() {
		AdViewUtil.logInfo("Wooboo onPlayFinish");
	}



	
}
