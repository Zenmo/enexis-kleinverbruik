package com.zenmo.kleinverbruik

data class CollectionPair(
    val electriciteit: KleinverbruikCollection<PostcodeKleinverbruikElectriciteit>,
    val gas: KleinverbruikCollection<PostcodeKleinverbruikGas>,
)
