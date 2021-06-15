/*
 *
 * SPDX-License-Identifier: MIT License
 *
 * Copyright (c) 2021 Jonathan Bisson
 *
 */

package net.nprod.molinfo.cache

import org.redisson.Redisson
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RLocalCachedMap
import org.redisson.api.RedissonClient
import org.redisson.config.Config

class CacheManager {
    val config = Config()
    private var instance: RedissonClient
    var host = System.getenv("MOLINFO_REDIS_HOST") ?: "127.0.0.1"

    init {
        config.useSingleServer().address = "redis://$host:6379"
        config.threads = 2
        config.nettyThreads = 2

        instance = Redisson.create(config)

    }

    fun mapStringToString(name: String): RLocalCachedMap<String, String> =
        instance.getLocalCachedMap(name, LocalCachedMapOptions.defaults())
    //fun mapStringToString(name: String): RMap<String, String> = instance.getMap(name)
}
