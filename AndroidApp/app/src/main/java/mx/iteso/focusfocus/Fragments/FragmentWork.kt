package mx.iteso.focusfocus.Fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.fragment_fragment_work.*

import mx.iteso.focusfocus.R
import mx.iteso.focusfocus.SettingsActivity
import mx.iteso.focusfocus.TimerExpiredReciever
import mx.iteso.focusfocus.util.NotificationUtil
import mx.iteso.focusfocus.util.PrefUtil
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentWork.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentWork.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWork : Fragment() {


    enum class TimerState {
        Stopped, Paused, Running
    }

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var toolbar_work: Toolbar
    private lateinit var compatActivity: AppCompatActivity
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L
    private lateinit var fab_play: FloatingActionButton
    private lateinit var fab_pause: FloatingActionButton
    private lateinit var fab_stop: FloatingActionButton
    private lateinit var toolbar: Toolbar


    private lateinit var cycle1 : MenuItem
    private lateinit var cycle2 : MenuItem
    private lateinit var cycle3 : MenuItem
    private lateinit var cycle4 : MenuItem

    private var cycles : Int = 1

    private var timerType = 0

    //El tiempo que va a durar se asigna en el return de Util->PrefUtil->getTimerLength()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragment_work, container, false)

        toolbar_work = view.findViewById(R.id.toolbar_work)
        compatActivity = activity as AppCompatActivity
        compatActivity.setSupportActionBar(toolbar_work)
        compatActivity = activity as AppCompatActivity
        compatActivity.supportActionBar?.setIcon(R.drawable.ic_timer)
        compatActivity.supportActionBar?.title = "      Timer"
        fab_play = view.findViewById(R.id.fragment_work_fab_play)
        fab_pause = view.findViewById(R.id.fragment_work_fab_pause)
        fab_stop = view.findViewById(R.id.fragment_work_fab_stop)
      
        fab_play.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel()
            onStopTimer()
        }
        return view
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(this.context)
        NotificationUtil.hideTimerNotification(context)
    }

    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running) {
            timer.cancel()
            val stopTime = setAlarm(this.context, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this.context, stopTime)
        } else if (timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(context)
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this.context)
        PrefUtil.setSecondsRemaining(secondsRemaining, this.context)
        PrefUtil.setTimerState(timerState, this.context)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this.context)

        setNewTimerLength()

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        if (timerState == TimerState.Running ||
                timerState == TimerState.Paused)
            secondsRemaining = PrefUtil.getSecondsRemaining(this.context)
        else
            secondsRemaining = timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this.context)
        if(alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if(secondsRemaining<=0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer ()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished () {
        updateCycles()
        timerState = TimerState.Stopped
        if ( timerType == 0 ) {
            setNewRestTimerLength()
            timerType = 1
        } else {
            setNewTimerLength()
            timerType = 0
        }
        content_timer_progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this.context)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
        updateCycles()
    }

    private fun onStopTimer () { //restart de rest time
        updateCycles()
        timerState = TimerState.Stopped
        if ( timerType == 0 ) {
            setNewRestTimerLength()
            timerType = 1
        } else {
            setNewTimerLength()
            timerType = 0
        }
        content_timer_progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this.context)
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this.context)
        timerLengthSeconds = (lengthInMinutes * 60L)
        content_timer_progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setNewRestTimerLength() {
        val lengthInMinutes = PrefUtil.getRestTimerLength(this.context)
        timerLengthSeconds = (lengthInMinutes * 60L)
        content_timer_progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this.context)
        content_timer_progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        content_timer_text_view_countdown.text = "$minutesUntilFinished:${
            if (secondsStr.length == 2) secondsStr
            else "0" + secondsStr
        }"
        content_timer_progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons () {
        when (timerState) {
            TimerState.Running -> {
                fab_play.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false

            }
            TimerState.Paused -> {
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }

        override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        fun newInstance(): FragmentWork {
            val fragment = FragmentWork()
            return fragment
        }

        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining:Long):Long{
            val endTime = (nowSeconds + secondsRemaining) * 1000;
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReciever::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return endTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReciever::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds:Long
            get() = Calendar.getInstance().timeInMillis/1000

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this.context, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        compatActivity.menuInflater.inflate(R.menu.menu_timer_cycle, menu)
        cycle1 = menu!!.getItem(0)
        cycle2 =  menu!!.getItem(1)
        cycle3 = menu!!.getItem(2)
        cycle4 = menu!!.getItem(3)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun updateCycles(){
        if (timerType == 0) {
            when (cycles) {
                1 -> cycle1.setVisible(true)
                2 -> cycle2.setVisible(true)
                3 -> cycle3.setVisible(true)
                4 -> cycle4.setVisible(true)
                else -> {
                    cycles = 0
                    cycle1.setVisible(false)
                    cycle2.setVisible(false)
                    cycle3.setVisible(false)
                    cycle4.setVisible(false)
                }
            }
            cycles++
        }
        else if(cycles == 5){
            cycle1.setVisible(false)
            cycle2.setVisible(false)
            cycle3.setVisible(false)
            cycle4.setVisible(false)
            cycles = 1
        }
    }
} // Required empty public constructor
