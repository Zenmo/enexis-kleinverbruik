package com.zenmo.kleinverbruik;

import com.zenmo.kleinverbruik.EnexisKleinverbruikKt;
import org.geotools.api.referencing.FactoryException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TestKleinverbruik {
    @Test
    public void testElectricityAndGas() throws FactoryException {
        var pair = EnexisKleinverbruikKt.byBoundigBox(
                new ReferencedEnvelope(
                        5.458133,
                        5.458134,
                        51.46846,
                        51.46847,
                        CRS.decode("EPSG:4326")
                )
        );
        var electriciteit = pair.getElectriciteit();
        assertEquals(4, electriciteit.size());

        var nullRecord = electriciteit.getByPostalCode("6161 HN");
        assertNull(nullRecord);

        var record = pair.getElectriciteit().getByPostalCode("5624 JL");
        assertEquals("5624 JL", record.getPostcodevan());
        assertEquals("5624 JL", record.getPostcodetot());
        assertTrue(record.getSjvgemiddeld() > 100);
        assertTrue(record.getSjvgemiddeld() < 10_000);

        var gas = pair.getGas();
        assertEquals(4, gas.size());

        var nullGasRecord = gas.getByPostalCode("6161 HN");
        assertNull(nullGasRecord);

        var gasRecord = pair.getElectriciteit().getByPostalCode("5624 JL");
        assertEquals("5624 JL", gasRecord.getPostcodevan());
        assertEquals("5624 JL", gasRecord.getPostcodetot());
        assertTrue(gasRecord.getSjvgemiddeld() > 100);
        assertTrue(gasRecord.getSjvgemiddeld() < 10_000);
    }
}
