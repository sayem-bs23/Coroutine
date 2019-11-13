package com.prime.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {
    private lateinit var job: Job
    private val PROGRESS_MAX = 100
    private val PROGRESS_START= 0
    private val JOB_TIME = 4000 //ms



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job_button.setOnClickListener{
            if(!::job.isInitialized){
                initJob()
            }
            job_progress_bar.startJobOrCancel(job)
        }


    }


    //kotlin extension function
    fun ProgressBar.startJobOrCancel(job:Job){
        if(this.progress > 0){

            println("${job} is already active. cancelling..")
            resetJob()
        }
        else{
            job_button.setText("cancel job #1")

            //fire the coroutine
            // **only IO contex, if I cancel the coroutine, it will call all other coroutine
            CoroutineScope(IO + job).launch {
                println("coroutine ${this} is activated with job ${job}")

                for(i in PROGRESS_START..PROGRESS_MAX){ //i = 0, 1, 2.. .100
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress  = i
                }
//                job_complete_text.setText("job is complete") //io thread
                updateJobCompleteTextView("job is complete")
            }
//            job.cancel()
        }
    }


    private fun updateJobCompleteTextView(text: String){
        GlobalScope.launch(Main){
            job_complete_text.setText(text)
        }
    }

    fun initJob(){

        job_button.setText("start job #1")
        updateJobCompleteTextView("")
        job = Job()

        //interface invokeOnCompletion
        job.invokeOnCompletion{
            it?.message.let{
                var msg = it
                if(msg.isNullOrBlank()){
                    msg = "unknown cancellation error"
                }
                println("${job} was cancelled. Reason: ${msg}")
                showToast(msg)
            }
        }

        //reset
        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_START


    }

    fun showToast(text: String){
        GlobalScope.launch(Main) {
            Toast.makeText(this@MainActivity, "net_trig", Toast.LENGTH_SHORT).show()

        }
    }

    private fun resetJob(){
        if(job.isActive|| job.isCompleted){
            job.cancel(CancellationException("the job is cancelled"))
        }
        initJob() //brand new job
    }
}
