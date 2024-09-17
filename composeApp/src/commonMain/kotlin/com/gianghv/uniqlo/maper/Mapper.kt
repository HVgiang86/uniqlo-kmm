package com.gianghv.uniqlo.maper

import com.gianghv.uniqlo.domain.BaseData
import com.gianghv.uniqlo.domain.BaseModel

abstract class DataMapper<T : BaseData, R : BaseModel> {
    abstract fun map(data: T): R

    fun nullableMap(data: T?): R? {
        return data?.let { map(it) }
    }

    fun collectionMap(collection: Collection<T>): List<R> {
        return collection.map { map(it) }
    }

    fun nullableCollectionMap(collection: Collection<T>?): List<R>? {
        return collection?.map { map(it) }
    }
}
