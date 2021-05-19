package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val toolbar_history_activity = findViewById<Toolbar>(R.id.toolbar_history_activity)
        setSupportActionBar(toolbar_history_activity)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "HISTORY"
        }

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()
    }

    private fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this, null)
        val allCompletedDatesList = dbHandler.getAllCompletedDateList()

        val tvHistory = findViewById<TextView>(R.id.tvHistory)
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        val tvNoDataAvailable = findViewById<TextView>(R.id.tvNoDataAvailable)

        if (allCompletedDatesList.size > 0) {

            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE

            rvHistory.layoutManager = LinearLayoutManager(this)

            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)

            rvHistory.adapter = historyAdapter
        } else {
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE
            tvNoDataAvailable.visibility = View.VISIBLE
        }
    }
}