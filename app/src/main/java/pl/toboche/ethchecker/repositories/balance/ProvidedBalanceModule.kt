package pl.toboche.ethchecker.repositories.balance

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
class ProvidedBalanceModule(
    private val url: String = "https://api.etherscan.io/"
) {

    @Provides
    fun provideBalanceRetrofitService(okHttpClient: OkHttpClient): BalanceRetrofitService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //I'd consider providing this builder from another module
            .create(BalanceRetrofitService::class.java)
}