package com.halil.pianogame.util

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import com.halil.pianogame.R
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownloadFile(var fileUrl:String,var context: Context):AsyncTask<Void,String, File>() {
    override fun doInBackground(vararg p0: Void?): File {

        var count:Int=0
        var file:File
        var url=URL(fileUrl)
        var connection=url.openConnection()
        connection.connect()
        var lengthOfFile=connection.contentLength
        var input=connection.getInputStream()
        //Environment.getDownloadCacheDirectory()
        //File(Environment.getDownloadCacheDirectory(),"music.mp3")
        //var file=createFile(outputDirectory,Constants.FILENAME_FORMAT,Constants.PHOTO_EXTENSIONS)

        file= createFile(getOutputDirectory(context),"yyyy-MM-dd-HH-mm-ss-SSS",".mp3")


        var fileOutputStream=FileOutputStream(file)
        var total=0
        var data=ByteArray(1024)
        while (count!=-1){//EOF
            total+=count
            publishProgress(""+((total*100)/lengthOfFile).toInt())
            fileOutputStream.write(data,0,count)
            count=input.read(data)

        }
        fileOutputStream.flush()
        fileOutputStream.close()
        input.close()
        return file
    }
    private fun getOutputDirectory(context: Context):File{


        var mediaDir=context.externalMediaDirs.firstOrNull()
            ?.let {
                File(it,context.applicationContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
        return if(mediaDir!=null && mediaDir.exists()) mediaDir else context.applicationContext.filesDir

    }
    private fun createFile(baseFolder:File,format:String,extension:String)=
        // File(baseFolder,SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis())+extension)
        File(baseFolder,"backGroundMusic"+extension)

}