package com.halil.pianogame.view

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.halil.pianogame.R
import com.halil.pianogame.databinding.FragmentGameBinding
import com.halil.pianogame.listener.ClickListener
import com.halil.pianogame.sharedPreferences.MySharedPreferences
import com.halil.pianogame.util.*
import com.halil.pianogame.viewModel.GameFragmentViewModel
import com.google.gson.Gson

class GameFragment : Fragment() ,ClickListener{
lateinit var viewModel: GameFragmentViewModel
lateinit var binding:FragmentGameBinding
var buttonVisibilityList= arrayListOf<Boolean?>(false)
var level=1
    var noteMap= hashMapOf<String,MediaPlayer>()
    // "la","re","la","re","la","si","la","la","re","la","re","la","si","la","re","re","re","mi","re","do","si"
   // arrayListOf<String>(
    //        "do-do","do","do","do-re-mi-re","si","si","si","si-do-re-do","la","la","la","la-si-do-si","sol","sol","mi-re-do-si",
    //        "do-do","do-do","do-do-do","do","si","si","si","si-la","","la","la","la","la","si","si","si"
    //    )
    var song= arrayListOf<String>()

    var songPosition=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //inflater.inflate(R.layout.fragment_game, container, false)
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

viewModel=ViewModelProviders.of(this).get(GameFragmentViewModel::class.java)
        viewModel.start(requireContext())
        binding.clickListener=this
        binding.background= backgroundImageLiveData.value
        binding.buttonVisibilityList=buttonVisibilityList.toTypedArray()
        var noteMap= hashMapOf<String,MediaPlayer>(
            "do" to MediaPlayer.create(requireContext(),R.raw.d),
            "fa" to MediaPlayer.create(requireContext(),R.raw.fa),
            "la" to MediaPlayer.create(requireContext(),R.raw.la),
            "mi" to MediaPlayer.create(requireContext(),R.raw.mi),
            "re" to MediaPlayer.create(requireContext(),R.raw.re),
            "si" to MediaPlayer.create(requireContext(),R.raw.si),
            "sol" to MediaPlayer.create(requireContext(),R.raw.sol),
            )
        this.noteMap.clear()
        this.noteMap.putAll(noteMap)
        song.clear()
        song.addAll(Songs().createRandomSongs(100,noteMap))
        Log.d(TAG, "onViewCreated: "+song)


        observeLiveData()

    }

fun observeLiveData(){

    viewModel.buttonLiveData.observe(viewLifecycleOwner, Observer {
        binding.visibilityList= Gson().toJson(it)
it.forEach {
    //println(it.buttonColumn.toString()+" "+it.buttonRow.toString()+(it.visibility==View.VISIBLE))
}


    })

    viewModel.heartLiveData.observe(viewLifecycleOwner, Observer {
        Log.d(TAG, "observeLiveData: "+it)
        binding.heartList=it
    })

    viewModel.pointLiveData.observe(viewLifecycleOwner, Observer {
        binding.point=it

        if(level<(it/40)+1.toInt()){
            level=(it/40)+1.toInt()
            cashLiveData.value = cashLiveData.value!!.plus(8)
            viewModel.speed+=0.25
            Handler().also{
                it.post(object :Runnable {
                    var i=0
                    override fun run() {
                        if (i==0){
                            binding.levelIndicatorVisibility=true
                            it.postDelayed(this,600)
                            i++
                        }else{
                            binding.levelIndicatorVisibility=false
                            it.removeCallbacks(this)
                        }
                    }

                })
            }



        }
    })

    viewModel.isLoseliveData.observe(viewLifecycleOwner, Observer {
        binding.finishPageVisibility=it
        if(!it) {

            level = 1

        }
    })
    cashLiveData.observe(viewLifecycleOwner, Observer {
        MySharedPreferences().saveCash(it)
    })
    viewModel.slowMotionButtonLiveData.observe(viewLifecycleOwner, Observer {
        var array=buttonVisibilityList.clone() as ArrayList<Boolean?>
        array[0]=it
        buttonVisibilityList=array
        binding.buttonVisibilityList=array.toTypedArray()
    })

}

    override fun click(view: View, list: String, column: Int, row: Int) {
        Log.e(TAG, "click: "+column.toString()+"-"+row.toString() )
        viewModel.click(column, row)
        if (songPosition>=song.size){
            songPosition=0
        }
      viewModel.playMusic(song,songPosition, noteMap)
        songPosition+=1

    }

    override fun buttonClick(view: View) {
        if(view.id==R.id.gamePauseButton&&!viewModel.isLoseliveData.value!!){
            if(!viewModel.isStop) {
                viewModel.stop()
            }else{
                viewModel.start(requireContext())
            }
        }
        if(view.id==R.id.changeMusicButton&&!viewModel.isLoseliveData.value!!){
            song=Songs().createRandomSongs(100,noteMap)
            songPosition=0
        }

        if(view.id==R.id.FinishPageRepeat){
            viewModel.start(requireContext())



        }
        if(view.id==R.id.FinishPageExit){
            //Navigation.findNavController(binding.root).navigate(R.id.homeFragment)

            requireActivity().finish()
            //var intent= Intent(requireContext(),HomeActivity::class.java)
            //startActivity(intent)


        }
        if (view.id==R.id.slowMotionButton){
            if(viewModel.slowMotionButtonLiveData.value==true) {
                viewModel.slowMotion(requireActivity())
            }

        }

    }


}