package cat.moki.display

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import pl.pawelkleczkowski.customgauge.CustomGauge
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.hours

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private lateinit var fullscreenContent: ImageView
    private lateinit var progress : CustomGauge
    private lateinit var status : TextView
    private var startAt = intArrayOf(7,0)
    private var endAt = intArrayOf(22,30)
    private var pattern = "HH:mm:ss"
    private var active = true
    private val statusList = arrayOf("P310","P201","WC","吃","Rotten Forward")
    private var current = 0
    private var here = true
    private var nth30=0

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()
        fullscreenContent = findViewById(R.id.fullscreen_content)
        fullscreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        // Set up the user interaction to manually show or hide the system UI.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//
//        // Upon interacting with UI controls, delay any scheduled hide()
//        // operations to prevent the jarring behavior of controls going away
//        // while interacting with the UI.
//        findViewById<Button>(R.id.dummy_button).setOnTouchListener(delayHideTouchListener)

        progress=findViewById(R.id.p)
        status=findViewById(R.id.status)
        switch(true)

        for (i in arrayOf(updateProgress(),onTime()))i.start()

   }

    private fun updateProgress():Thread{
        return Thread{
            while (true){
                Thread.sleep(1000)
                runOnUiThread {
                    val per = percentage()
                    if (per<0){
                        if (active)switch(true)
                    }else{
                        progress.value=(per*100).roundToInt()
                        Log.i("TAG", "onCreate: "+progress)
                        if (!active)switch(false)
                    }
                }

//                val now = Calendar.getInstance().time
//                if (now.minutes%30==0&&now.minutes==nth30){
//                    onTime().start()
//                    nth30= abs(now.minutes-30)
//                }
            }
        }
    }

    private fun switch(sleep:Boolean){
        active=sleep
        Glide.with(this).load(if (sleep) "file:///android_asset/parrot.gif" else "file:///android_asset/parrot.gif").into(fullscreenContent)
    }

    private fun percentage():Double{
        val start = Calendar.getInstance().time
        start.hours=startAt[0]
        start.minutes=startAt[1]

        val end = Calendar.getInstance().time
        end.hours=endAt[0]
        end.minutes=endAt[1]

        val now = Calendar.getInstance().time

        val result: Double = ((now.time-start.time)*1.0)/((end.time-start.time)*1.0)
        return if (result>1||result<0) (-1.0) else result

    }


    override fun onBackPressed() {
        if (here){
            status.setText(statusList[current])
            current=(current+1)%statusList.size
            here=false
        }else{
            here=true
            status.setText("")
        }


    }

    override fun onAttachedToWindow() {
    }

    private fun onTime():Thread{
        return Thread{
            for (i in 1..3){
                runOnUiThread { fullscreenContent.setBackgroundColor(resources.getColor(R.color.oclock)) }
                Thread.sleep(1000)
                runOnUiThread { fullscreenContent.setBackgroundColor(resources.getColor(R.color.black)) }
                Thread.sleep(1000)
            }
        }
    }


}
