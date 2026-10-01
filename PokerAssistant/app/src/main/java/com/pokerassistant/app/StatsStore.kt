package com.pokerassistant.app

import android.content.Context
import org.json.JSONObject

class StatsStore(ctx:Context){
    private val p=ctx.getSharedPreferences("player_stats",Context.MODE_PRIVATE)
    data class Stats(var hands:Int=0,var vpip:Int=0,var pfr:Int=0,var threeBet:Int=0,var fold3:Int=0,var raises:Int=0,var folds:Int=0,var calls:Int=0)
    fun get(name:String):Stats{ val j=JSONObject(p.getString(name,"{}")); return Stats(j.optInt("hands"),j.optInt("vpip"),j.optInt("pfr"),j.optInt("threeBet"),j.optInt("fold3"),j.optInt("raises"),j.optInt("folds"),j.optInt("calls")) }
    fun record(name:String,action:String){val s=get(name);s.hands++;when(action.lowercase()){"raise"->{s.raises++;s.pfr++;s.vpip++};"3-bet","4-bet"->{s.threeBet++;s.pfr++;s.vpip++};"call"->s.calls++;"fold"->s.folds};p.edit().putString(name,JSONObject().apply{put("hands",s.hands);put("vpip",s.vpip);put("pfr",s.pfr);put("threeBet",s.threeBet);put("fold3",s.fold3);put("raises",s.raises);put("folds",s.folds);put("calls",s.calls)}.toString()).apply()}
}
