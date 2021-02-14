package pl.toboche.ethchecker.repositories.balance

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
class ProvidedBalanceModule {

    @Provides
    fun provideBalanceRetrofitService(
        okHttpClient: OkHttpClient,
        configurationConstants: ConfigurationConstants
    ): BalanceRetrofitService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(configurationConstants.etherscanApiUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //I'd consider providing this builder from another module
            .create(BalanceRetrofitService::class.java)
}