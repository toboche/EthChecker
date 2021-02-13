package pl.toboche.ethchecker.home.presentation

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import pl.toboche.ethchecker.base.scheduler.DefaultScheduler
import pl.toboche.ethchecker.home.HomeContract
import pl.toboche.ethchecker.repositories.balance.AccountBalance
import pl.toboche.ethchecker.repositories.balance.BalanceRepository
import java.math.BigDecimal

class HomePresenterTest {

    val mockView: HomeContract.View = mock()

    val mockAccountBalance = AccountBalance(
        BigDecimal("123"),
        BigDecimal("9")
    )
    val mockBalanceRepository: BalanceRepository = mock {
        on { getAccountBalance(address = any()) } doReturn Single.just(mockAccountBalance)
    }
    val stubApplicationScheduler =
        DefaultScheduler(Schedulers.trampoline(), Schedulers.trampoline())

    val systemUnderTest = HomePresenter(mockBalanceRepository, stubApplicationScheduler)

    @Test
    fun `show ethereum address`() {
        val expectedAddressToShow = "0xde57...78c3"

        systemUnderTest.attach(mockView)

        verify(mockView).showAddress(expectedAddressToShow)
    }

    @Test
    fun `pass correct eth address`() {
        val expectedAddress = "0xde57844f758a0a6a1910a4787ab2f7121c8978c3"

        systemUnderTest.attach(mockView)

        verify(mockBalanceRepository).getAccountBalance(expectedAddress)
    }

    @Test
    fun `show ethereum formatted balance`() {
        systemUnderTest.attach(mockView)

        verify(mockView).showBalance("\$9.00 (123.00) ETH")
    }

    @Test
    fun `control progress bar visibility when loading data`() {
        systemUnderTest.attach(mockView)

        inOrder(mockView, mockBalanceRepository).apply {
            verify(mockView).showProgress()
            verify(mockBalanceRepository).getAccountBalance(any())
            verify(mockView).hideProgress()
        }
    }

    @Test
    fun `show error getting ethereum formatted balance`() {
        whenever(mockBalanceRepository.getAccountBalance(any())).thenReturn(Single.error(Exception()))
        systemUnderTest.attach(mockView)

        verify(mockView, never()).showBalance(any())
        verify(mockView).showError("Error loading data")
    }

    @Test
    fun `control progress bar visibility when error loading data`() {
        whenever(mockBalanceRepository.getAccountBalance(any())).thenReturn(Single.error(Exception()))

        systemUnderTest.attach(mockView)

        inOrder(mockView, mockBalanceRepository).apply {
            verify(mockView).showProgress()
            verify(mockBalanceRepository).getAccountBalance(any())
            verify(mockView).hideProgress()
        }
    }
}
