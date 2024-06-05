package com.zenmo.kleinverbruik

class KleinverbruikCollection<E: PostcodeRange>(
    val items: List<E>,
): AbstractList<E>() {
    override val size = items.size
    override fun get(index: Int): E = items[index]

    fun getByPostalCode(postalCode: String): E? {
        validatePostalCode(postalCode)
        return items.find {
            it.postcodevan <= postalCode && postalCode <= it.postcodetot
        }
    }
}

fun validatePostalCode(postalCode: String) {
    if (!postalCode.matches(Regex("^[1-9][0-9]{3} [A-Z]{2}$"))) {
        throw RuntimeException("Postal code does not match the format 1111 AA: $postalCode")
    }
}
