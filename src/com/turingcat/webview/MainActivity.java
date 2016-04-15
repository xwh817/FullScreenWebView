package com.turingcat.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 全屏浏览器
 *  全屏显示
 *  退出后自动重启进入前面
 *  长按更改页面网址
 *  页面加载失败，自动刷新
 * @author XWH
 *
 */
public class MainActivity extends Activity {
	
	private static final String TAG = "test";

	private WebView webview;
	private TextView text_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		
		Window window = getWindow();  
        WindowManager.LayoutParams params = window.getAttributes();  
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;  
        window.setAttributes(params); 
        

		setContentView(R.layout.activity_main);

		initWebView();

		
		text_info = (TextView) this.findViewById(R.id.text_info);
		
		

		webview.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

		        initInputDialog();
				
				return false;
			}
		});
		
		

		startService(new Intent(this, HeartBeatService.class));

	}
	
	
	private String url_default = "http://172.16.45.254/fridge/fridge.html";
	//private String url_default = "http://www.baidu.com";
	private String url = url_default;
	
	private void initWebView(){

		url = PreferenceManager.getDefaultSharedPreferences(this).getString("web_url", url_default);

		webview = (WebView) this.findViewById(R.id.webview);

		webview.getSettings().setJavaScriptEnabled(true);

		// 设置webviewChromClient
		webview.setWebChromeClient(new WebChromeClient());
		
		// 去掉滚动条
		webview.setHorizontalScrollBarEnabled(false);
		webview.setVerticalScrollBarEnabled(false);

		// 防止跳出浏览器
		webview.setWebViewClient(new WebViewClient() {
			// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				
				Toast.makeText(MainActivity.this, "页面加载失败："+errorCode+" "+description, Toast.LENGTH_SHORT).show();
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						webview.loadUrl(url);
					}
				}, 5000);
				
			}
		});

		
		//设置可否支持缩放   
		webview.getSettings().setSupportZoom(false);   

        //设置默认缩放方式尺寸是far   
        //webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);  

        //设置出现缩放工具   
        //webview.getSettings().setBuiltInZoomControls(true);
		//webview.setInitialScale(80);

		webview.loadUrl(url);
	}
	
	

	
	private void initInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText inputServer = new EditText(this);
		inputServer.setText(url);
        builder.setTitle("请输入网址：").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               url = inputServer.getText().toString();
               if(!url.startsWith("http")){
            	   url = "http://"+url;
               }
               webview.loadUrl(url);
               
               // 保存上次的输入
               PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("web_url", url).commit();
             }
        });
        
        builder.show();
	}
	


}
