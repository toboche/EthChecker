package pl.toboche.ethchecker.base

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PresenterTest {
    class TestPresenter<View> : Presenter<View>()

    val mockView = mock<Any>()
    val systemUnderTest = TestPresenter<Any>()

    @Test
    fun `should attach view`() {
        systemUnderTest.attach(mockView)

        assertThat(systemUnderTest.view)
            .isEqualTo(mockView)
    }

    @Test
    fun `should detach view`() {
        systemUnderTest.attach(mockView)
        systemUnderTest.detach()

        assertThat(systemUnderTest.view)
            .isNull()
    }
}