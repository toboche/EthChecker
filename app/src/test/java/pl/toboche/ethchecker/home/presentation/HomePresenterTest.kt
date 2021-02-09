package pl.toboche.ethchecker.home.presentation

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import pl.toboche.ethchecker.home.HomeContract

class HomePresenterTest {

    val mockView: HomeContract.View = mock()

    val systemUnderTest = HomePresenter()

    @Test
    fun `show ethereum address`() {
        val expectedAddressToShow = "0xde57...78c3"

        systemUnderTest.attach(mockView)

        verify(mockView).showAddress(expectedAddressToShow)
    }
}