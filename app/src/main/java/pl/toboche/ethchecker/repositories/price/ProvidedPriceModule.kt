package pl.toboche.ethchecker.repositories.price

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.toboche.ethchecker.base.ConfigurationConstants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ProvidedPriceModule {

    @Provides
    fun providePriceRetrofitService(
        okHttpClient: OkHttpClient,
        configurationConstants: ConfigurationConstants
    ): CryptoPriceRetrofitService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(configurationConstants.coingeckoApiUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoPriceRetrofitService::class.java)
}