package com.vicpin.cabifychallenge.domain.usecase

import com.vicpin.cabifychallenge.domain.model.Item
import com.vicpin.cabifychallenge.domain.repository.ICheckoutRepository

class GetDataCase(val repository: ICheckoutRepository): UseCase<List<Item>>() {


    override suspend fun doOnBackground() = repository.getData()


}