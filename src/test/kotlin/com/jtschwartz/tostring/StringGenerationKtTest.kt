package com.jtschwartz.tostring

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal abstract class TestParentClass(
	val a: Int?,
	val b: List<TestDataClass> = listOf(TestDataClass("Jacob", null), TestDataClass("Test", false, -123))
                                       )

internal data class TestDataClass(
	val x: String,
	val y: Boolean?,
	val z: Int = 6698
                                 ) {
	override fun toString(): String = generateString()
}

internal class TestClass(j: List<TestDataClass>, k: Int): TestParentClass(k, j) {
	lateinit var l: String
	lateinit var m: Map<Int, String>
	private var n: Nothing? = null
}

internal class StringGenerationKtTest {
	@Test
	fun `Data class`() {
		val dataClazz = TestDataClass("String", true)
		
		val expected0 = "TestDataClass(x: String, y: true, z: 6698)"
		var actual = dataClazz.toString()
		assertEquals(expected0, actual)
		
		val clazz = TestClass(listOf(dataClazz), 6)
		val expected1 = "TestClass(a: 6, b: [$expected0], l: Not yet initialized, m: Not yet initialized, n: null)"
		actual = clazz.generateString()
		assertEquals(expected1, actual)
		
		val expected2 = "TestClass(a: 6, b: [$expected0], l: Not yet initialized, m: Not yet initialized)"
		actual = clazz.generateString(StringGenerationSettings(ExclusionSettings(excludePrivate = true)))
		assertEquals(expected2, actual)
		
		clazz.l = "Now initialized"
		val expected3 = "TestClass(a: 6, b: [$expected0], l: Now initialized, m: Not yet initialized, n: null)"
		actual = clazz.generateString()
		assertEquals(expected3, actual)
		
		clazz.m = mapOf(0 to "zero")
		val expected4 = "TestClass(a: 6, b: [$expected0], l: Now initialized, m: {0=zero}, n: null)"
		actual = clazz.generateString()
		assertEquals(expected4, actual)
		
		val abstractClazz = object: TestParentClass(-1){}
		val expected5 = "(a: -1, b: [TestDataClass(x: Jacob, y: null, z: 6698), TestDataClass(x: Test, y: false, z: -123)])"
		actual = abstractClazz.generateString(StringGenerationSettings(symbolSettings = SymbolSettings(displayClassName = false)))
		assertEquals(expected5, actual)
	}
}
