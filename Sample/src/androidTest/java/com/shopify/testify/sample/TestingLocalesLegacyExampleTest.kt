/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.testify.sample

import android.os.Build
import androidx.test.filters.SdkSuppress
import com.shopify.testify.ScreenshotRule
import com.shopify.testify.annotation.ScreenshotInstrumentation
import com.shopify.testify.sample.test.TestHarnessActivity
import com.shopify.testify.sample.test.clientDetailsView
import com.shopify.testify.sample.test.getViewState
import org.junit.Rule
import org.junit.Test
import java.util.Locale

/**
 * These tests demonstrate how to test the same Activity on different locales
 * API less than 24
 */
@SdkSuppress(maxSdkVersion = Build.VERSION_CODES.M)
class TestingLocalesLegacyExampleTest {

    @get:Rule var rule = ScreenshotRule(
        activityClass = TestHarnessActivity::class.java,
        launchActivity = false,
        rootViewId = R.id.harness_root
    )

    /**
     * Test the Activity in French
     */
    @ScreenshotInstrumentation
    @Test
    fun testLocaleFrance() {
        assertLocale(Locale.FRANCE)
    }

    /**
     * Test the Activity in Japanese
     */
    @ScreenshotInstrumentation
    @Test
    fun testLocaleJapan() {
        assertLocale(Locale.JAPAN)
    }

    /**
     * Test the Activity in English - Canada variant
     */
    @ScreenshotInstrumentation
    @Test
    fun testLocaleCanada() {
        assertLocale(Locale.CANADA)
    }

    private fun assertLocale(locale: Locale) {
        rule
            .setTargetLayoutId(R.layout.view_client_details)
            .setViewModifications { harnessRoot ->
                rule.activity.getViewState(name = "Locale ${locale.displayName}").let {
                    harnessRoot.clientDetailsView.render(it)
                    rule.activity.title = it.name
                }
            }
            .setLocale(locale)
            .assertSame()
    }
}