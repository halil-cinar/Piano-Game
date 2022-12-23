package com.halil.pianogame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.halil.pianogame.R
import com.halil.pianogame.databinding.ShopRecyclerViewRowBinding
import com.halil.pianogame.listener.ProductListener
import com.halil.pianogame.modul.ProductModul

class ShopRecyclerViewAdapter(var list: ArrayList<ProductModul>,var listener:ProductListener):
    RecyclerView.Adapter<ShopRecyclerViewAdapter.viewHolder>() {

    class viewHolder(var view: ShopRecyclerViewRowBinding):RecyclerView.ViewHolder(view.root){
        fun changeProduct(productModul: ProductModul){
            view.product=productModul
        }
        fun changeCash(cash:Int){
            view.cash=cash
        }
        fun changeListener(listener: ProductListener){
            view.listener=listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var inflater=LayoutInflater.from(parent.context)
        var binding:ShopRecyclerViewRowBinding=DataBindingUtil.inflate(inflater, R.layout.shop_recycler_view_row,parent,false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.changeProduct(list[position])
        holder.changeCash(100)
        holder.changeListener(listener)
        holder.view.adapter=this
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun changeList(list: ArrayList<ProductModul>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}