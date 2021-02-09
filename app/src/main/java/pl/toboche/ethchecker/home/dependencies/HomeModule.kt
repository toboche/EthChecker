package pl.toboche.ethchecker.home.dependencies

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import pl.toboche.ethchecker.home.HomeContract
import pl.toboche.ethchecker.home.presentation.HomePresenter
import pl.toboche.ethchecker.home.ui.HomeFragment

@Module
@InstallIn(FragmentComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindHomeView(homeFragment: HomeFragment): HomeContract.View

    @Binds
    abstract fun bindHomePresenter(homePresenter: HomePresenter): HomeContract.Presenter
}