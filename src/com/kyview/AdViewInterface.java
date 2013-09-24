package com.kyview;

public interface AdViewInterface {
	/**
	 * 当广告被点击时调用该函数
	 * 
	 * 
	 */
	public void onClickAd();
	
	/**
	 * 当广告被显示时调用该函数.
	 * 
	 * 
	 */
	
	public void onDisplayAd();
	
	/**
	 * 当广告被关闭时调用该函数.
	 * 
	 * 
	 */
	
	public void onClosedAd();
}
