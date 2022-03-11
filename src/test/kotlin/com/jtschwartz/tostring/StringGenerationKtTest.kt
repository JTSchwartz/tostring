package com.jtschwartz.tostring

import org.junit.jupiter.api.Test
import kotlin.test.*

internal abstract class TestParentClass(
	val a: Int?,
	val b: List<TestDataClass> = listOf(TestDataClass("Jacob", null), TestDataClass("Test", false, -123))
                                       )

internal data class TestDataClass(
	val x: String,
	val y: Boolean?,
	val z: Int = 6698
                                 )

internal class TestClass(j: List<TestDataClass>, k: Int): TestParentClass(k, j) {
	private var n: Nothing? = null
	lateinit var m: Map<Int, String>
}

internal class StringGenerationKtTest {
	companion object {
		val basicDataObject = TestDataClass("String", true)
		var basicObject = TestClass(emptyList(), 0)
	}
	
	@Test
	fun `Basic usage`() {
		val expected = "TestDataClass(x: String, y: true, z: 6698)"
		val actual = basicDataObject.generateString()
		assertEquals(expected, actual)
	}
	
	@Test
	fun `Exclusion list`() {
		val expected = "TestDataClass(z: 6698)"
		val actual = basicDataObject.generateString(StringGenerationSettings(ExclusionSettings(exclusionList = listOf("x", "y"))))
		assertEquals(expected, actual)
	}
	
	@Test
	fun `Exclude private`() {
		val expected = "n:"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(ExclusionSettings(excludePrivate = true)))
		assertFalse(actual.contains(expected))
	}
	
	@Test
	fun `Exclude null`() {
		val expected = "n:"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(ExclusionSettings(excludeNull = true)))
		assertFalse(actual.contains(expected))
	}
	
	@Test
	fun `Exclude uninitialized`() {
		val expected = "m:"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(ExclusionSettings(excludeUninitialized = true)))
		assertFalse(actual.contains(expected))
	}
	
	@Test
	fun `Name mutation`() {
		val expected = listOf("[X]", "[Y]", "[Z]")
		val mutator: (String) -> String = { "[${it.uppercase()}]" }
		
		var actual = basicDataObject.generateString()
		assertFalse(expected.any { actual.contains(it) })
		
		actual = basicDataObject.generateString(
			StringGenerationSettings(mutationSettings = MutationSettings(propertyNameMutation = mutator)))
		assertTrue(expected.all { actual.contains(it) })
	}
	
	@Test
	fun `Value mutation`() {
		val expected = listOf("\"String\"", "6698.0")
		val mutator: (Any?) -> String = {
			when (it) {
				is String     -> "\"$it\""
				is Number -> it.toDouble().toString()
				else  -> it.toString()
			}
		}
		
		var actual = basicDataObject.generateString()
		assertFalse(expected.any { actual.contains(it) })
		
		actual = basicDataObject.generateString(
			StringGenerationSettings(mutationSettings = MutationSettings(propertyValueMutation = mutator)))
		assertTrue(expected.all { actual.contains(it) })
	}
	
	@Test
	fun `Minimal postfix masking`() {
		val expected = "Str***"
		var actual = basicDataObject.generateString()
		assertFalse(actual.contains(expected))
		
		actual = basicDataObject.generateString(StringGenerationSettings(postfixMaskSettings = PostfixMaskSettings(listOf("x"), 3, '*')))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `Limit postfix masking length`() {
		val expected = "Str*"
		var actual = basicDataObject.generateString()
		assertFalse(actual.contains(expected))
		
		actual = basicDataObject.generateString(StringGenerationSettings(postfixMaskSettings = PostfixMaskSettings(listOf("x"), 3, '*', 1)))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `Minimal prefix masking`() {
		val expected = "***ing"
		var actual = basicDataObject.generateString()
		assertFalse(actual.contains(expected))
		
		actual = basicDataObject.generateString(StringGenerationSettings(prefixMaskSettings = PrefixMaskSettings(listOf("x"), 3, '*')))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `Limit prefix masking length`() {
		val expected = "*ing"
		var actual = basicDataObject.generateString()
		assertFalse(actual.contains(expected))
		
		actual = basicDataObject.generateString(StringGenerationSettings(prefixMaskSettings = PrefixMaskSettings(listOf("x"), 3, '*', 1)))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `Mask max longer than mask`() {
		val expected = "***ing"
		val actual = basicDataObject.generateString(StringGenerationSettings(prefixMaskSettings = PrefixMaskSettings(listOf("x"), 3, '*', 4)))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `String shorter than mask`() {
		val expected = "String"
		val actual = basicDataObject.generateString(StringGenerationSettings(prefixMaskSettings = PrefixMaskSettings(listOf("x"), 7, '*', 4)))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `Masked string is empty`() {
		val data = TestDataClass("", true, 0)
		val expected = "x: ,"
		val actual = data.generateString(StringGenerationSettings(prefixMaskSettings = PrefixMaskSettings(listOf("x"), 7, '*', 4)))
		assertTrue(actual.contains(expected))
	}
	
	@Test
	fun `Display name`() {
		val expected = "TestClass"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(displayClassName = false)))
		assertFalse(actual.contains(expected))
	}
	
	@Test
	fun `Assignment symbol`() {
		val expected = StringGenerationDefaults.symbolSettings.assignment
		val symbol = " -> "
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(symbol)))
		assertFalse(actual.contains(expected))
		assertTrue(actual.contains(symbol))
	}
	
	@Test
	fun `Separator symbol`() {
		val expected = StringGenerationDefaults.symbolSettings.separator
		val symbol = " | "
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(separator = symbol)))
		assertFalse(actual.contains(expected))
		assertTrue(actual.contains(symbol))
	}
	
	@Test
	fun `Opening bracket symbol`() {
		val expected = StringGenerationDefaults.symbolSettings.openingBracket
		val symbol = "{"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(openingBracket = symbol)))
		assertFalse(actual.contains(expected))
		assertTrue(actual.contains(symbol))
	}
	
	@Test
	fun `Closing bracket symbol`() {
		val expected = StringGenerationDefaults.symbolSettings.closingBracket
		val symbol = "}"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(closingBracket = symbol)))
		assertFalse(actual.contains(expected))
		assertTrue(actual.contains(symbol))
	}
	
	@Test
	fun `Uninitialized symbol`() {
		val expected = StringGenerationDefaults.symbolSettings.uninitializedValue
		val symbol = "[X]"
		var actual = basicObject.generateString()
		assertTrue(actual.contains(expected))
		
		actual = basicObject.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(uninitializedValue = symbol)))
		assertFalse(actual.contains(expected))
		assertTrue(actual.contains(symbol))
	}
}
