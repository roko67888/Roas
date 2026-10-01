package com.pokerassistant.app

import kotlin.random.Random

data class Card(val r:Int,val s:Int)

object PokerEngine {
    private val ranks = "23456789TJQKA"
    private val suits = "cdhs"
    fun parse(text:String): List<Card> = text.trim().split(Regex("\\s+"), ",").filter { it.length>=2 }.mapNotNull {
        val r=ranks.indexOf(it[0].uppercaseChar())+2; val s=suits.indexOf(it[1].lowercaseChar())
        if(r in 2..14 && s>=0) Card(r,s) else null
    }
    private fun score7(cs:List<Card>):Long {
        var best=0L
        for(a in 0 until cs.size-4) for(b in a+1 until cs.size-3) for(c in b+1 until cs.size-2) for(d in c+1 until cs.size-1) for(e in d+1 until cs.size){
            val x=listOf(cs[a],cs[b],cs[c],cs[d],cs[e]); val cnt=x.groupingBy{it.r}.eachCount(); val flush=x.map{it.s}.distinct().size==1
            val uniq=x.map{it.r}.distinct().sorted(); val straight=if(uniq.size==5 && (uniq.last()-uniq.first()==4 || uniq==listOf(2,3,4,5,14))) if(uniq==listOf(2,3,4,5,14))5 else uniq.last() else 0
            val groups=cnt.entries.sortedWith(compareByDescending<Map.Entry<Int,Int>>{it.value}.thenByDescending{it.key})
            val cat=when { flush&&straight>0->8; groups[0].value==4->7; groups[0].value==3&&groups.size>1&&groups[1].value==2->6; flush->5; straight>0->4; groups[0].value==3->3; groups[0].value==2&&groups[1].value==2->2; groups[0].value==2->1; else->0 }
            val vals=when(cat){8->listOf(straight);7->listOf(groups[0].key,groups[1].key);6->listOf(groups[0].key,groups[1].key);5->x.map{it.r}.sortedDescending();4->listOf(straight);3->listOf(groups[0].key)+groups.drop(1).map{it.key}.sortedDescending();2->listOf(groups[0].key,groups[1].key,groups[2].key).sortedDescending();1->listOf(groups[0].key)+groups.drop(1).map{it.key}.sortedDescending();else->x.map{it.r}.sortedDescending()}
            var sc=cat.toLong(); for(v in vals) sc=sc*15+v
            if(sc>best)best=sc
        }
        return best
    }
    fun equity(hero:List<Card>, board:List<Card>, trials:Int=3000):Int {
        if(hero.size!=2 || board.size>5) return -1
        val deck=(2..14).flatMap{r->(0..3).map { s -> Card(r, s) }}.toMutableList()
        deck.removeAll(hero+board)
        var wins=0; var ties=0; val rng=Random.Default
        repeat(trials){
            deck.shuffle(rng); val opp=listOf(deck[0],deck[1]); val runBoard=board.toMutableList(); var i=2; while(runBoard.size<5){runBoard.add(deck[i++])}
            val h=score7(hero+runBoard); val o=score7(opp+runBoard); if(h>o)wins++ else if(h==o)ties++
        }
        return ((wins+ties/2.0)/trials*100).toInt()
    }
}
