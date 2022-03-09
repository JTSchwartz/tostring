package com.jtschwartz.tostring

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow


internal class StringGenerationDefaultsTest {
	@Test
	fun `Ensure properties are read properly`() {
		assertDoesNotThrow {
			StringGenerationDefaults
		}

		assertEquals(": ", StringGenerationDefaults.symbolSettings.assignment)
		assertEquals(", ", StringGenerationDefaults.symbolSettings.separator)
		assertNotEquals("UNINITIALIZED", StringGenerationDefaults.symbolSettings.uninitializedValue)
		assertEquals(4, StringGenerationDefaults.prefixMaskSettings.visibleCount)
	}
	
	@Test
	fun `Validate that defaults are used properly`() {
		StringGenerationSettings().apply {
			assertEquals(4, prefixMaskSettings.visibleCount)
		}
		
		StringGenerationDefaults.prefixMaskSettings.visibleCount = 3
		StringGenerationSettings().apply {
			assertEquals(3, prefixMaskSettings.visibleCount)
		}
		StringGenerationDefaults.prefixMaskSettings.visibleCount = 4

		StringGenerationSettings(prefixMaskSettings = PrefixMaskSettings(visibleCount = 5)).apply {
			assertEquals(5, prefixMaskSettings.visibleCount)
		}
	}
}
