package com.nhut.hoshi.features.dictionary

import com.nhut.hoshi.features.audio.LocalAudioRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class ProcessTextLookupDependenciesTest {
    @Test
    fun dependenciesInjectLocalAudioRepository() {
        val constructor = ProcessTextLookupDependencies::class.java.declaredConstructors.single()

        assertTrue(
            "Process Text lookup should use the Hilt-provided LocalAudioRepository.",
            constructor.parameterTypes.contains(LocalAudioRepository::class.java),
        )
    }
}
