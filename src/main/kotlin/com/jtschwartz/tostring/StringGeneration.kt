package com.jtschwartz.tostring

import com.jtschwartz.chorecore.isNull
import java.lang.reflect.InvocationTargetException
import kotlin.math.min
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

inline fun <reified T: Any> T.generateString(
	settings: StringGenerationSettings = StringGenerationSettings()
                                            ): String {
	val (exclusionSettings, mutationSettings, postfixMaskSettings, prefixMaskSettings, symbols) = settings
	
	val (excludeNull, excludeUninitialized, exclusionList, excludePrivate) = exclusionSettings
	val (nameMutation, valueMutation) = mutationSettings
	val (postInclusionList, postMaskCount, postMaskChar, postCharMax) = postfixMaskSettings
	val (preInclusionList, preMaskCount, preMaskChar, preCharMax) = prefixMaskSettings
	return try {
		T::class.memberProperties.filter { it.name !in exclusionList }
			.map { property ->
				property.isAccessible = !excludePrivate
				val name = property.name
				val prefix = "${nameMutation(name)}${symbols.assignment}"
				try {
					var value = property.getter.call(this)
					if (value.isNull() && excludeNull) throw StringGenerationException()
					value = valueMutation(value)
					
					if (name in postInclusionList) {
						value = value.mask(postMaskCount, postCharMax, postMaskChar)
					}
					
					if (name in preInclusionList) {
						value = value.reversed().mask(preMaskCount, preCharMax, preMaskChar).reversed()
					}
					
					"$prefix$value"
				} catch (_: InvocationTargetException) {
					if (excludeUninitialized) "" else "$prefix${symbols.uninitializedValue}"
				} catch (_: Exception) {
					""
				}
			}.filter { it.isNotBlank() }.joinToString(symbols.separator, "${if (symbols.displayClassName) T::class.simpleName else ""}${symbols.openingBracket}", symbols.closingBracket)
	} catch (e: Exception) {
		"An error occurred while generating string:\n${e.printStackTrace()}"
	}
}

fun String.mask(count: Int, max: Int, char: Char): String {
	return try {
		if (length >= count && max > 0) {
			"${this.substring(0 until count)}${char.toString().repeat(min(max, (length - count)))}"
		} else if (length >= 0) {
			"${this.substring(0 until count)}${char.toString().repeat(length - count)}"
		} else this
	} catch (_: Exception) {
		this
	}
}
