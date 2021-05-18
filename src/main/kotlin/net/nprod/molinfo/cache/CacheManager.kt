/*
 *
 * SPDX-License-Identifier: MIT License
 *
 * Copyright (c) 2021 Jonathan Bisson
 *
 */

package net.nprod.molinfo.cache

import org.redisson.Redisson
import org.redisson.api.RMap
import org.redisson.api.RedissonClient
import org.redisson.config.Config

class CacheManager {
    val config = Config()
    private var instance: RedissonClient

    init {
        config.useSingleServer().address = "redis://redis:6379"
        instance = Redisson.create(config)
    }

    fun mapStringToString(name: String): RMap<String, String> = instance.getMap(name)
}
