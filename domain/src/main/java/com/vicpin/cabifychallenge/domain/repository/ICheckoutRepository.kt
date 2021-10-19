package com.vicpin.cabifychallenge.domain.repository

import com.vicpin.cabifychallenge.domain.model.Item

interface ICheckoutRepository {
    suspend fun getData(): List<Item>
}