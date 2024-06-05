package com.zenmo.kleinverbruik

import org.geotools.api.geometry.BoundingBox
import org.locationtech.jts.geom.MultiPolygon

data class PostcodeKleinverbruikElectriciteit(
    val fid: Int,
    val netbeheerder: String,
    val netgebied: String,
    val straatnaam: String,
    override val postcodevan: String, // "1111 AA"
    override val postcodetot: String,
    val woonplaats: String,
    val landcode: String, // "NL"
    val productsoort: String, // "ELK"
    val verbruikssegment: String, // "KVB"
    val aansluitingenaantal: Int,
    val leveringsrichtingperc: Float,
    val fysiekestatusperc: Float,
    val soortaansluitingperc: Float,
    val soortaansluiting: String, // TODO: enum
    val sjvgemiddeld: Float,
    val sjvlaagtariefperc: Float,
    val slimmemeterperc: Float,
    val geometry: MultiPolygon,
    val bbox: BoundingBox,
): PostcodeRange
