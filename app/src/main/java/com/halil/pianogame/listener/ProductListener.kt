package com.halil.pianogame.listener

import android.view.View
import com.halil.pianogame.adapter.ShopRecyclerViewAdapter
import com.halil.pianogame.modul.ProductModul

interface ProductListener {
    fun buy(view: View,productModul: ProductModul,adapter: ShopRecyclerViewAdapter?)

    fun use(view:View,productModul: ProductModul,adapter: ShopRecyclerViewAdapter?)

}