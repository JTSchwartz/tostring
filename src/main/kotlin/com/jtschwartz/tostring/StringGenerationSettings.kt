package com.jtschwartz.tostring

val DefaultExclusions = StringGenerationDefaults.exclusionSettings
val DefaultMutations = StringGenerationDefaults.mutationSettings
val DefaultPostfixMask = StringGenerationDefaults.postfixMaskSettings
val DefaultPrefixMask = StringGenerationDefaults.prefixMaskSettings
val DefaultSort = StringGenerationDefaults.sortSettings
val DefaultSymbols = StringGenerationDefaults.symbolSettings

data class StringGenerationSettings(
	var exclusionSettings: ExclusionSettings = ExclusionSettings(),
	var mutationSettings: MutationSettings = MutationSettings(),
	var postfixMaskSettings: PostfixMaskSettings = PostfixMaskSettings(),
	var prefixMaskSettings: PrefixMaskSettings = PrefixMaskSettings(),
	var sortSettings: SortSettings = SortSettings(),
	var symbolSettings: SymbolSettings = SymbolSettings())

data class ExclusionSettings(
	override var excludeNull: Boolean = DefaultExclusions.excludeNull,
	override var excludeNullAndUninitialized: Boolean = DefaultExclusions.excludeNullAndUninitialized,
	override var excludeUninitialized: Boolean = DefaultExclusions.excludeUninitialized,
	override var exclusionList: List<String> = DefaultExclusions.exclusionList,
	override var excludePrivate: Boolean = DefaultExclusions.excludePrivate
                            ): ExclusionSettingsContract

data class PostfixMaskSettings(
	override var inclusionList: List<String> = DefaultPostfixMask.inclusionList,
	override var visibleCount: Int = DefaultPostfixMask.visibleCount,
	override var maskCharacter: Char = DefaultPostfixMask.maskCharacter,
	override var maxMaskCharCount: Int = DefaultPostfixMask.maxMaskCharCount
                              ): MaskSettingsContract

data class PrefixMaskSettings(
	override var inclusionList: List<String> = DefaultPrefixMask.inclusionList,
	override var visibleCount: Int = DefaultPrefixMask.visibleCount,
	override var maskCharacter: Char = DefaultPrefixMask.maskCharacter,
	override var maxMaskCharCount: Int = DefaultPrefixMask.maxMaskCharCount
                             ): MaskSettingsContract

data class MutationSettings(
	override var propertyNameMutation: (String) -> String = DefaultMutations.propertyNameMutation,
	override var propertyValueMutation: (Any?) -> String = DefaultMutations.propertyValueMutation
                           ): MutationSettingsContract

data class SortSettings(
	override var method: SortMethod = DefaultSort.method
                       ): SortSettingsContract

data class SymbolSettings(
	override var assignment: String = DefaultSymbols.assignment,
	override var closingBracket: String = DefaultSymbols.closingBracket,
	override var displayClassName: Boolean = DefaultSymbols.displayClassName,
	override var openingBracket: String = DefaultSymbols.openingBracket,
	override var separator: String = DefaultSymbols.separator,
	override var uninitializedValue: String = DefaultSymbols.uninitializedValue
                         ): SymbolSettingsContract
