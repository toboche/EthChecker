package pl.toboche.ethchecker.repositories.balance

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BalanceModule {

    @Binds
    abstract fun bindBalanceRepository(balanceRemoteRepository: BalanceRemoteRepository): BalanceRepository
}