package com.nhut.hoshi.features.reader

import com.nhut.hoshi.features.dictionary.LookupPopupHtml
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidFontReferencesTest {
    @Test
    fun readerCssUsesAndroidJapaneseFontsWithoutIosOnlyNames() {
        val css = ReaderContentStyles.styleTag()

        assertTrue(css.contains("Noto Serif CJK JP"))
        assertFalse(css.contains(iosOnlyJapaneseFontPrefix))
    }

    @Test
    fun lookupPopupHtmlUsesAndroidJapaneseFontsWithoutIosOnlyNames() {
        val html = LookupPopupHtml.renderIframeDocument()

        assertTrue(html.contains("Noto Sans CJK JP"))
        assertTrue(html.contains("""<html lang="ja""""))
        assertFalse(html.contains(iosOnlyJapaneseFontPrefix))
    }

    private companion object {
        const val iosOnlyJapaneseFontPrefix = "Hiragino"
    }
}
