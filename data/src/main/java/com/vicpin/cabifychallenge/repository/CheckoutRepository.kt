package com.vicpin.cabifychallenge.repository

import com.vicpin.cabifychallenge.api.CheckoutService
import com.vicpin.cabifychallenge.domain.repository.ICheckoutRepository
import com.vicpin.cabifychallenge.mapper.DataItemMapper

class CheckoutRepository(val service: CheckoutService):
    ICheckoutRepository {

    override suspend fun getData() = service.getData().map { DataItemMapper.map(it) }
}