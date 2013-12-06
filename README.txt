
欢迎使用AdView Android SDK。开发者通过使用AdView，可以实现众多广告公司广告的排列组合优化；使用应用互推功能可以实现应用推广。

版本说明
====================================
当前AdView支持的android最低版本为1.6版。

====================================
平台版本（AdView和各家广告平台的SDK均在libs目录下）：
	库名				广告平台					版本

AdViewSDK_Android.jar			AdView    					2.0.4
AdViewSDK_BID.jar			AdView互推&补余&竞价				2.0.4
adlantis.jar				Adlantis					1.3.7
adchina_android_sdk.jar			易传媒						3.0.1
adlib_android.jar			哇棒						2.3.0
adOn-3.2.8.jar				Vpon						3.2.8
adtouch-embed-sdk-1.1.0.jar		AdTouch						1.1.0
Adwo_Android_SDK3.1.jar			AdWo						4.0
ad-fractalist-sdk-android.jar		飞云						2.0
airAD_sdk.jar				AirAD						1.3.3
Baidu_MobAds_SDK_Agg_3.2.jar		百度						3.4.1
AppMediaAdAndroidSdk-1.1.0.jar		AppMedia					1.1.0
domob_android_sdk.jar			多盟						4.0.3
GoogleAdMobAdsSdk-6.4.1.jar		AdMob						6.4.1
gssdk_2.0.2.jar				GreyStripe					2.0.2
IZPView.jar				亿赞普						1.0.3
immobSDK.jar				力美						2.6.0

InMobiAdNetwork_3.7.0.jar
InMobiCommons_3.7.0.jar			InMobi						4.0.2

mdotm-sdk-android.jar			MdotM						3.0.0

MMSDK.jar								
armeabi\libnmsp_speex.so 		MillennialMedia					5.1.0
nmdp_speech_kit.jar

mobisageSDK.jar				艾德思奇					5.4.1
momark_sdk_v1.4.1.jar			Momark						1.4.1
smartmad-sdk-android.jar		亿动智道					3.0.1
SOMAAndroidSDK2.5.4.jar			Smaato						2.5.4
Tencent_MobWIN_SDK_2.0.jar		腾讯(MobWin)					2.1
tinmoo-android-sdk_v3.1.0.jar		天幕(Tinmoo)					3.1.0
umeng_ad_android_sdk_v1.4.4.jar		友盟						1.4.4

WiAd.jar
WiCommon.jar				微云						4.0.0

wqmobile_android_sdk.jar		帷千						3.1.1
youmi-android.jar			有米						4.0.5
zestadz_sdk_androidv1.2.jar		ZestADZ						1.2.0
l_android_adsdk.jar			Lsense						2.2.0
yunyun-ads-sdk-4-Adview-V3.0.jar	云云						3.1.0
aduu-sdk.jar				aduu						3.3.4
PunchBoxSDK_Android.jar			触控						3.2.3
zhidian_ad_sdk.jar			指点						4.1.1


平台选择
====================================
如果你是一个新手，对广告平台不够了解，不知道如何选择广告平台，平台是否收入稳定。
Adview建议您使用百度，亿动智道，Inmobi平台。




注意事项
====================================
广告平台	描述

友盟		需要把umeng_res目录下的资源文件也放到项目中

微云		包括WiAd.jar,WiCommon.jar，需要把wiyun_res目录下的资源文件也放到项目中

帷千、亿赞普	需要把<uses-sdkandroid:minSdkVersion=”4”/>去掉，否则广告显示尺寸会缩小；
		如果去掉<uses-sdkandroid:minSdkVersion=”4”/>的话，对于高分辨率的手机后台统计的可能不准；

聚赢		若需要lbs功能，请加上<uses-permission android:name=”android.permission.CHANGE_WIFI_STATE”/>

Admob		使用admob 4.3.1以上版本，需要android 3.2以上版本才能正确编译, 
		且需要在AndroidManifest.xml中配置为android:configChanges=”keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize”/>。
		（sample中缺省为给注释掉了）

GreyStripe	在sample中的Manifest注释掉了GreyStripe广告的声明，需要使用请自行打开

InMobi		需要android 3.2以上版本才能正确编译且需要在AndroidManifest.xml中配置为
		<activity android:name="com.inmobi.androidsdk.IMBrowserActivity" 
		android:configChanges="keyboardHidden|orientation|keyboard|smallestScreenSize|screenSize" />
		（sample中缺省给注释掉了）

其他：

名词		描述

离线配置功能	需要使用离线配置功能，请从adview后台点击“离线配置下载”下载配置文件，保存在<assets>文件夹中，离线配置确保		任何情况下都能展示广告

内置下载功能	请在AndroidManifest.xml文件中声明com.kyview.DownloadService，互推和内嵌广告下载的时候要用

市场渠道
====================================
开发者可以设置渠道，在Manifest文件中设置<meta-data android:name="AdView_CHANNEL" android:value="GFAN"></meta-data> 
(AdViewTargeting.setChannel接口已经作废); 目前支持的渠道包括下面列出的，各个市场的链接见移动广告观察(http://t.adview.cn )
EOE		优亿市场
GOOGLEMARKET	谷歌电子市场
APPCHINA	应用汇
HIAPK		安卓市场
GFAN		机锋
GOAPK		安智
NDUOA		N多网
91Store		手机91
LIQUCN		历趣
ANDROIDAI	安卓之家
ANDROIDD	安致
YINGYONGSO	应用搜
IMOBILE		手机之家
MUMAYI		木蚂蚁
PAOJIAO		泡椒网
AIBALA		爱扒拉市场
COOLAPK		酷安网
ANFONE		安丰
APKOK		乐致网
360MARKET	360市场
如果不配置，或配置其他的值，一律作为"OTHER"处理。

相关文档
====================================
AdView SDK 安装指南
AdView应用推广使用指南
java_doc:详细的java接口说明

源代码开放
====================================
Android sdk已经开源，源代码地址：
https://github.com/adview/AdviewSDK_Android

联系方式
====================================
欢迎联系AdView，您可以用以下方式和我们沟通：

Tel: 010-52911111  QQ: 1263811658
Email: threadxyz@gmail.com
Blog: http://t.sina.com.cn/adview
www: http://www.adview.cn

