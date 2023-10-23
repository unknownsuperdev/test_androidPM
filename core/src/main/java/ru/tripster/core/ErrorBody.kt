package ru.tripster.core

data class ErrorBody(val non_field_errors: List<String>)
data class ErrorDetail(val detail: String?)
data class AuthErrorBody(val code: String?, val message: String?)
data class PhoneErrorBody(val phone: List<String>)
