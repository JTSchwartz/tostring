package com.jtschwartz.tostring

class StringGenerationException: Exception()

class StringGenerationDefaultsException(private val property: String): Exception() {
	override fun toString() = "There was an error parsing $property"
}
