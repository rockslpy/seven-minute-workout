package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
    val US_UNITS_VIEW = "US_UNITS_VIEW"

    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        val toolbar_bmi_activity = findViewById<Toolbar>(R.id.toolbar_bmi_activity)
        setSupportActionBar(toolbar_bmi_activity)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "CALCULATE BMI"
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        val btnCalculateUnits = findViewById<Button>(R.id.btnCalculateUnits)
        btnCalculateUnits.setOnClickListener {
            if(currentVisibleView.equals(METRIC_UNITS_VIEW)){
                if(validateMetricUnits()){
                    val etMetricUnitWeight = findViewById<AppCompatEditText>(R.id.etMetricUnitWeight)
                    val etMetricUnitHeight = findViewById<AppCompatEditText>(R.id.etMetricUnitHeight)
                    val heightValue : Float = etMetricUnitHeight.text.toString().toFloat() /100
                    val weightValue : Float = etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weightValue / (heightValue*heightValue)
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity, "Please enter valid values."
                            , Toast.LENGTH_SHORT)
                            .show()
                }
            }else{
                if(validateUSUnits()){
                    val etUsUnitHeightFeet = findViewById<AppCompatEditText>(R.id.etUsUnitHeightFeet)
                    val etUsUnitHeightInch = findViewById<AppCompatEditText>(R.id.etUsUnitHeightInch)
                    val etUsUnitWeight = findViewById<AppCompatEditText>(R.id.etUsUnitWeight)
                    val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch: String = etUsUnitHeightInch.text.toString()
                    val usUnitWeightValue: Float = etUsUnitWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    val bmi = 703 * (usUnitWeightValue / (heightValue*heightValue))
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity, "Please enter valid values."
                            , Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }

        makeVisibleMetricUnitsView()
        val rgUnits = findViewById<RadioGroup>(R.id.rgUnits)
        rgUnits.setOnCheckedChangeListener { radioGroup: RadioGroup , checkedId: Int ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else {
                makeVisibleUSUnitsView()
            }
        }
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        val tilMetricUnitWeight = findViewById<TextInputLayout>(R.id.tilMetricUnitWeight)
        val tilMetricUnitHeight = findViewById<TextInputLayout>(R.id.tilMetricUnitHeight)
        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE

        val etUsUnitWeight = findViewById<AppCompatEditText>(R.id.etUsUnitWeight)
        val etUsUnitHeightFeet = findViewById<AppCompatEditText>(R.id.etUsUnitHeightFeet)
        val etUsUnitHeightInch = findViewById<AppCompatEditText>(R.id.etUsUnitHeightInch)
        etUsUnitWeight.text!!.clear()
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()

        val tilUsUnitWeight = findViewById<TextInputLayout>(R.id.tilUsUnitWeight)
        val llUsUnitsHeight = findViewById<LinearLayout>(R.id.llUsUnitsHeight)
        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        val llDiplayBMIResult = findViewById<LinearLayout>(R.id.llDiplayBMIResult)
        llDiplayBMIResult.visibility = View.VISIBLE
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        val tilMetricUnitWeight = findViewById<TextInputLayout>(R.id.tilMetricUnitWeight)
        val tilMetricUnitHeight = findViewById<TextInputLayout>(R.id.tilMetricUnitHeight)
        tilMetricUnitWeight.visibility = View.VISIBLE
        tilMetricUnitHeight.visibility = View.VISIBLE

        val etMetricUnitWeight = findViewById<AppCompatEditText>(R.id.etMetricUnitWeight)
        val etMetricUnitHeight = findViewById<AppCompatEditText>(R.id.etMetricUnitHeight)
        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()

        val tilUsUnitWeight = findViewById<TextInputLayout>(R.id.tilUsUnitWeight)
        val llUsUnitsHeight = findViewById<LinearLayout>(R.id.llUsUnitsHeight)
        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE

        val llDiplayBMIResult = findViewById<LinearLayout>(R.id.llDiplayBMIResult)
        llDiplayBMIResult.visibility = View.VISIBLE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription:String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val tvBMIValue = findViewById<TextView>(R.id.tvBMIValue)
        val tvBMIType = findViewById<TextView>(R.id.tvBMIType)
        val tvBMIDescription = findViewById<TextView>(R.id.tvBMIDescription)
        val llDiplayBMIResult = findViewById<LinearLayout>(R.id.llDiplayBMIResult)
        llDiplayBMIResult.visibility = View.VISIBLE


        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription
    }

    private fun validateUSUnits(): Boolean{
        var isValid = true

        val etUsUnitHeightFeet = findViewById<AppCompatEditText>(R.id.etUsUnitHeightFeet)
        val etUsUnitHeightInch = findViewById<AppCompatEditText>(R.id.etUsUnitHeightInch)
        val etMetricUnitWeight = findViewById<AppCompatEditText>(R.id.etMetricUnitWeight)

        when {
            etUsUnitHeightFeet.text.toString().isEmpty() -> isValid = false
            etUsUnitHeightInch.text.toString().isEmpty() -> isValid = false
            etMetricUnitWeight.text.toString().isEmpty() -> isValid = false
        }

        return isValid
    }

    private fun validateMetricUnits():Boolean{
        var isValid = true
        val etMetricUnitWeight = findViewById<AppCompatEditText>(R.id.etMetricUnitWeight)
        val etMetricUnitHeight = findViewById<AppCompatEditText>(R.id.etMetricUnitHeight)

        if(etMetricUnitWeight.text.toString().isEmpty())
            isValid = false
        else if(etMetricUnitHeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }
}