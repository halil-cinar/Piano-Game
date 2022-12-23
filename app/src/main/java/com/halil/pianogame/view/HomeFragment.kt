package com.halil.pianogame.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.halil.pianogame.R
import com.halil.pianogame.databinding.FragmentHomeBinding
import com.halil.pianogame.listener.ClickListener
import com.halil.pianogame.sharedPreferences.MySharedPreferences
import com.halil.pianogame.util.backgroundImageLiveData
import com.halil.pianogame.util.cashLiveData
import com.google.gson.Gson
import com.halil.pianogame.util.DownloadFile
import kotlinx.coroutines.*

class HomeFragment : Fragment(),ClickListener {

    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //inflater.inflate(R.layout.fragment_home, container, false)
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickListener=this
        cashLiveData.value=MySharedPreferences(requireContext()).getCash()
        binding.cash= cashLiveData.value?:100
        observeLiveData()
        CoroutineScope(Dispatchers.Default).launch{
            val productModul=MySharedPreferences(requireContext()).getUsedChanges(MySharedPreferences.BACKGROUND_KEY)
            val imageModel=MySharedPreferences(requireContext()).getUsedImageModel(MySharedPreferences.BACKGROUND_IMAGEKEY)
            withContext(Dispatchers.Main){
                if (productModul!=null&&imageModel!=null){
                    val byteArray=Gson().fromJson<ByteArray>(imageModel.image,ByteArray::class.java)
                    val bitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                    backgroundImageLiveData.value=bitmap
                    this@launch.cancel()
            }
        }

        }


    }


    override fun click(view: View, list: String, column: Int, row: Int) {

    }

    override fun buttonClick(view: View) {
        if(view.id==R.id.HomePagePlayButton){
            //play game
               // requireActivity().finish()
            var intent=Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
            //Navigation.findNavController(binding.root).navigate(R.id.gameFragment)


        }
        if(view.id==R.id.HomePageSettings){
            //game Settings

        }
        if (view.id==R.id.HomePageStore){
            Navigation.findNavController(binding.root)
                .navigate(HomeFragmentDirections.actionHomeFragment2ToShopFragment())
        }

    }


    fun observeLiveData(){
        backgroundImageLiveData.observe(viewLifecycleOwner, Observer {
            binding.background=it

        })
    }




}