package com.pokerassistant.app

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*

class MainActivity:Activity(){
    private lateinit var hero:EditText; private lateinit var board:EditText; private lateinit var range:EditText
    private lateinit var equity:TextView; private lateinit var rec:TextView; private lateinit var ocr:TextView
    private val store by lazy{StatsStore(this)}
    override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(R.layout.activity_main)
        hero=findViewById(R.id.heroCards);board=findViewById(R.id.boardCards);range=findViewById(R.id.villainRange);equity=findViewById(R.id.equityText);rec=findViewById(R.id.recommendation);ocr=findViewById(R.id.ocrText)
        val spinner=findViewById<Spinner>(R.id.tableMode);spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listOf("6-max","9-max"))
        findViewById<Button>(R.id.calcButton).setOnClickListener{calculate()}
        findViewById<Button>(R.id.overlayButton).setOnClickListener{if(!Settings.canDrawOverlays(this))startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))}
        findViewById<Button>(R.id.captureButton).setOnClickListener{startCapture()}
    }
    private fun calculate(){
        val h=PokerEngine.parse(hero.text.toString()); val b=PokerEngine.parse(board.text.toString())
        if(h.size!=2||b.size>5){equity.text="Equity: —";rec.text="Preporuka: provjeri karte";return}
        val e=PokerEngine.equity(h,b);equity.text="Equity: $e%"
        rec.text="Preporuka: "+when{e>=70->"Raise / Bet";e>=52->"Call / Bet";e>=35->"Check / Call uz dobar pot odds";else->"Fold"}
    }
    private fun startCapture(){
        val mgr=getSystemService(MEDIA_PROJECTION_SERVICE) as android.media.projection.MediaProjectionManager
        startActivityForResult(mgr.createScreenCaptureIntent(),42)
    }
    override fun onActivityResult(req:Int,res:Int,data:Intent?){super.onActivityResult(req,res,data);if(req==42&&res==RESULT_OK&&data!=null){
        val i=Intent(this,CaptureService::class.java).putExtra("resultCode",res).putExtra("data",data)
        startForegroundService(i); Toast.makeText(this,"Čitanje ekrana pokrenuto",Toast.LENGTH_SHORT).show()
    }}
    fun showOcr(text:String){runOnUiThread{ocr.text="OCR: $text"}}
}
