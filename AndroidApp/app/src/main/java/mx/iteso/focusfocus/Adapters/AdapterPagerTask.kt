package mx.iteso.focusfocus.Adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import mx.iteso.focusfocus.Beans.Status
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.Database.ControlSubTask
import mx.iteso.focusfocus.Database.ControlTag
import mx.iteso.focusfocus.Database.ControlTask
import mx.iteso.focusfocus.Database.DataBaseHandler
import mx.iteso.focusfocus.Fragments.FragmentTypeTask
/**
 * Created by Maritza on 19/03/2018.
 */
class AdapterPagerTask(fragmentManager: FragmentManager, private val context: Context):
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FragmentTypeTask.newInstance(getData(Status.NON_START))
            1 -> return FragmentTypeTask.newInstance(getData(Status.IN_PROGRESS))
            2 -> return FragmentTypeTask.newInstance(getData(Status.DONE))
            else -> return FragmentTypeTask.newInstance(getData(Status.NON_START))
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "Non Start"
            1 -> return "In Progress"
            2 -> return "Done"
            else -> return "Done"
        }
    }

    fun getData(status: Status): ArrayList<Task> {
        val controlTask = ControlTask()
        val controlSubTask = ControlSubTask()
        val controlTag = ControlTag()
        var dh = DataBaseHandler.getInstance(context)

        val tasks = controlTask.getTaskByStatus(status, dh)
        for (task in tasks) {
            task.tags = controlTag.getTagByIdTask(task.id, dh)
            task.subTask = controlSubTask.getSubTaskByIdTask(task.id, dh)
        }
        tasks.sort()
        return tasks
    }
}