package mx.iteso.focusfocus.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.iteso.focusfocus.Adapters.AdapterRecyclerTask
import mx.iteso.focusfocus.Beans.Task

import mx.iteso.focusfocus.R

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTypeTask.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTypeTask : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterRecyclerTask
    private var data: ArrayList<Task> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            data = arguments.getParcelableArrayList(ARG_PARAM1)
        }
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_type_task, container, false)
        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = linearLayoutManager
        adapter = AdapterRecyclerTask(data, activity)
        recyclerView.adapter = adapter
        return view
    }

    companion object {
        private val ARG_PARAM1 = "DATA"

        fun newInstance(task: ArrayList<Task>): FragmentTypeTask {
            val fragment = FragmentTypeTask()
            val args = Bundle()
            args.putParcelableArrayList(ARG_PARAM1, task)
            fragment.arguments = args
            return fragment
        }
    }
} // Required empty public constructor
