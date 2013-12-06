package com.kyview.statistics;

public class StatisticsBean {
	private String adName=null;
	private int impression=0;
	private int click=0;
	private int failed=0;
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	public int getImpression() {
		return impression;
	}
	public void setImpression(int impression) {
		this.impression = impression;
	}
	public int getClick() {
		return click;
	}
	public void setClick(int click) {
		this.click = click;
	}
	public int getFailed() {
		return failed;
	}
	public void setFailed(int failed) {
		this.failed = failed;
	}
	
}
