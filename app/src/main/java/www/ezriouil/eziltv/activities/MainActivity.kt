package www.ezriouil.eziltv.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.monstertechno.adblocker.AdBlockerWebView
import kotlinx.coroutines.*
import www.ezriouil.eziltv.R
import www.ezriouil.eziltv.ads.Admob
import www.ezriouil.eziltv.ads.Applovin
import www.ezriouil.eziltv.ads.Unity
import www.ezriouil.eziltv.databinding.ActivityMainBinding
import www.ezriouil.eziltv.utils.AdBlock
import www.ezriouil.eziltv.utils.ChromeClient
import www.ezriouil.eziltv.utils.ConnectionLiveData
import www.ezriouil.eziltv.utils.Constants

class MainActivity : AppCompatActivity() {

    private var serverUrl: String? = null
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var adBlock: AdBlock
    private lateinit var binding: ActivityMainBinding
    private val rowAd = Constants.servers[0]
    private val applovinAd = Constants.servers[1]
    private val unityAd = Constants.servers[2]
    private var counter = 0
    private var admob: Admob? = null
    private var applovin: Applovin? = null
    private var unity: Unity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        connectionLiveData = ConnectionLiveData(application)
        connectionLiveData.observe(this) { network ->
            if (!network) binding.networkLayout.visibility = View.VISIBLE
            else binding.networkLayout.visibility = View.GONE
        }
        webView()
        initAds()
        initView()
        refresh()
        contact()
    }

    private fun contact() {
        binding.contactUs.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "--> EZIL TV <--\n" +
                        "pour avoir tous les match de football gratuitement\n" +
                        "lien:${Constants.servers.last().bannerAd}"
            )
            sendIntent.type = "text/plain"
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }

    private fun initAds() {
        if (rowAd.adsVisible == 1) {
            when (rowAd.bannerType) {
                Constants.ADMOB -> {
                    rowAd.bannerAd?.let { bannerId ->
                        admob?.banner(bannerId, binding.adLayout)
                    }
                }
                Constants.APP_LOVIN -> {
                    applovinAd.bannerAd?.let { bannerId ->
                        applovin?.banner(bannerId, binding.adLayout)
                    }
                }
                Constants.UNITY -> {
                    unity?.banner(binding.adLayout)
                }
                Constants.START_APP -> {}
                Constants.AD_MANAGER -> {}
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun webView() {
        AdBlockerWebView.init(this).initializeWebView(binding.webView)
        adBlock = AdBlock()
        binding.webView.webViewClient = adBlock
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportMultipleWindows(true)
        binding.progressLoading.max = 100
        binding.webView.webChromeClient = ChromeClient(this, binding.progressLoading)
        binding.webView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                counter++
                if (counter == Constants.servers.last().adsVisible) {
                    interstitial()
                    counter = 0
                }
            }
            return@setOnTouchListener false
        }
        admob = Admob(this)
        applovin = Applovin(this)
        unity = Unity(this, unityAd.bannerAd.toString())
    }

    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            runBlocking {
                delay(1000)
                binding.swipeRefresh.isRefreshing = false
                binding.webView.reload()
            }
        }
    }

    private fun initView() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolBar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.isDrawerSlideAnimationEnabled = true
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.title) {
                Constants.SERVER_1 -> {
                    eachItemMenu(Constants.SERVER_1)
                }
                Constants.SERVER_2 -> {
                    eachItemMenu(Constants.SERVER_2)
                }
                Constants.SERVER_3 -> {
                    eachItemMenu(Constants.SERVER_3)
                }
                Constants.SERVER_4 -> {
                    eachItemMenu(Constants.SERVER_4)
                }
                Constants.SERVER_5 -> {
                    eachItemMenu(Constants.SERVER_5)
                }
                Constants.SERVER_6 -> {
                    eachItemMenu(Constants.SERVER_6)
                }
                Constants.SERVER_7 -> {
                    eachItemMenu(Constants.SERVER_7)
                }
                Constants.SERVER_8 -> {
                    eachItemMenu(Constants.SERVER_8)
                }
                Constants.SERVER_9 -> {
                    eachItemMenu(Constants.SERVER_9)
                }
                Constants.SERVER_10 -> {
                    eachItemMenu(Constants.SERVER_10)
                }
            }
            return@setNavigationItemSelectedListener true
        }
        addMenu()
    }

    private fun addMenu() {
        val navView = binding.navigationView
        Constants.servers.forEach { menuItem ->
            menuItem.let { serverItem ->
                navView.menu.add(serverItem.title)
                navView.menu.getItem(serverItem.id!!).title = serverItem.title
                navView.menu.getItem(serverItem.id).isVisible = serverItem.isVisible == 1
                navView.menu.getItem(serverItem.id).setIcon(R.drawable.ic_live)
            }
        }

        Constants.servers[0].url?.let { url ->
            binding.webView.loadUrl(url)
            binding.progressLoading.progress = 0
            serverUrl = url
        }
    }

    private fun eachItemMenu(serverNumber: String) {
        val currentItem = Constants.servers.find { serverItem ->
            serverItem.title == serverNumber
        }

        currentItem?.url?.let { url ->
            binding.webView.clearHistory()
            binding.webView.loadUrl(url)
            binding.progressLoading.progress = 0
        }
        currentItem?.id?.let { id ->
            binding.navigationView.menu.getItem(id).setCheckable(true)
        }
        serverUrl = currentItem?.url
        binding.toolBar.title = serverNumber
        binding.drawerLayout.close()

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.close()
        } else {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                val alertDialog = AlertDialog.Builder(this, R.style.bottomSheetStyle).create()
                val view = layoutInflater.inflate(R.layout.dialog, null)
                val btnExitOui = view.findViewById<Button>(R.id.btnExitOui)
                val btnExitNo = view.findViewById<Button>(R.id.btnExitNo)
                btnExitOui.setOnClickListener {
                    this.finish()
                }
                btnExitNo.setOnClickListener {
                    alertDialog.dismiss()
                }
                alertDialog.setView(view)
                alertDialog.show()
            }
        }
    }

    private fun interstitial() {
        if (rowAd.adsVisible == 1) {
            when (rowAd.interType) {
                Constants.ADMOB -> {
                    rowAd.interstitialAd?.let { interId ->
                        admob?.interstitial(interId)
                    }
                }
                Constants.APP_LOVIN -> {
                    applovinAd.interstitialAd?.let { interId ->
                        applovin?.interstitial(interId)
                    }
                }
                Constants.UNITY -> {
                    unity?.initialize()
                }
                Constants.START_APP -> {}
                Constants.AD_MANAGER -> {}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.webView.restoreState(savedInstanceState)
    }

}
