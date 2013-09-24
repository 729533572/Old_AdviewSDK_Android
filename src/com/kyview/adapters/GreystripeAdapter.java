package com.kyview.adapters;

import android.app.Activity;
import android.view.View;

import com.greystripe.sdk.GSAd;
import com.greystripe.sdk.GSAdErrorCode;
import com.greystripe.sdk.GSAdListener;
import com.greystripe.sdk.GSFullscreenAd;
import com.greystripe.sdk.GSMobileBannerAdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;



public class GreystripeAdapter extends AdViewAdapter implements GSAdListener,GSAd 
{

	private GSFullscreenAd myFullscreenAd;


	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_GREYSTRIP;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.greystripe.sdk.GSMobileBannerAdView") != null) {
				registry.registerClass(networkType(), GreystripeAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public GreystripeAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		 AdViewUtil.logInfo("Into Greystripe");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		try {
			Activity activity = adViewLayout.activityReference.get();
			if(activity == null) {
				return;
			}
			  myFullscreenAd  = new  GSFullscreenAd(activity.getApplicationContext() ,ration.key); 
			  GSMobileBannerAdView banner=new GSMobileBannerAdView(activity.getApplicationContext());
		    banner.addListener(this);
		    banner.refresh();
		}
		catch (Exception e) {               
			adViewLayout.rotatePriAd();                       
		}
		
	}

 


	@Override
	public void onAdClickthrough(GSAd arg0) {
		
	}

	@Override
	public void onAdDismissal(GSAd arg0) {
	 
		
	}



	@Override
	public void onFailedToFetchAd(GSAd arg0, GSAdErrorCode arg1) {
		AdViewUtil.logInfo("Greystripe fail");
		arg0.removeListener(this);
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	    	return;
	    }
	    super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
		
	}



	@Override
	public void onFetchedAd(GSAd arg0) {
		AdViewUtil.logInfo("Greystripe success");
		arg0.removeListener(this);
		
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	      return;
	    }
	    super.onSuccessed(adViewLayout, ration);
	    adViewLayout.adViewManager.resetRollover();
//	    adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
	    adViewLayout.rotateThreadedDelayed();
		
	}



	@Override
	public void addListener(GSAdListener arg0) {
		 
		
	}



	@Override
	public boolean isAdReady() {
		 
		return false;
	}



	@Override
	public void removeListener(GSAdListener arg0) {
		 
		
	}
	  public void fetchFullscreenClicked(View v) {
		  AdViewUtil.logInfo("Fetch fullscreen clicked.");
	        if (!myFullscreenAd.isAdReady()) {
	            myFullscreenAd.fetch();
	        } else {
	        	  AdViewUtil.logInfo("Ad ready!  Display it");
	        }
	    }
	    
	    
	    public void displayFullscreenClicked(View v) {
	        AdViewUtil.logInfo("Fullscreen display clicked.");
	        if (!myFullscreenAd.isAdReady()) {
	           
	        } else {
	            myFullscreenAd.display();
	        }
	    }


}
