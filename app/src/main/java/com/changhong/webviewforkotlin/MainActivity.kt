package com.changhong.webviewforkotlin

import android.annotation.SuppressLint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_main.*
import android.view.ViewGroup
import android.webkit.*


class MainActivity : AppCompatActivity() {
    private lateinit var webview: WebView

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview = WebView(applicationContext)
        frameLayout_webview.addView(webview)

        webview.loadUrl("https://blog.csdn.net/dhl91604/article/details/76272050")

        webview.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端

        /** WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
         *   onJsAlert webview不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
         *   onReceivedTitle 获取网页标题
         *   onReceivedIcon 获取网页icon
         *   onProgressChanged 加载进度回调
        * */
        webview.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }
        }
        /** WebViewClient主要帮助WebView处理各种通知、请求事件的
         *   onPageFinished 页面请求完成
         *   onPageStarted 页面开始加载
         *   shouldOverrideUrlLoading 拦截url
         *   onReceivedError 访问错误时回调，例如访问网页时报错404，在这个方法回调的时候可以加载错误页面。
         */
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        val webSettings = webview.getSettings()
        // webview启用javascript支持 用于访问页面中的javascript
        webSettings.setJavaScriptEnabled(true)
        //设置WebView缓存模式 默认断网情况下不缓存
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        //断网情况下加载本地缓存
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //让WebView支持DOM storage API
        webSettings.domStorageEnabled = true
        //让WebView支持缩放
        webSettings.setSupportZoom(true)
        //启用WebView内置缩放功能
        webSettings.builtInZoomControls = true
        //让WebView支持可任意比例缩放
        webSettings.useWideViewPort = true
        //让WebView支持播放插件
        webSettings.pluginState = WebSettings.PluginState.ON
        //设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上
        webSettings.displayZoomControls = false
        //设置在WebView内部是否允许访问文件
        webSettings.allowFileAccess = true
        //设置WebView的访问UserAgent
//        webSettings.setUserAgentString(WebViewUtil.getUserAgent(getActivity(), webSettings));
        //设置脚本是否允许自动打开弹窗
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        // 加快HTML网页加载完成速度
        webSettings.loadsImagesAutomatically = Build.VERSION.SDK_INT >= 19
        // 开启Application H5 Caches 功能
        webSettings.setAppCacheEnabled(true)
        // 设置编码格式
        webSettings.defaultTextEncodingName = "utf-8"
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webview.goBack() // goBack()表示返回webView的上一页面
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        webview.removeAllViews()
        (webview.getParent() as ViewGroup).removeView(webview)
        webview.setTag(null)
        webview.clearHistory()
        webview.destroy()
    }
}
