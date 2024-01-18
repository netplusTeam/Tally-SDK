package com.netplus.coremechanism.utils

interface TallyResponseCallback<T> {
    fun success(data: T?)
    fun failed(message: String?)
}