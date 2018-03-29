package mx.iteso.focusfocus.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import mx.iteso.focusfocus.ActivityAddTask
import mx.iteso.focusfocus.Adapters.AdapterPagerTask
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.R
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentTasks.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentTasks.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTasks : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: AdapterPagerTask
    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private var data: HashMap<Int, ArrayList<Task>> = HashMap()
    private lateinit var compatActivity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fragment_tasks, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        toolbar = view.findViewById(R.id.toolbar)
        compatActivity = activity as AppCompatActivity
        compatActivity.setSupportActionBar(toolbar)
        pagerAdapter = AdapterPagerTask(activity.supportFragmentManager, activity)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = pagerAdapter.count / 2
        return view
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance(): FragmentTasks {
            val fragment = FragmentTasks()
            return fragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        compatActivity.menuInflater.inflate(R.menu.menu_task, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.action_add -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            var intent = Intent(activity.baseContext, ActivityAddTask::class.java)
            activity.startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
} // Required empty public constructor
