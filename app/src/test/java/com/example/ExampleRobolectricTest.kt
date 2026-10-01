package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.ExpressionEvaluator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Calculator Pro", appName)
    }

    @Test
    fun `standard arithmetic evaluation`() {
        val res = ExpressionEvaluator.evaluateStandard("12 + 8 * 2")
        assertTrue(res.isSuccess)
        assertEquals(28.0, res.getOrThrow(), 0.0001)
    }

    @Test
    fun `scientific evaluation`() {
        val res = ExpressionEvaluator.evaluateScientific("sin(0)", isRadMode = true)
        assertTrue(res.isSuccess)
        assertEquals(0.0, res.getOrThrow(), 0.0001)
    }
}
