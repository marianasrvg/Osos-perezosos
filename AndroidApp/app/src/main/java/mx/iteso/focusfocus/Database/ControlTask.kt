package mx.iteso.focusfocus.Database

import mx.iteso.focusfocus.Beans.Task
import android.content.ContentValues
import mx.iteso.focusfocus.Beans.Priority
import mx.iteso.focusfocus.Beans.Status
import mx.iteso.focusfocus.Beans.SubTask
import java.util.Date
import kotlin.collections.ArrayList


/**
 * Created by Maritza on 21/03/2018.
 */
class ControlTask {

    fun addTask(task: Task, dh: DataBaseHandler): Long {
        var inserted: Long = 0
        var db = dh.writableDatabase
        var values = ContentValues()

        values.put(DataBaseHandler.KEY_TASK_TITLE, task.title)
        values.put(DataBaseHandler.KEY_TASK_DATE, task.date.time)
        values.put(DataBaseHandler.KEY_TASK_ESTIMATED_TIME, task.estimatedDate.time)
        values.put(DataBaseHandler.KEY_TASK_STATUS, task.status.name)
        values.put(DataBaseHandler.KEY_TASK_PRIORITY, task.priority.name)
        values.put(DataBaseHandler.KEY_TASK_DESCRIPTION, task.description)
        values.put(DataBaseHandler.KEY_TASK_COLOR, task.color)

        // Inserting Row
        inserted = db.insert(DataBaseHandler.TABLE_TASK, null, values)
        // Closing database connection
        try {
            db.close()
        } catch (e: Exception) {
        }

        db = null
        return inserted
    }

    fun getTaskByStatus(status: Status, dh: DataBaseHandler): ArrayList<Task> {
        val list = ArrayList<Task>()
        val selectQuery = ("SELECT " + DataBaseHandler.KEY_TASK_ID + "," +
                DataBaseHandler.KEY_TASK_TITLE + ", " +
                DataBaseHandler.KEY_TASK_COLOR + ", " +
                DataBaseHandler.KEY_TASK_DATE + ", " +
                DataBaseHandler.KEY_TASK_ESTIMATED_TIME + ", " +
                DataBaseHandler.KEY_TASK_PRIORITY + ", " +
                DataBaseHandler.KEY_TASK_STATUS + ", " +
                DataBaseHandler.KEY_TASK_DESCRIPTION + " FROM " +
                DataBaseHandler.TABLE_TASK + " WHERE " +
                DataBaseHandler.KEY_TASK_STATUS + " = '" +
                status.name + "'")
        val db = dh.getReadableDatabase()
        val cursor = db.rawQuery(selectQuery, null)
        while (cursor.moveToNext()) {
            val date = Date()
            val estimatedate = Date()
            date.time = cursor.getLong(3)
            estimatedate.time = cursor.getLong(4)
            val task = Task(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    ArrayList(),
                    date,
                    estimatedate,
                    Priority.valueOf(cursor.getString(5)),
                    Status.valueOf(cursor.getString(6)),
                    cursor.getString(7),
                    ArrayList<SubTask>())
            list.add(task)
        }
        try {
            cursor.close()
            db.close()
        } catch (e: Exception ) {}

        return list
    }

    fun updateState(status: Status, id: Int?, dh: DataBaseHandler): Int {
        val inserted: Long = 0
        var db = dh.writableDatabase
        val values = ContentValues()

        values.put(DataBaseHandler.KEY_TASK_STATUS, status.name)
        val count = db!!.update(DataBaseHandler.TABLE_TASK, values,
                DataBaseHandler.KEY_TASK_ID + " = ?",
                Array<String>(1) { id.toString() })
        try {
            db.close()
        } catch (e: Exception) {
        }

        db = null
        return count
    }
}