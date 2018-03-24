package mx.iteso.focusfocus.Database

import mx.iteso.focusfocus.Beans.Task
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.v4.app.NotificationCompat.getCategory

/**
 * Created by Maritza on 21/03/2018.
 */
class ControlTask {


    fun addTask(task : Task, dh : DataBaseHandler): Long {
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
        return inserted;
    }
}