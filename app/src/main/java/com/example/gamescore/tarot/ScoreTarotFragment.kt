package com.example.gamescore.tarot

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gamescore.R
import com.example.gamescore.ScoreFragment
import kotlinx.android.synthetic.main.fragment_score.view.*
import com.example.gamescore.Game


class ScoreTarotFragment : ScoreFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        v.LL_tarot.visibility = View.VISIBLE
        val nbPlayers = listPlayers.size
        v.P1.text = listPlayers[0].take(2)
        v.P2.text = listPlayers[1].take(2)
        v.P3.text = listPlayers[2].take(2)
        v.P4.text = listPlayers[3].take(2)
        if (nbPlayers == 5) {
            v.P5.visibility = View.VISIBLE
            v.P5.text = listPlayers[4].take(2)
        }

        val act = activity as TarotActivity
        if (listGames.size > 0) {
            Log.w("LIST", listGames[0].toString())
            if (!listGames.last().restart) {
                val lastGame = listGames.last() as GameTarot
                val editor: SharedPreferences.Editor =
                    act.context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE).edit()
                lastGame.saveGameToSharedPreferences(editor)
                if (!act.namesSaved) {
                    act.names.forEachIndexed { index, value ->
                        editor.putString("Name_$index", value)
                    }
                    editor.apply()
                    act.namesSaved = true
                }
            }
        }

        // TODO: make this feature more visible
        val onItemClick = { game: Game ->
            if (game.restart) {
                Toast.makeText(
                    act.context,
                    act.resources.getString(R.string.click_hint_not_possible),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    act.context,
                    act.resources.getString(R.string.long_click_hint),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val onItemLongClick = { position: Int ->
            if (listGames[position].restart) {
                Toast.makeText(
                    act.context,
                    act.resources.getString(R.string.click_hint_not_possible),
                    Toast.LENGTH_SHORT
                ).show()
                true // because onItemLongClick is of time (Int) -> Boolean
            } else {
                act.editTarotGame(position)
            }
        }
        val adapter = TarotListAdapter(listGames, onItemClick, onItemLongClick)
        v.RV_games.adapter = adapter

        return v
    }
}