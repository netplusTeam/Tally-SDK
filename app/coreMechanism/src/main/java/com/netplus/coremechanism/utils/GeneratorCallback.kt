package com.netplus.coremechanism.utils

interface GeneratorCallback<T> {
    fun onQrcodeGenerateSuccess(data: T?)
    fun onQrcodeGenerateFailed(message: String?)
}