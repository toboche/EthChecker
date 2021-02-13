package pl.toboche.ethchecker.repositories.balance

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class BalanceModule {

    @Binds
    abstract fun bindBalanceRepository(balanceRemoteRepository: BalanceRemoteRepository): BalanceRepository
}