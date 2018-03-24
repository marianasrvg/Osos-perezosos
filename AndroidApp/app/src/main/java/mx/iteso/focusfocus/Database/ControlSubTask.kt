package mx.iteso.focusfocus.Database

import android.content.ContentValues
import mx.iteso.focusfocus.Beans.SubTask
import mx.iteso.focusfocus.Beans.Task

/**
 * Created by Maritza on 22/03/2018.
 */
class ControlSubTask {

    fun addSubTask(subTask : SubTask, dh : DataBaseHandler, idTask : Long): Long {
        var inserted: Long = 0
        var db = dh.writableDatabase
        var values = ContentValues()

        values.put(DataBaseHandler.KEY_SUBTASK_ID_TASK, idTask)
        values.put(DataBaseHandler.KEY_SUBTASK_NAME, subTask.name)
        values.put(DataBaseHandler.KEY_TASK_DATE, subTask.done)
        // Inserting Row
        inserted = db.insert(DataBaseHandler.TABLE_SUBTASK, null, values)
        // Closing database connection
        try {
            db.close()
        } catch (e: Exception) {
        }
        db = null
        return inserted;
    }
}