package mx.iteso.focusfocus.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.Fragments.FragmentTypeTask


/**
 * Created by Maritza on 19/03/2018.
 */
class AdapterPagerTask(fragmentManager: FragmentManager, private val task: HashMap<Int, ArrayList<Task>>):
        FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return FragmentTypeTask.newInstance(task.getValue(position))
    }

    override fun getCount(): Int {
        return task.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position){
            0 ->  return "Non Start"
            1 ->  return "In Progress"
            2 ->  return "Done"
            else -> return "Done"
        }
    }
}