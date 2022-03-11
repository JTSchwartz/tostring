package com.jtschwartz.tostring

interface ExclusionSettingsContract {
	var excludeNull: Boolean
	var excludeUninitialized: Boolean
	var exclusionList: List<String>
	var excludePrivate: Boolean
}

interface MaskSettingsContract {
	var inclusionList: List<String>
	var visibleCount: Int
	var maskCharacter: Char
	var maxMaskCharCount: Int
}

interface MutationSettingsContract {
	var propertyNameMutation: (String) -> String
	var propertyValueMutation: (Any?) -> String
}

interface SymbolSettingsContract {
	var assignment: String
	var closingBracket: String
	var displayClassName: Boolean
	var openingBracket: String
	var separator: String
	var uninitializedValue: String
}
