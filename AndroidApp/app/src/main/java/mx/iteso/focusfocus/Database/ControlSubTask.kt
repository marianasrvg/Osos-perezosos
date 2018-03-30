package mx.iteso.focusfocus.Database

import android.content.ContentValues
import mx.iteso.focusfocus.Beans.SubTask

/**
 * Created by Maritza on 22/03/2018.
 */
class ControlSubTask {

    fun addSubTask(subTask: SubTask, dh: DataBaseHandler, idTask: Long): Long {
        var inserted: Long = 0
        var db = dh.writableDatabase
        var values = ContentValues()

        values.put(DataBaseHandler.KEY_SUBTASK_ID_TASK, idTask)
        values.put(DataBaseHandler.KEY_SUBTASK_NAME, subTask.name)
        values.put(DataBaseHandler.KEY_SUBTASK_DONE, subTask.done)
        // Inserting Row
        inserted = db.insert(DataBaseHandler.TABLE_SUBTASK, null, values)
        // Closing database connection
        try {
            db.close()
        } catch (e: Exception) {
        }
        db = null
        return inserted
    }

    fun getSubTaskByIdTask (id: Int ?, dh: DataBaseHandler): ArrayList<SubTask> {
        val subtasks = ArrayList<SubTask>()

        val selectQuery = ("SELECT " + DataBaseHandler.KEY_SUBTASK_NAME + "," +
                DataBaseHandler.KEY_SUBTASK_DONE + ", " +
                DataBaseHandler.KEY_SUBTASK_ID + ", " +
                DataBaseHandler.KEY_SUBTASK_ID_TASK + " FROM " +
                DataBaseHandler.TABLE_SUBTASK + " WHERE " +
                DataBaseHandler.KEY_SUBTASK_ID_TASK + " = " + id)
        val db = dh.getReadableDatabase()
        val cursor = db.rawQuery(selectQuery, null)
        while (cursor.moveToNext()) {
            val done: Boolean = cursor.getInt(1) > 0
            val sub = SubTask(
                    cursor.getString(0),
                    done,
                    cursor.getInt(2),
                    cursor.getInt(3))
            subtasks.add(sub)
        }
        try {
            cursor.close()
            db.close()
        } catch (e: Exception ) {
        }
        return subtasks
    }
}