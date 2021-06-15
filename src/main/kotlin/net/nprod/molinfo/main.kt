/*
 *
 * SPDX-License-Identifier: MIT License
 *
 * Copyright (c) 2021 Jonathan Bisson
 *
 */

package net.nprod.molinfo

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.date.*
import net.nprod.molinfo.cache.CacheManager
import net.nprod.molinfo.chemistry.MoleculeManager
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * How long do we ask the clients to cache for
 */
const val CACHE_IN_DAYS: Int = 8

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@ExperimentalTime
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val cacheManager = CacheManager()
    val moleculeManager = MoleculeManager()

    install(CORS) {
        method(HttpMethod.Options)
        allowCredentials = false
        anyHost() // If you put that publicly accessible like that, you may get a LOT of requests from … people
    }

    install(CachingHeaders) {
        options { outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Image.SVG -> CachingOptions(
                    CacheControl.MaxAge(maxAgeSeconds = Duration.days(CACHE_IN_DAYS).inWholeSeconds.toInt()),
                    expires = null as? GMTDate?
                )
                else -> null
            }
        }
    }

    val smilesCache = cacheManager.mapStringToString("smiles2svg")
    val smiles2inchiCache = cacheManager.mapStringToString("smiles2inchikey")
    val smiles2exactCache = cacheManager.mapStringToString("smiles2exactmass")
    val smiles2averageCache = cacheManager.mapStringToString("smiles2averagemass")

    routing {
        get("/ping") {
            call.respondText("pong", contentType = ContentType.Text.Plain)
        }

        get("/molecule/smiles/{smiles}.svg") {
            call.parameters["smiles"]?.let { smiles ->
                val svg = smilesCache.getOrPut(smiles) {
                    moleculeManager.moleculeFromSmiles(smiles).svg()
                }
                call.respondText(svg, contentType = ContentType.Image.SVG)
            } ?: call.response.status(HttpStatusCode.BadRequest)
        }

        get("/molecule/smiles/{smiles}/inchikey") {
            call.parameters["smiles"]?.let { smiles ->
                val inchikey = smiles2inchiCache.getOrPut(smiles) {
                    moleculeManager.moleculeFromSmiles(smiles).inchikey
                }
                call.respondText(inchikey, contentType = ContentType.Text.Plain)
            } ?: call.response.status(HttpStatusCode.BadRequest)
        }

        get("/molecule/smiles/{smiles}/exactmass") {
            call.parameters["smiles"]?.let { smiles ->
                val mass = smiles2exactCache.getOrPut(smiles) {
                    moleculeManager.moleculeFromSmiles(smiles).exactmass.toString()
                }
                call.respondText(mass, contentType = ContentType.Text.Plain)
            } ?: call.response.status(HttpStatusCode.BadRequest)
        }

        get("/molecule/smiles/{smiles}/averagemass") {
            call.parameters["smiles"]?.let { smiles ->
                val mass = smiles2averageCache.getOrPut(smiles) {
                    moleculeManager.moleculeFromSmiles(smiles).averagemass.toString()
                }
                call.respondText(mass, contentType = ContentType.Text.Plain)
            } ?: call.response.status(HttpStatusCode.BadRequest)
        }
    }
}
