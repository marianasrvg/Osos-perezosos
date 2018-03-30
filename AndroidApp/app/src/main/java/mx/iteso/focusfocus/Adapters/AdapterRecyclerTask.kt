package mx.iteso.focusfocus.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_task.view.done
import kotlinx.android.synthetic.main.item_task.view.tag_one
import kotlinx.android.synthetic.main.item_task.view.tag_two
import kotlinx.android.synthetic.main.item_task.view.tag_three
import kotlinx.android.synthetic.main.item_task.view.tag_four
import kotlinx.android.synthetic.main.item_task.view.subtasks
import kotlinx.android.synthetic.main.item_task.view.circle
import kotlinx.android.synthetic.main.item_task.view.title
import kotlinx.android.synthetic.main.item_task.view.date
import mx.iteso.focusfocus.Beans.Status
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.Database.ControlTask
import mx.iteso.focusfocus.Database.DataBaseHandler
import mx.iteso.focusfocus.R
import mx.iteso.focusfocus.inflate
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by Maritza on 19/03/2018.
 */
class AdapterRecyclerTask(
    private val task: ArrayList<Task>,
    var context: Context
) : RecyclerView.Adapter<AdapterRecyclerTask.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRecyclerTask.TaskHolder {
        val inflatedView = parent.inflate(R.layout.item_task, false)
        return TaskHolder(inflatedView, context)
    }

    override fun getItemCount(): Int {
        return task.size
    }

    override fun onBindViewHolder(holder: AdapterRecyclerTask.TaskHolder, position: Int) {
        val itemTask = task[position]
        holder.bindHolder(itemTask)
        holder.itemView.done.setOnCheckedChangeListener { buttonView, isChecked ->
            task.removeAt(position)
            val controlTask = ControlTask()
            val dh = DataBaseHandler.getInstance(this.context)
            if (isChecked) {
                controlTask.updateState(Status.DONE, itemTask.id, dh)
            } else controlTask.updateState(Status.NON_START, itemTask.id, dh)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, task.size)
        }
    }

    class TaskHolder(v: View, val context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {

        override fun onClick(p0: View?) {
            Log.d("RecyclerView", "CLICK!")
        }

        private var view: View = v
        private var task: Task? = null

        init {
            v.setOnClickListener(this)
        }

        companion object {
            //5
            private val TASK_KEY = "TASK"
        }

        fun bindHolder(task: Task) {
            this.task = task
            view.title.text = task.title
            when (task.tags.size) {
                4 -> {
                    view.tag_four.text = task.tags[3].name
                    view.tag_four.visibility = View.VISIBLE
                    view.tag_four.setBackgroundColor(task.tags[3].color)
                    view.tag_three.text = task.tags[2].name
                    view.tag_three.visibility = View.VISIBLE
                    view.tag_three.setBackgroundColor(task.tags[2].color)
                    view.tag_two.text = task.tags[1].name
                    view.tag_two.visibility = View.VISIBLE
                    view.tag_two.setBackgroundColor(task.tags[1].color)
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(task.tags[0].color)
                }
                3 -> {
                    view.tag_three.text = task.tags[2].name
                    view.tag_three.visibility = View.VISIBLE
                    view.tag_three.setBackgroundColor(task.tags[2].color)
                    view.tag_two.text = task.tags[1].name
                    view.tag_two.visibility = View.VISIBLE
                    view.tag_two.setBackgroundColor(task.tags[1].color)
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(task.tags[0].color)
                }
                2 -> {
                    view.tag_two.text = task.tags[1].name
                    view.tag_two.visibility = View.VISIBLE
                    view.tag_two.setBackgroundColor(task.tags[1].color)
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(task.tags[0].color)
                }
                1 -> {
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(task.tags[0].color)
                }
            }
            if (task.status == Status.DONE) view.done.isChecked = true
            else view.done.isChecked = false
            view.subtasks.text = task.subtaskDone().toString() + "/" + task.subTask.size
            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            view.date.text = sdf.format(task.date.getTime())
            view.circle.backgroundTintList = ColorStateList.valueOf(task.color)
        }
    }
}