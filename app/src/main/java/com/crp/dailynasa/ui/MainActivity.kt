package com.crp.dailynasa.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.api.load
import com.crp.dailynasa.data.model.ResponseData
import com.crp.dailynasa.data.model.State
import com.crp.dailynasa.databinding.ActivityMainBinding
import com.crp.dailynasa.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    var dateString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setupObserver()
        setupUI()


    }

    private fun setupUI() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dateString = "$year-$month-$day"
        fetchData()
        activityMainBinding.datePickerFab.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    dateString = "$year-$monthOfYear-$dayOfMonth"
                    activityMainBinding.dateTv.text = "Featured on $dayOfMonth-$monthOfYear-$year"
                    fetchData()
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()

        }
    }

    private fun fetchData() {
        mainViewModel.fetchData(dateString)
    }

    private fun setupObserver() {
        mainViewModel.imageData.observe(this, Observer {
            when (it) {
                is State.Success -> {
                    activityMainBinding.progressBar.visibility = View.GONE
                    it.data?.let { data -> renderData(data) }
                }
                is State.Loading -> {
                    activityMainBinding.progressBar.visibility = View.VISIBLE
                }
                is State.Error -> {
                    activityMainBinding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderData(responseData: ResponseData) {
        activityMainBinding.titleTv.text = responseData.title
        activityMainBinding.descTv.text = responseData.explanation
        activityMainBinding.displayImage.load(responseData.url)
    }
}