package www.ezriouil.eziltv.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import www.ezriouil.eziltv.databinding.ActivitySplashBinding
import www.ezriouil.eziltv.remote.retrofit
import www.ezriouil.eziltv.utils.ConnectionLiveData
import www.ezriouil.eziltv.utils.Constants

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)
        network()
    }

    private fun network() {
        val ntw = ConnectionLiveData(application)
        ntw.observe(this) { network ->
            if (network) {
                binding.networkLayout.visibility = View.GONE
                binding.splashProgress.visibility = View.VISIBLE
                api()
            } else {
                binding.networkLayout.visibility = View.VISIBLE
                binding.splashProgress.visibility = View.GONE
            }
        }
    }

    private fun api() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val serversResult = retrofit.servers()
                if (serversResult.isSuccessful) {
                    serversResult.body()?.let {
                        Constants.servers.addAll(it)
                        withContext(Dispatchers.Main) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            this@SplashActivity.finish()
                        }
                    }
                }

            } catch (e: Exception) {
                Log.d("TAG", e.message ?: "Error404")
            }
        }
    }
}