package www.ezriouil.eziltv.remote

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import www.ezriouil.eziltv.utils.Constants

interface Api {
    @GET(Constants.API_GET_SERVERS)
    suspend fun servers(): Response<List<Server>>
}

val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(Api::class.java)