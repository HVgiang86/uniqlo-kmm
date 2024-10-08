package com.gianghv.uniqlo.util

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger

fun getAsyncImageLoader(context: PlatformContext)=
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).crossfade(true).logger(DebugLogger()).build()
