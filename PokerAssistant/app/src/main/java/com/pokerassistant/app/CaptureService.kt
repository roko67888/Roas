package com.pokerassistant.app

import android.app.*
import android.content.*
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class CaptureService:Service(){
    private var hud:TextView?=null; private var wm:WindowManager?=null
    private var projection:MediaProjection?=null; private var reader:ImageReader?=null; private var last=0L
    override fun onCreate(){super.onCreate();createChannel();startForeground(10,notification())}
    override fun onStartCommand(i:Intent?,flags:Int,id:Int):Int{
        val rc=i?.getIntExtra("resultCode",0)?:return START_NOT_STICKY; val data=i.getParcelableExtra<Intent>("data")?:return START_NOT_STICKY
        val mgr=getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager; projection=mgr.getMediaProjection(rc,data)
        showHud()
        val dm=resources.displayMetrics; reader=ImageReader.newInstance(dm.widthPixels,dm.heightPixels,android.graphics.PixelFormat.RGBA_8888,2)
        projection?.createVirtualDisplay("PokerAssistantCapture",dm.widthPixels,dm.heightPixels,dm.densityDpi,DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,reader!!.surface,null,null)
        reader!!.setOnImageAvailableListener({r->
            val now=System.currentTimeMillis(); if(now-last<1200)return@setOnImageAvailableListener; last=now
            val img=try{r.acquireLatestImage()}catch(_:Exception){null} ?: return@setOnImageAvailableListener
            val plane=img.planes[0]; val w=img.width; val h=img.height; val bmp=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
            bmp.copyPixelsFromBuffer(plane.buffer); img.close(); recognize(bmp)
        },Handler(Looper.getMainLooper()))
        return START_STICKY
    }
    private fun recognize(bmp:Bitmap){TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(InputImage.fromBitmap(bmp,0)).addOnSuccessListener{r->
        val compact=r.text.replace("\\n"," ").take(600); val n=NotificationManagerCompatBridge(this); n.update("OCR: $compact"); hud?.post{hud?.text="Poker HUD\n$compact"}
    }}
    override fun onDestroy(){reader?.close();projection?.stop();try{hud?.let{wm?.removeView(it)}}catch(_:Exception){};super.onDestroy()}
    private fun showHud(){if(!Settings.canDrawOverlays(this))return; wm=getSystemService(WINDOW_SERVICE) as WindowManager; hud=TextView(this).apply{setTextColor(android.graphics.Color.WHITE);setBackgroundColor(0xCC111111.toInt());setPadding(18,12,18,12);textSize=13f;text="Poker HUD\nČitam ekran…"}; val lp=WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,if(Build.VERSION.SDK_INT>=26)WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,android.graphics.PixelFormat.TRANSLUCENT);lp.gravity=Gravity.TOP or Gravity.END;lp.x=16;lp.y=80;wm?.addView(hud,lp)}
    override fun onBind(i:Intent?)=null
    private fun createChannel(){if(Build.VERSION.SDK_INT>=26){getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel("poker","Poker Assistant",NotificationManager.IMPORTANCE_LOW))}}
    private fun notification():Notification=Notification.Builder(this,"poker").setContentTitle("Poker Assistant").setContentText("Čitanje ekrana aktivno").setSmallIcon(android.R.drawable.ic_menu_view).build()
}
private class NotificationManagerCompatBridge(private val c:Context){fun update(t:String){(c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(10,Notification.Builder(c,"poker").setContentTitle("Poker Assistant").setContentText(t).setSmallIcon(android.R.drawable.ic_menu_view).build())}}
