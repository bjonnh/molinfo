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
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.date.*
import net.nprod.molinfo.cache.CacheManager
import net.nprod.molinfo.chemistry.Molecule
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

const val GZIP_PRIORITY: Double = 1.0
const val DEFLATE_PRIORITY: Double = 10.0
const val DEFLATE_MIN_SIZE: Long = 1024

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
    val svgMap = cacheManager.mapStringToString("smiles2svg")

    install(Locations) {
    }

    install(Compression) {
        gzip {
            priority = GZIP_PRIORITY
        }
        deflate {
            priority = DEFLATE_PRIORITY
            minimumSize(DEFLATE_MIN_SIZE)
        }
    }

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

    routing {
        get("/molecule/smiles/{smiles}.svg") {
            call.parameters["smiles"]?.let { smiles ->
                val svg = svgMap.getOrElse(smiles) {
                    Molecule.fromSmiles(smiles).svg()
                }
                call.respondText(svg, contentType = ContentType.Image.SVG)
            } ?: call.response.status(HttpStatusCode.BadRequest)
        }
    }
}
