package mx.iteso.focusfocus.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.IntentSender
import android.os.CountDownTimer
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.fragment_fragment_work.*

import mx.iteso.focusfocus.R
import mx.iteso.focusfocus.util.PrefUtil

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentWork.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentWork.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWork : Fragment(){

    private var mListener: OnFragmentInteractionListener? = null

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLenghtSecond = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fab_start.setOnClickListener{ v ->
            println("Hello Clicker")
        }

    }

    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onPause() {
        super.onPause()
        if(timerState == TimerState.Running){
            timer.cancel()
        }else if (timerState == TimerState.Paused){

        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLenghtSecond, activity)
        PrefUtil.setSecondsRemaining(secondsRemaining, activity)
        PrefUtil.setTimerState(timerState, activity)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(activity)
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(activity)
        else
            timerLenghtSecond


        if(timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()

    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        setNewTimerLength()
        progress_countDown.progress = 0

        PrefUtil.setSecondsRemaining(timerLenghtSecond, activity)
        secondsRemaining = timerLenghtSecond
        updateButtons()
        updateCountdownUI()

    }

    private fun startTimer(){
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(milliUntilFinished: Long) {
                secondsRemaining = milliUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthMinutes = PrefUtil.getTimerLength(activity)
        timerLenghtSecond = (lengthMinutes*60L)
        progress_countDown.max = timerLenghtSecond.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLenghtSecond = PrefUtil.getPreviousTimerLengthSeconds(activity)
        progress_countDown.max = timerLenghtSecond.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textView_countDown.text = "$minutesUntilFinished:${
        if(secondsStr.length == 2) secondsStr
        else "0" + secondsStr}"
        progress_countDown.progress = (timerLenghtSecond-secondsRemaining).toInt()

    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running ->{
                fab_start.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
            }
            TimerState.Paused->{
                fab_start.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_fragment_work, container, false)
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
    }
}// Required empty public constructor
