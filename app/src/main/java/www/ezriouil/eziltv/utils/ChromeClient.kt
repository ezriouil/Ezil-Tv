package www.ezriouil.eziltv.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.test.core.app.ApplicationProvider

class ChromeClient(private val activity: Activity, private val pBar: ProgressBar) :
    WebChromeClient() {

    private var mCustomView: View? = null
    private var mCustomViewCallback: CustomViewCallback? = null
    private var mOriginalOrientation = 0
    private var mOriginalSystemUiVisibility = 0

    override fun getDefaultVideoPoster(): Bitmap? {
        return if (mCustomView == null) {
            null
        } else BitmapFactory.decodeResource(
            ApplicationProvider.getApplicationContext<Context>().resources,
            2130837573
        )
    }

    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(mCustomView)
        mCustomView = null
        activity.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
        activity.requestedOrientation = mOriginalOrientation
        mCustomViewCallback!!.onCustomViewHidden()
        mCustomViewCallback = null
    }

    override fun onShowCustomView(paramView: View, paramCustomViewCallback: CustomViewCallback) {
        if (mCustomView != null) {
            onHideCustomView()
            return
        }
        mCustomView = paramView
        mOriginalSystemUiVisibility = activity.window.decorView.systemUiVisibility
        mOriginalOrientation = activity.requestedOrientation
        mCustomViewCallback = paramCustomViewCallback
        (activity.window.decorView as FrameLayout).addView(
            mCustomView,
            FrameLayout.LayoutParams(-1, -1)
        )
        activity.window.decorView.systemUiVisibility = 3846
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        pBar.setProgress(newProgress, true)
        if (newProgress == 100) pBar.visibility = View.GONE
        else pBar.visibility = View.VISIBLE
        super.onProgressChanged(view, newProgress)
    }

}