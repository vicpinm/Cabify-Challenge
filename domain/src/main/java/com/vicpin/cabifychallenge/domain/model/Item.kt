package com.vicpin.cabifychallenge.domain.model

import com.vicpin.cabifychallenge.model.ItemPrice

data class Item(override val code: String, val name: String, override val price: Double): ItemPrice


