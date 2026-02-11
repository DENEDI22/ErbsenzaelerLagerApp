package com.example.team6mobileapp.model

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
                        // Matching old ID format to new fields:
                        // ID.nr.name.menge
                        // We might need to handle missing messeinheit and preis for barcode scans,
                        // or assume default values.
                        Artikel(
                            nr = barcodeSplitted[1].toInt(),
                            name = barcodeSplitted[2],
                            menge = barcodeSplitted[3].toInt(),
                            messeinheit = "Stk.", // Default for scans
                            preis = 0 // Unknown from barcode
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
