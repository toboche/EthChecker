package pl.toboche.ethchecker.erc20balance.presentation

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import pl.toboche.ethchecker.base.scheduler.DefaultScheduler
import pl.toboche.ethchecker.erc20balance.Erc20BalanceContract
import pl.toboche.ethchecker.erc20balance.ui.Erc20TokenBalanceDescription
import pl.toboche.ethchecker.repositories.erc20.Erc20TokenBalance
import pl.toboche.ethchecker.repositories.erc20.Erc20TokenBalanceRepository
import java.util.concurrent.TimeUnit

class Erc20BalancePresenterTest {

    val userInputProcessor = BehaviorProcessor.create<CharSequence>()

    val mockView: Erc20BalanceContract.View = mock {
        on { getUserInput() } doReturn userInputProcessor
    }

    val testScheduler = TestScheduler()

    val stubApplicationScheduler =
        DefaultScheduler(testScheduler, testScheduler)

    val mockErc20TokenBalanceRepository: Erc20TokenBalanceRepository = mock {
        on { getTokenBalancesWithNameContaining(any()) } doReturn Single.just(emptyList())
    }

    val systemUnderTest = Erc20BalancePresenter(
        stubApplicationScheduler,
        mockErc20TokenBalanceRepository
    )

    private val debounceInMillis = 300L

    @Test
    fun `show error loading data`() {
        whenever(mockErc20TokenBalanceRepository.getTokenBalancesWithNameContaining(any()))
            .thenReturn(Single.error(NullPointerException()))

        systemUnderTest.attach(mockView)

        userInputProcessor.onNext("testInput")
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)

        inOrder(mockView, mockErc20TokenBalanceRepository).apply {
            verify(mockView).showProgress()
            mockErc20TokenBalanceRepository.getTokenBalancesWithNameContaining(any())
            verify(mockView).hideProgress()
            verify(mockView).showError("Error refreshing the list")
        }
    }

    @Test
    fun `show loaded data`() {
        systemUnderTest.attach(mockView)

        userInputProcessor.onNext("testInput")
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)

        inOrder(mockView, mockErc20TokenBalanceRepository).apply {
            verify(mockView).showProgress()
            mockErc20TokenBalanceRepository.getTokenBalancesWithNameContaining(any())
            verify(mockView).hideProgress()
            verify(mockView).showBalanceDescriptions(any())
        }
    }

    @Test
    fun `pass valid user input data`() {
        val expectedInputPassedToRepository = "testInput"

        systemUnderTest.attach(mockView)

        userInputProcessor.onNext(expectedInputPassedToRepository)
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)

        mockErc20TokenBalanceRepository.getTokenBalancesWithNameContaining(
            expectedInputPassedToRepository
        )
    }

    @Test
    fun `filter user input if too short`() {
        systemUnderTest.attach(mockView)

        userInputProcessor.onNext("")
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)
        verify(mockErc20TokenBalanceRepository, never()).getTokenBalancesWithNameContaining(any())

        userInputProcessor.onNext("a")
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)
        verify(mockErc20TokenBalanceRepository, never()).getTokenBalancesWithNameContaining(any())

        userInputProcessor.onNext("aa")
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)
        verify(mockErc20TokenBalanceRepository).getTokenBalancesWithNameContaining("aa")
        verifyNoMoreInteractions(mockErc20TokenBalanceRepository)
    }

    @Test
    fun `debounce user input`() {
        systemUnderTest.attach(mockView)

        userInputProcessor.onNext("asd")
        testScheduler.advanceTimeBy(debounceInMillis - 1, TimeUnit.MILLISECONDS)
        verify(mockErc20TokenBalanceRepository, never()).getTokenBalancesWithNameContaining(any())

        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)

        verify(mockErc20TokenBalanceRepository).getTokenBalancesWithNameContaining("asd")
    }

    @Test
    fun `pass results to view with formatted description`() {
        val expectedSymbol = "UNI"
        val expectedBalance = "10"
        val userQuery = "uni"
        whenever(mockErc20TokenBalanceRepository.getTokenBalancesWithNameContaining(userQuery))
            .thenReturn(
                Single.just(
                    listOf(
                        Erc20TokenBalance(
                            expectedSymbol,
                            expectedBalance
                        )
                    )
                )
            )

        systemUnderTest.attach(mockView)

        userInputProcessor.onNext(userQuery)
        testScheduler.advanceTimeBy(debounceInMillis, TimeUnit.MILLISECONDS)

        verify(mockView).showBalanceDescriptions(
            listOf(
                Erc20TokenBalanceDescription(
                    "UNI Balance: 10 UNI"
                )
            )
        )
    }
}