package com.zenmo.kleinverbruik

import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.geotools.data.geojson.GeoJSONReader
import org.geotools.geometry.jts.ReferencedEnvelope
import org.locationtech.jts.geom.MultiPolygon

fun byBoundigBox(boundingBox: ReferencedEnvelope): CollectionPair {
    val uri = "https://opendata.enexis.nl/geoserver/wfs".toHttpUrl()
        .newBuilder()
        .addQueryParameter("service", "WFS")
        .addQueryParameter("request", "GetFeature")
        .addQueryParameter("typeName", "postcode_kleinverbruik_e,postcode_kleinverbruik_g")
        .addQueryParameter("count", "2000")
        .addQueryParameter("srsName", "EPSG:4326")
        .addQueryParameter("outputFormat", "application/json")
        .addQueryParameter("bbox", "${boundingBox.minX},${boundingBox.minY},${boundingBox.maxX},${boundingBox.maxY},${boundingBox.coordinateReferenceSystem.identifiers.first()}")
        .build()

    val request = HttpRequest.newBuilder()
        .uri(uri.toUri())
        .build()

    val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream())
    if (!response.headers().map().containsKey("Content-Type")) {
        throw RuntimeException("Response has no Content-Type header: ${response.body().readAllBytes().decodeToString()}")
    }

    if (!response.headers().firstValue("Content-Type").get().contains("application/json")) {
        throw RuntimeException("Response is not JSON: ${response.body().readAllBytes().decodeToString()}")
    }

    val geoJsonReader = GeoJSONReader(response.body())
    val features = geoJsonReader.features.features()

    val electriciteit = mutableListOf<PostcodeKleinverbruikElectriciteit>()
    val gas = mutableListOf<PostcodeKleinverbruikGas>()

    while (features.hasNext()) {
        val feature = features.next()

        val productSoort = feature.getAttribute("productsoort") as String

        if (productSoort == "GAS") {
            gas.add(
                PostcodeKleinverbruikGas(
                    fid = feature.getAttribute("fid") as Int,
                    netbeheerder = feature.getAttribute("netbeheerder") as String,
                    netgebied = feature.getAttribute("netgebied") as String,
                    straatnaam = feature.getAttribute("straatnaam") as String,
                    postcodevan = feature.getAttribute("postcodevan") as String,
                    postcodetot = feature.getAttribute("postcodetot") as String,
                    woonplaats = feature.getAttribute("woonplaats") as String,
                    landcode = feature.getAttribute("landcode") as String,
                    productsoort = feature.getAttribute("productsoort") as String,
                    verbruikssegment = feature.getAttribute("verbruikssegment") as String,
                    aansluitingenaantal = feature.getAttribute("aansluitingenaantal") as Int,
                    leveringsrichtingperc = feature.getAttribute("leveringsrichtingperc").toFloat(),
                    fysiekestatusperc = feature.getAttribute("fysiekestatusperc").toFloat(),
                    soortaansluitingperc = feature.getAttribute("soortaansluitingperc").toFloat(),
                    soortaansluiting = feature.getAttribute("soortaansluiting") as String,
                    sjvgemiddeld = feature.getAttribute("sjvgemiddeld").toFloat(),
                    geometry = feature.defaultGeometry as MultiPolygon,
                    bbox = feature.bounds,
                )
            )
        } else if (productSoort == "ELK") {
            electriciteit.add(
                PostcodeKleinverbruikElectriciteit(
                    fid = feature.getAttribute("fid") as Int,
                    netbeheerder = feature.getAttribute("netbeheerder") as String,
                    netgebied = feature.getAttribute("netgebied") as String,
                    straatnaam = feature.getAttribute("straatnaam") as String,
                    postcodevan = feature.getAttribute("postcodevan") as String,
                    postcodetot = feature.getAttribute("postcodetot") as String,
                    woonplaats = feature.getAttribute("woonplaats") as String,
                    landcode = feature.getAttribute("landcode") as String,
                    productsoort = feature.getAttribute("productsoort") as String,
                    verbruikssegment = feature.getAttribute("verbruikssegment") as String,
                    aansluitingenaantal = feature.getAttribute("aansluitingenaantal") as Int,
                    leveringsrichtingperc = feature.getAttribute("leveringsrichtingperc").toFloat(),
                    fysiekestatusperc = feature.getAttribute("fysiekestatusperc").toFloat(),
                    soortaansluitingperc = feature.getAttribute("soortaansluitingperc").toFloat(),
                    soortaansluiting = feature.getAttribute("soortaansluiting") as String,
                    sjvgemiddeld = feature.getAttribute("sjvgemiddeld").toFloat(),
                    sjvlaagtariefperc = feature.getAttribute("sjvlaagtariefperc").toFloat(),
                    slimmemeterperc = feature.getAttribute("slimmemeterperc").toFloat(),
                    geometry = feature.defaultGeometry as MultiPolygon,
                    bbox = feature.bounds,
                )
            )
        } else {
            throw RuntimeException("Unknown Enexis productsoort: $productSoort")
        }
    }

    return CollectionPair(
        KleinverbruikCollection(electriciteit),
        KleinverbruikCollection(gas),
    )
}

fun Any.toFloat(): Float {
    return (this as Number).toFloat()
}
