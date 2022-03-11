package com.jtschwartz.tostring

import com.jtschwartz.chorecore.attempt
import com.jtschwartz.chorecore.div
import java.io.File
import java.io.FileInputStream
import java.util.*

object StringGenerationDefaults {
	val exclusionSettings = object: ExclusionSettingsContract {
		override var excludeNull: Boolean = false
		override var excludeUninitialized: Boolean = false
		override var exclusionList: List<String> = emptyList()
		override var excludePrivate: Boolean = false
	}
	
	val postfixMaskSettings = object: MaskSettingsContract {
		override var inclusionList: List<String> = emptyList()
		override var visibleCount: Int = 3
		override var maskCharacter: Char = '*'
		override var maxMaskCharCount: Int = 0
	}
	
	val prefixMaskSettings = object: MaskSettingsContract {
		override var inclusionList: List<String> = emptyList()
		override var visibleCount: Int = 3
		override var maskCharacter: Char = '*'
		override var maxMaskCharCount: Int = 0
	}
	
	val mutationSettings = object: MutationSettingsContract {
		override var propertyNameMutation: (String) -> String = { it }
		override var propertyValueMutation: (Any?) -> String = { it?.toString() ?: "null" }
	}
	
	val symbolSettings = object: SymbolSettingsContract {
		override var assignment: String = "="
		override var closingBracket: String = ")"
		override var displayClassName: Boolean = true
		override var openingBracket: String = "("
		override var separator: String = ", "
		override var uninitializedValue: String = "UNINITIALIZED"
	}
	
	init {
		attempt {
			val file = File(javaClass.classLoader.getResource("tostring.properties").path)
			Properties().apply {
				FileInputStream(file).use { load(it) }
			}.entries.forEach { (name, value) ->
				try {
					when (name as String) {
						"tostring.exclude.private"            -> exclusionSettings.excludePrivate = (value as String).equals("true", ignoreCase = true)
						"tostring.exclude.null"            -> exclusionSettings.excludeNull = (value as String).equals("true", ignoreCase = true)
						"tostring.exclude.uninit"          -> exclusionSettings.excludeUninitialized = (value as String).equals("true", ignoreCase = true)
						"tostring.exclude.list"            -> exclusionSettings.exclusionList = (value as String) / ','
						"tostring.mask.post.list"          -> postfixMaskSettings.inclusionList = (value as String) / ','
						"tostring.mask.post.visible"       -> postfixMaskSettings.visibleCount = (value as String).toInt()
						"tostring.mask.post.mask"          -> postfixMaskSettings.maskCharacter = (value as String)[0]
						"tostring.mask.post.max"           -> postfixMaskSettings.maxMaskCharCount = (value as String).toInt()
						"tostring.mask.pre.list"           -> prefixMaskSettings.inclusionList = (value as String) / ','
						"tostring.mask.pre.visible"        -> prefixMaskSettings.visibleCount = (value as String).toInt()
						"tostring.mask.pre.mask"           -> prefixMaskSettings.maskCharacter = (value as String)[0]
						"tostring.mask.pre.max"            -> prefixMaskSettings.maxMaskCharCount = (value as String).toInt()
						"tostring.symbols.assignment"      -> symbolSettings.assignment = value as String
						"tostring.symbols.closing"         -> symbolSettings.closingBracket = value as String
						"tostring.symbols.display_name"    -> symbolSettings.displayClassName = (value as String).equals("true", ignoreCase = true)
						"tostring.symbols.opening"         -> symbolSettings.openingBracket = value as String
						"tostring.symbols.separator"       -> symbolSettings.separator = value as String
						"tostring.symbols.uninit"          -> symbolSettings.uninitializedValue = value as String
					}
				} catch (_: Exception) {
					throw StringGenerationDefaultsException(name as String)
				}
			}
		}
	}
}
