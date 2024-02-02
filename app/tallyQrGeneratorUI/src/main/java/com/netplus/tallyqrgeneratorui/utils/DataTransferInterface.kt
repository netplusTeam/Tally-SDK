package com.netplus.tallyqrgeneratorui.utils

import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse

interface DataTransferInterface {

    fun transferData(generateQrcodeResponse: GenerateQrcodeResponse?)
}