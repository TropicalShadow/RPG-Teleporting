package club.tesseract.rpgteleporting.utils

object InventoryUtils {

    fun calculateInventorySize(size: Int): Int{
        return size + (9 - size % 9)
    }



}