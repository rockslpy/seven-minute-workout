package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration : Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        val toolbar_exercise_activity =
                findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_exercise_activity)
        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        tts = TextToSpeech(this,this)

        exerciseList = Constants.defaultExerciseList()

        setupRestView()

        setupExerciseStatusRecyclerView()

    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!= null){
            player!!.stop()
        }

        super.onDestroy()
    }

        private fun setRestProgressBar(){
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.progress = restProgress
            restTimer = object: CountDownTimer(restTimerDuration*1000,1000) {
                override fun onTick(millisUntilFinished: Long) {
                    restProgress++
                    progressBar.progress = 10 - restProgress
                    val tvTimer = findViewById<TextView>(R.id.tvTimer)
                    tvTimer.text = (10 - restProgress).toString()
                }

                override fun onFinish() {
                    currentExercisePosition++

                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setupExerciseView()
                }
            }.start()
        }

        private fun setExerciseProgressBar(){
            val progressBarExercise = findViewById<ProgressBar>(R.id.progressBarExercise)
            progressBarExercise.progress = exerciseProgress
            exerciseTimer = object: CountDownTimer(exerciseTimerDuration*1000,1000) {
                override fun onTick(millisUntilFinished: Long) {
                    exerciseProgress++
                    progressBarExercise.progress = exerciseTimerDuration.toInt() - exerciseProgress
                    val tvExerciseTimer = findViewById<TextView>(R.id.tvExerciseTimer)
                    tvExerciseTimer.text = (exerciseTimerDuration.toInt() - exerciseProgress).toString()
                }

                override fun onFinish() {
                    if(currentExercisePosition < exerciseList?.size!! - 1){
                        exerciseList!![currentExercisePosition].setIsSelected(false)
                        exerciseList!![currentExercisePosition].setIsCompleted(true)
                        exerciseAdapter!!.notifyDataSetChanged()
                        setupRestView()
                    }else{
                        finish()
                        val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                        startActivity(intent)
                    }
                }
            }.start()
        }

        private fun setupExerciseView(){

            val llRestView = findViewById<LinearLayout>(R.id.llRestView)
            llRestView.visibility = View.GONE
            val llExerciseView = findViewById<LinearLayout>(R.id.llExerciseView)
            llExerciseView.visibility = View.VISIBLE

            if(exerciseTimer != null){
                exerciseTimer!!.cancel()
                exerciseProgress = 0
            }

            speakOut(exerciseList!![currentExercisePosition].getName())

            setExerciseProgressBar()

            val ivImage = findViewById<ImageView>(R.id.ivImage)
            ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
            val tvExerciseName = findViewById<TextView>(R.id.tvExerciseName)
            tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        }

        private fun setupRestView(){
            try{
                val soundURI = Uri.parse("android:resource://com.example.a7minuteworkout/"
                     + R.raw.press_start)
                player = MediaPlayer.create(applicationContext, R.raw.press_start)
                player!!.isLooping = false
                player!!.start()
            }catch (e: Exception){
                e.printStackTrace()
            }

            val llRestView = findViewById<LinearLayout>(R.id.llRestView)
            llRestView.visibility = View.VISIBLE
            val llExerciseView = findViewById<LinearLayout>(R.id.llExerciseView)
            llExerciseView.visibility = View.GONE

            if(restTimer != null){
                restTimer!!.cancel()
                restProgress = 0
            }

            val tvUpcomingExerciseName = findViewById<TextView>(R.id.tvUpcomingExerciseName)
            tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition +1].getName()

            setRestProgressBar()
        }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specified is not supported!")
            }
        }else{
            Log.e("TTS","Initialization Failed!")
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExerciseStatusRecyclerView(){
        val rvExerciseStatus = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvExerciseStatus)
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.findViewById<Button>(R.id.tvYes).setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.findViewById<Button>(R.id.tvNo).setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}