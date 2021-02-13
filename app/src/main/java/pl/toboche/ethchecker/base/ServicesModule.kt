package pl.toboche.ethchecker.base

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.toboche.ethchecker.base.scheduler.ApplicationScheduler
import pl.toboche.ethchecker.base.scheduler.DefaultScheduler

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    fun bindScheduler(): ApplicationScheduler =
        DefaultScheduler(AndroidSchedulers.mainThread(), Schedulers.io())

    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()
    }
}