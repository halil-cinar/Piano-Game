package com.halil.pianogame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.halil.pianogame.R
import com.halil.pianogame.adapter.ShopRecyclerViewAdapter
import com.halil.pianogame.databinding.FragmentShopBinding
import com.halil.pianogame.listener.ProductListener
import com.halil.pianogame.modul.ProductModul
import com.halil.pianogame.sharedPreferences.MySharedPreferences
import com.halil.pianogame.util.backgroundImageLiveData
import com.halil.pianogame.util.cashLiveData
import com.halil.pianogame.viewModel.ShopFragmentViewModel
import kotlinx.android.synthetic.main.fragment_shop.*

class ShopFragment : Fragment() {
    lateinit var binding: FragmentShopBinding
    lateinit var adapter: ShopRecyclerViewAdapter
    lateinit var viewModel: ShopFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_shop, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProviders.of(this)[ShopFragmentViewModel::class.java]
        observeLiveData()
        viewModel.start(requireContext())
        adapter= ShopRecyclerViewAdapter(arrayListOf(),productListener)
        shopRecyclerView.adapter=adapter



    }
    fun observeLiveData(){

        cashLiveData.observe(viewLifecycleOwner, Observer {
            binding.cash=it
             MySharedPreferences(requireContext()).saveCash(it)
        })
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer {
            adapter.changeList(it)
            viewModel.saveProductList(it)
            //viewModel.saveImageList(viewModel.imageModelList)

        })
        backgroundImageLiveData.observe(viewLifecycleOwner, Observer {
            binding.background=it
        })

    }
    var productListener=object :ProductListener{
        override fun buy(
            view: View,
            productModul: ProductModul,
            adapter: ShopRecyclerViewAdapter?
        ) {
            viewModel.buy(productModul)
        }

        override fun use(
            view: View,
            productModul: ProductModul,
            adapter: ShopRecyclerViewAdapter?
        ) {
            viewModel.use(productModul)
        }

    }



}