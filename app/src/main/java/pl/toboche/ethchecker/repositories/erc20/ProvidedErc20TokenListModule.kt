package pl.toboche.ethchecker.repositories.erc20

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ProvidedErc20TokenListModule(
    private val url: String = "https://api.ethplorer.io/"
) {

    @Provides
    fun provideBalanceRetrofitService(okHttpClient: OkHttpClient): Erc20TokenListRetrofitService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //I'd consider providing this builder from another module
            .create(Erc20TokenListRetrofitService::class.java)
}