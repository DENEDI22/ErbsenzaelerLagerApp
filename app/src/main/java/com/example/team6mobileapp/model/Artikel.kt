package com.example.team6mobileapp.model

data class ArtikelResponse(
    val message: String,
    val artikel: Artikel
)

data class ArtikelUpdateRequest(
    val name: String,
    val messeinheit: String,
    val preis: Int,
    val menge: Int
)

data class ArtikelCreateRequest(
    val nr: Int? = null,
    val name: String,
    val messeinheit: String,
    val preis: Int,
    val menge: Int
)

data class Artikel(
    val nr: Int,
    val name: String,
    val messeinheit: String,
    val preis: Int,
    val menge: Int
) {
    companion object {
        fun fromBarcode(barcodeValue: String): Artikel? {
            if (barcodeValue.isNotEmpty() && barcodeValue.trim().startsWith("ID")) {
                return try {
                    val barcodeSplitted = barcodeValue.split(".")
                    if (barcodeSplitted.size >= 4) {
                        Artikel(
                            nr = barcodeSplitted[1].toInt(),
                            name = barcodeSplitted[2],
                            menge = barcodeSplitted[3].toInt(),
                            messeinheit = "Stk.",
                            preis = 0
                        )
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    null
                }
            }
            return null
        }
    }
}
