package com.zenmo.kleinverbruik

import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.referencing.CRS
import kotlin.test.Test
import kotlin.test.assertEquals

class TestKleinverbruikKt {
    @Test
    fun testElectricityAndGas() {
        byBoundigBox(
            ReferencedEnvelope(
                5.458133,
                5.458134,
                51.46846,
                51.46847,
                CRS.decode("EPSG:4326"),
            )
        ).let { (electricity, gas) ->
            assertEquals(4, electricity.size)
            assertEquals(4, gas.size)
        }
    }
}