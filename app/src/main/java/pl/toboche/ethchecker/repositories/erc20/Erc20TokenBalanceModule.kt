package pl.toboche.ethchecker.repositories.erc20

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class Erc20TokenBalanceModule {

    @Binds
    abstract fun bindErc20TokenBalanceRepository(balanceRemoteRepository: Erc20TokenBalanceRemoteRepository): Erc20TokenBalanceRepository
}