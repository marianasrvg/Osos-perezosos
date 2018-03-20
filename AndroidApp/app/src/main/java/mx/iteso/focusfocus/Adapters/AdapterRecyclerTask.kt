package mx.iteso.focusfocus.Adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_task.view.*
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.R
import mx.iteso.focusfocus.inflate

/**
 * Created by Maritza on 19/03/2018.
 */
class AdapterRecyclerTask(private val task: ArrayList<Task>) : RecyclerView.Adapter<AdapterRecyclerTask.TaskHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRecyclerTask.TaskHolder {
        val inflatedView = parent.inflate(R.layout.item_task, false)
        return TaskHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return task.size
    }

    override fun onBindViewHolder(holder: AdapterRecyclerTask.TaskHolder, position: Int) {
        val itemTask = task[position]
        holder.bindHolder(itemTask)
    }


    class TaskHolder(v : View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        override fun onClick(p0: View?) {
            Log.d("RecyclerView", "CLICK!")
        }

        private var view: View = v
        private var task: Task? = null

        init{
            v.setOnClickListener(this)
        }

        companion object {
            //5
            private val TASK_KEY = "TASK"
        }

        fun bindHolder(task: Task) {
            this.task = task
            view.title.text = task.title
            when(task.tags.size){
                4 -> {
                    view.tag_four.text = task.tags[3].name
                    view.tag_four.visibility = View.VISIBLE
                    view.tag_four.setBackgroundColor(Color.parseColor(task.tags[3].color))
                    view.tag_three.text = task.tags[2].name
                    view.tag_three.visibility = View.VISIBLE
                    view.tag_three.setBackgroundColor(Color.parseColor(task.tags[2].color))
                    view.tag_two.text = task.tags[1].name
                    view.tag_two.visibility = View.VISIBLE
                    view.tag_two.setBackgroundColor(Color.parseColor(task.tags[1].color))
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(Color.parseColor(task.tags[0].color))
                }
                3 -> {
                    view.tag_three.text = task.tags[2].name
                    view.tag_three.visibility = View.VISIBLE
                    view.tag_three.setBackgroundColor(Color.parseColor(task.tags[2].color))
                    view.tag_two.text = task.tags[1].name
                    view.tag_two.visibility = View.VISIBLE
                    view.tag_two.setBackgroundColor(Color.parseColor(task.tags[1].color))
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(Color.parseColor(task.tags[0].color))
                }
                2 -> {
                    view.tag_two.text = task.tags[1].name
                    view.tag_two.visibility = View.VISIBLE
                    view.tag_two.setBackgroundColor(Color.parseColor(task.tags[1].color))
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(Color.parseColor(task.tags[0].color))
                }
                1 -> {
                    view.tag_one.text = task.tags[0].name
                    view.tag_one.visibility = View.VISIBLE
                    view.tag_one.setBackgroundColor(Color.parseColor(task.tags[0].color))
                }
            }
            view.subtasks.text = task.subtaskDone().toString() + "/" + task.subTask.size
            view.date.text = task.date.toString()
            view.circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor(task.color))
        }

    }
}