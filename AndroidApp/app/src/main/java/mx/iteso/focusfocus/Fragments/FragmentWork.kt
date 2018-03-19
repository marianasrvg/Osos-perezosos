package mx.iteso.focusfocus.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import mx.iteso.focusfocus.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentWork.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentWork.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWork : Fragment() {

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
