package com.halil.pianogame.viewModel

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.halil.pianogame.R
import com.halil.pianogame.modul.ButtonClickModel
import com.halil.pianogame.modul.ButtonVisibility
import com.halil.pianogame.util.ROW_COUNT
import kotlin.math.roundToInt

class GameFragmentViewModel:ViewModel() {
    private lateinit var context: Context
     var isStop=false
        private set

    var isPause=false
        private set

val LAUNCH_SPEED=7.6
    var speed:Double=LAUNCH_SPEED
    var isLoseliveData=MutableLiveData<Boolean>(false)
    var pointLiveData=MutableLiveData<Int>(0)
    var buttonLiveData=MutableLiveData<ArrayList<ButtonVisibility>>()
    var heartLiveData=MutableLiveData<Int>(4)
    var slowMotionButtonLiveData=MutableLiveData<Boolean?>(true)
    private val MAX_PADDING=2000
    private var clickList=ArrayList<ButtonClickModel>()
    private lateinit var startRunnable:Runnable

    fun start(context: Context){
        this.context=context
        heartLiveData.value=4
        pointLiveData.value=0
        speed=LAUNCH_SPEED
        isStop=false
        isPause=false
        isLoseliveData.value=false
        var handler=Handler()
        var runnable=object :Runnable{
            override fun run() {
                handler.postDelayed(this,1500*5/(speed).toLong())
                var selectedRow:Int?
                var randomColumn:Int
                startRunnable=this



                randomColumn = random(1, 3)

                selectedRow = rowControl(randomColumn)

               // Log.d(TAG, "run: "+selectedRow.toString()+" "+randomColumn)

                selectedRow?.let {
                run(randomColumn, it)

                }
                if(isStop){
                    handler.removeCallbacks(this)
                }








            }

        }
      handler.post(runnable)


    }

    fun slowMotion(activity: FragmentActivity? =null){

        Handler().also {
            it.post(object :Runnable{
                var i=0
                val changeLimit=5
                override fun run() {

                    if (activity?.isFinishing==true){
                        i= Int.MAX_VALUE
                    }

                    if (i in 0..6) {
                        slowMotionButtonLiveData.value = null
                        if(i==0) {
                            speed -= changeLimit
                        }
                        if (!isStop && !isPause){
                            i++

                        }
                       activity?.findViewById<ProgressBar>(R.id.progress_circular_bar)?.visibility=View.VISIBLE

                        it.postDelayed(this, 1000)


                    }else if (i in 6..19){

                        slowMotionButtonLiveData.value=false
                        if(i==7) {
                            speed += changeLimit
                        }
                         if (!isStop && !isPause){
                             i+=1
                         }

                        it.postDelayed(this,1000)


                    }else{
                        activity?.findViewById<ProgressBar>(R.id.progress_circular_bar)?.visibility=View.INVISIBLE

                        slowMotionButtonLiveData.value=true
                        it.removeCallbacks(this)



                    }
                }

            })
        }
    }


    fun click(column: Int,row: Int){
        if (!isStop && !isPause) {
            var list = clickList
            list.add(ButtonClickModel(column, row, true))
            clickList = list
            pointLiveData.value = pointLiveData.value?.plus(5)

        }
    }

    fun playMusic(song:ArrayList<String>,songPosition:Int,noteMap:HashMap<String,MediaPlayer>){
        if (!isStop && !isPause) {
            Handler().also {
                it.post(object : Runnable {
                    override fun run() {
                        var split = song[songPosition].split("-")
                        split.forEach { str ->
                            noteMap.forEach {
                                if (it.key == str) {
                                    var value = it.value

                                    value.seekTo(0)
                                    value.start()
                                }
                            }
                            it.postDelayed(this, 500)

                        }

                        it.removeCallbacks(this)
                    }


                })
            }
        }
    }

    fun stop(pause:Boolean=true){
       isStop=true

        isPause=pause

    }
    private fun lose(){
        isLoseliveData.value=true
        stop(false)


    }

    private fun rowControl(column: Int):Int?{

        var list=buttonLiveData.value
        var selectedRow:Int?=null

        var isColumn=false
        var maxRow=0
        list?.let { list->
            list.forEach {
                if(it.buttonColumn==column){
                    isColumn=true
                    if (maxRow<it.buttonRow){maxRow=it.buttonRow}
                    if(!it.running){
                        selectedRow=it.buttonRow
                    }
                }
            }

        }
        if (list==null || !isColumn ){
            buttonLiveData.value= createRow(column,1, View.INVISIBLE)
            selectedRow= 1
        }
        else if (maxRow< ROW_COUNT[column]!!){

            var i=1
            while (i<= ROW_COUNT[column]!!){
                buttonLiveData.value= createRow(column,i, View.INVISIBLE)
                i+=1
            }


        }

        return selectedRow
    }

   private fun createRow(column: Int,row: Int,visibility: Int):ArrayList<ButtonVisibility>{
        var list= buttonLiveData.value?: arrayListOf()
        list.add(ButtonVisibility(column,row, visibility,0).setrunning(false))
        return list
    }






    private fun run(column: Int,row: Int){

        var handler=Handler()
        var runnable=object :Runnable{
            var padding=0;
            override fun run() {
                handler.postDelayed(this,10)
                if (!isPause) {
                    if (padding < MAX_PADDING) {

                        padding += speed.roundToInt()
                        if (!clickControl(column, row)) {
                            moveButton(column, row, padding, View.VISIBLE)
                        } else {
                            padding = 0
                            moveButton(column, row, padding, View.INVISIBLE, false)
                            handler.removeCallbacks(this)
                        }
                    } else {
                        if (heartLiveData.value!! - 1 >= 0) {
                            heartLiveData.value = heartLiveData.value!! - 1
                        } else {
                            lose()
                        }
                        padding = 0
                        moveButton(column, row, padding, View.INVISIBLE, false)
                        handler.removeCallbacks(this)
                    }

                }

            }

        }
handler.post(runnable)
    }

    private fun clickControl(column: Int,row: Int):Boolean{
        clickList.forEach {
            if (it.column==column&&it.row==row){

                clickList.remove(it)
                return true

            }
        }
        return false
    }


   private fun moveButton(column:Int,row:Int,padding:Int,visibility: Int,running:Boolean=true){
        var list=buttonLiveData.value

        if(! list.isNullOrEmpty()){
            var iscolumn=false
            var isrow=false
            list.forEach {
                if(it.buttonColumn==column){
                    iscolumn=true
                    if(it.buttonRow==row){
                        it.padding=padding
                        it.visibility=visibility
                        it.running=running
                        isrow=true

                    }
                }
            }
            if(!iscolumn or !isrow ){
                list.add(ButtonVisibility(column,row, View.VISIBLE,padding).setrunning(running))

            }


        }
       buttonLiveData.value=list?: arrayListOf(ButtonVisibility(1,1,View.VISIBLE,0, R.id.column_1_button_1).setrunning(running))


    }

   private fun random(min:Int,max:Int):Int{
        return (((System.nanoTime()/100000))%(max)).toInt()+min
    }

}