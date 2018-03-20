package mx.iteso.focusfocus.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import mx.iteso.focusfocus.Adapters.AdapterPagerTask
import mx.iteso.focusfocus.Beans.*

import mx.iteso.focusfocus.R
import java.util.*
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
    private lateinit var toolbar : Toolbar
    private lateinit var tabLayout: TabLayout
    private var data : HashMap<Int, ArrayList<Task>> = HashMap()

    private lateinit var compatActivity : AppCompatActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fragment_tasks, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        toolbar = view.findViewById(R.id.toolbar)
        compatActivity  = activity as AppCompatActivity
        compatActivity.setSupportActionBar(toolbar)
        getData()
        pagerAdapter = AdapterPagerTask(activity.supportFragmentManager, data)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = pagerAdapter.count / 2
        return view;
    }

    fun getData(){
        //Primer array
        var tags : ArrayList<Tag> = ArrayList()
        tags.add(Tag("Programación", "#A9999A"))
        tags.add(Tag("Historia", "#0C0B0B"))
        var subTask : ArrayList<SubTask> = ArrayList()
        subTask.add(SubTask("Parte 1", true))
        subTask.add(SubTask("Documentación", false))
        var array : ArrayList<Task> = ArrayList()
        array.add(Task("Tarea 1", "#5FA52D", tags, Date(), Priority.LOW, Status.NON_START, subTask))
        array.add(Task("Tarea 2", "#CE7115", tags, Date(), Priority.LOW, Status.NON_START , subTask))
        array.add(Task("Tarea 3", "#951A1F", tags, Date(), Priority.LOW, Status.NON_START, subTask ))
        data.put(0, array)

        //Segundo array
        var tags2 : ArrayList<Tag> = ArrayList()
        tags2.add(Tag("Hola", "#3C8C83"))
        var subTask2 : ArrayList<SubTask> = ArrayList()
        subTask2.add(SubTask("Parte 1", true))
        subTask2.add(SubTask("Documentación", false))
        subTask2.add(SubTask("Historias de usuario", true))
        var array2 : ArrayList<Task> = ArrayList()
        array2.add(Task("Tarea 1", "#6C0505", tags2, Date(), Priority.LOW, Status.IN_PROGRESS, subTask2 ))
        array2.add(Task("Tarea 2", "#E81B6D", tags, Date(), Priority.LOW, Status.IN_PROGRESS, subTask))
        array2.add(Task("Tarea 3", "#C9A5B3", tags2, Date(), Priority.LOW, Status.IN_PROGRESS, subTask2 ))
        data.put(1, array2)

        //Tercer array
        var tags3 : ArrayList<Tag> = ArrayList()
        tags3.add(Tag("Caca", "#DDCF0F"))
        tags3.add(Tag("Feo", "#C9A5B3"))
        tags3.add(Tag("Popo", "#0D3556"))
        var tags4 : ArrayList<Tag> = ArrayList()
        tags4.add(Tag("Caca", "#DDCF0F"))
        tags4.add(Tag("Feo", "#C9A5B3"))
        tags4.add(Tag("Popo", "#0D3556"))
        tags4.add(Tag("Bebe", "#DDCF0F"))

        var subTask3 : ArrayList<SubTask> = ArrayList()
        subTask3.add(SubTask("Parte 1", true))
        subTask3.add(SubTask("Documentación", false))
        subTask3.add(SubTask("Algo más", false))
        subTask3.add(SubTask("asdfas", false))
        var array3 : ArrayList<Task> = ArrayList()
        array3.add(Task("Tarea 1", "#532E8A", tags, Date(), Priority.LOW, Status.DONE, subTask3 ))
        array3.add(Task("Tarea 2", "#1E012C", tags3, Date(), Priority.LOW, Status.DONE, subTask3 ))
        array3.add(Task("Tarea 3", "#2A9186", tags2, Date(), Priority.LOW, Status.DONE, subTask2 ))
        array3.add(Task("Tarea 4", "#373A3A", tags4, Date(), Priority.LOW, Status.DONE, subTask2 ))
        data.put(2, array3)
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
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}// Required empty public constructor
