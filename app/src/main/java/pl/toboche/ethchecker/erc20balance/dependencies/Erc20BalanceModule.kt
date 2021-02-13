package pl.toboche.ethchecker.erc20balance.dependencies

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import pl.toboche.ethchecker.erc20balance.Erc20BalanceContract
import pl.toboche.ethchecker.erc20balance.presentation.Erc20BalancePresenter
import pl.toboche.ethchecker.erc20balance.ui.Erc20BalanceFragment

@Module
@InstallIn(FragmentComponent::class)
abstract class Erc20BalanceModule {

    @Binds
    abstract fun bindErc20BalanceView(homeFragment: Erc20BalanceFragment): Erc20BalanceContract.View

    @Binds
    abstract fun bindErc20BalancePresenter(homePresenter: Erc20BalancePresenter): Erc20BalanceContract.Presenter

}