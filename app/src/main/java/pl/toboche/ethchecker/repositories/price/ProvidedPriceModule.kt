package pl.toboche.ethchecker.repositories.price

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(FragmentComponent::class)
class ProvidedPriceModule(
    private val url: String = "https://api.coingecko.com/"
) {

    @Provides
    fun providePriceRetrofitService(okHttpClient: OkHttpClient): CryptoPriceRetrofitService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoPriceRetrofitService::class.java)
}