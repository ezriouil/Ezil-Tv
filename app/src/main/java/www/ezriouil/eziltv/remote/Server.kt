package www.ezriouil.eziltv.remote

import com.google.gson.annotations.SerializedName

data class Server(
    @SerializedName("adsVisible")
    val adsVisible: Int? = null, // 1
    @SerializedName("bannerAd")
    val bannerAd: String? = null, // ca-app-pub-3940256099942544/6300978111
    @SerializedName("bannerType")
    val bannerType: String? = null, // admob
    @SerializedName("id")
    val id: Int? = null, // 0
    @SerializedName("interType")
    val interType: String? = null, // admob
    @SerializedName("interstitialAd")
    val interstitialAd: String? = null, // ca-app-pub-3940256099942544/1033173712
    @SerializedName("isVisible")
    val isVisible: Int? = null, // 1
    @SerializedName("title")
    val title: String? = null, // server 1
    @SerializedName("url")
    val url: String? = null // https://w.egylive.club/
)