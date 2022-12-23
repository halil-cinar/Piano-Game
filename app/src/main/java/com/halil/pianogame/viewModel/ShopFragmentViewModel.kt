package com.halil.pianogame.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.Build

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter
import com.google.gson.Gson
import com.halil.pianogame.modul.ImageModel
import com.halil.pianogame.modul.ProductModelRetrofit
import com.halil.pianogame.modul.ProductModul
import com.halil.pianogame.retrofit.ApiServis
import com.halil.pianogame.room.RoomDatabase
import com.halil.pianogame.sharedPreferences.MySharedPreferences
import com.halil.pianogame.util.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ShopFragmentViewModel:ViewModel() {
    lateinit var context:Context
    var productLiveData=MutableLiveData<ArrayList<ProductModul>>()
    var imageModelList=ArrayList<ImageModel>()
    fun start(context: Context){
        this.context=context
        viewModelScope.launch(Dispatchers.Default){
            var list=getProductList()
            withContext(Dispatchers.Main){
                productLiveData.value=list
                this@launch.cancel()
            }

        }
        try {


            getProductListInRetrofit()
        }catch (e:Exception){
            e.printStackTrace()
        }
        //getImageList()
        //productLiveData.value= getProductList(context)
    }

    var target=object :com.squareup.picasso.Target{
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            backgroundImageLiveData.value = bitmap
                viewModelScope.launch(Dispatchers.Unconfined) {
                    var stream = ByteArrayOutputStream()
                    resizeImage(bitmap!!, bitmap.height/(2)).compress(Bitmap.CompressFormat.PNG, 50, stream)

                    MySharedPreferences(context).saveUsedChanges(
                        product!!,
                        MySharedPreferences.BACKGROUND_KEY,
                        ImageModel(
                            product!!.uuid.toString(),
                            Gson().toJson(stream.toByteArray())
                        ),
                        MySharedPreferences.BACKGROUND_IMAGEKEY
                    )
                    stream.close()

                }

        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            e?.printStackTrace()
            
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            Log.d(TAG, "onPrepareLoad: ")
        }

    }
private var product:ProductModul?=null
    fun applyChanges(){
        productLiveData.value?.forEach {
            if(it.isUse){
                when(it.typeId){
                    1-> {
                        /*var imageView=ImageView(context)
                        Glide.with(context).load("").into(imageView)
                        var bitmap=imageView.drawable.toBitmap()
                        */Log.d(TAG, "buy: 1 background")
                        var bitmap:Bitmap
                        product=it
                        var drawable=CircularProgressDrawable(context).apply {
                            strokeWidth=8f
                            centerRadius=40f
                            start()
                        }
                       viewModelScope.launch(Dispatchers.Main){
                           try {


                               Picasso.get().load(it.data).into(target)
                           }catch (e:Exception){
                               e.printStackTrace()
                           }
                            this.cancel()
                       }





                    }

                    2->{

                        var music:MediaPlayer
                        CoroutineScope(Dispatchers.IO).launch {
                            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                                Log.e("application",true.toString())

                            }
                            var file= DownloadFile(it.data?:"",context).execute().get()
                            Log.e("application",file.path)
                            MySharedPreferences(context).saveBackGroundMusicUri(file.toUri())
                            withContext(Dispatchers.Main) {
                                music = MediaPlayer.create(context, file.toUri())
                               // music.start()
                                Log.e("application", music.toString())

                                backgroundMediaPlayerLiveData.value=music

                                this@launch.cancel()
                            }
                        }

                    }

                    else->{
                        Log.d(TAG, "buy:else")
                    }


                }
            }
        }
    }



    fun buy(productModul: ProductModul):Boolean{
        cashLiveData.value = cashLiveData.value?.minus(productModul.price)
        if(cashLiveData.value!! >=0) {
            var index = productLiveData.value?.indexOf(productModul)
            if (index != null) {

                var list = productLiveData.value
                list?.get(index)?.isTaken = true


                productLiveData.value = list ?: arrayListOf()


            } else {
                Log.e(TAG, "buy: index=null")
            }
            return true
        }
        else{
            cashLiveData.value=cashLiveData.value?.plus(productModul.price)
            return false
        }
    }

    fun use(productModul: ProductModul){
        var index=productLiveData.value?.indexOf(productModul)
        if (index!=null){

            var list=productLiveData.value
            list?.get(index)?.isUse=true



            list?.forEach {
                if(it.isUse==true&& it.typeId==productModul.typeId){
                    if (it.uuid!=productModul.uuid) {
                        it.isUse = false
                    }
                }

            }

            productLiveData.value=list ?: arrayListOf()
            applyChanges()



        }else{
            Log.e(TAG, "use: index=null" )
        }
    }



    fun saveProductList(productModulList: ArrayList<ProductModul>){
        viewModelScope.launch(Dispatchers.IO) {
            var dao=RoomDatabase(context).getDao()
            dao.deleteAllProductModel()
            dao.saveProductModel(*productModulList.toTypedArray())


        }
    }

    fun saveImageList(imageModelList:ArrayList<ImageModel>){
        viewModelScope.launch(Dispatchers.IO){
            var dao=RoomDatabase(context).getImageDao()
            //dao.deleteAllImageModel()
           var a= dao.saveImageModel(*imageModelList.toTypedArray())
            Log.d(TAG, "saveImageList: "+imageModelList.toTypedArray().size)
        }
    }

    suspend fun getProductList(): ArrayList<ProductModul> {


            var dao=RoomDatabase(context).getDao()
           var list=dao.getProductModelList()

                if (!list.isNullOrEmpty()) {
                    return  arrayListOf(*list.toTypedArray())

                }else{
                        return com.halil.pianogame.util.getProductList(context)
                }
                //getImageList()






    }



    fun getProductListInRetrofit(){
        val job:Job
        var list:ArrayList<ProductModelRetrofit>
        job=viewModelScope.launch(Dispatchers.IO) {
            var response:retrofit2.Response<List<ProductModelRetrofit>>?=null
            try {
                 response = ApiServis().api.getProductData()
            }catch (e:Exception){e.printStackTrace()}
            withContext(Dispatchers.Main){
                if(response?.isSuccessful == true){

                    response.body()?.let {
                        list= ArrayList(it)
                        var i=it.size-1
                        applyChangesInRetrofitProductModel(list)

                        while (i>=0){
                           println(list[i])
                            i-=1
                                       }

                    }
                    
                    this@launch.cancel()
                }else{
                    this@launch.cancel()
                }
            }
        }
    }

    fun applyChangesInRetrofitProductModel(productModelRetrofitList: ArrayList<ProductModelRetrofit>){
        var list= arrayListOf<ProductModul>()
        var defaultList= arrayListOf<ProductModul>()
        viewModelScope.launch(Dispatchers.Default){
            defaultList= getProductList()
            defaultList.forEach {productModel->
                productModelRetrofitList.forEach {productModelRetrofit ->
                    if (productModel.uuid==productModelRetrofit.id){
                        productModelRetrofit.isTaken=productModel.isTaken
                        productModelRetrofit.isUse=productModel.isUse
                    }
                }
            }
            withContext(Dispatchers.IO) {
                productModelRetrofitList.forEach {




                    var bitmap = Picasso.get().load(it.image).get()
                    bitmap= resizeImage(bitmap,300)
                    var stream = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)

                    list.add(
                        ProductModul(
                            it.title,
                            it.price,
                            stream.toByteArray(),
                            it.isTaken,
                            it.isUse,
                            it.data,
                            it.typeId,
                            it.id
                        )
                    )








                }
                withContext(Dispatchers.Main) {
                    productLiveData.value = list
                    applyChanges()
                    this@launch.cancel()
                }
            }
        }

    }

    fun getImageList(){
        viewModelScope.launch(Dispatchers.Default) {
            var dao = RoomDatabase(context).getImageDao()
            var list = dao.getAllImageModel()
            withContext(Dispatchers.Main) {
                if (!list.isNullOrEmpty()) {
                imageModelList = arrayListOf(*list.toTypedArray())
                    Log.d(TAG, "getImageList: ${imageModelList.size}")
            }else{
                imageModelList= com.halil.pianogame.util.getImageList(context)
            }
                applyChanges()
                this@launch.cancel()

            }
        }
    }




}