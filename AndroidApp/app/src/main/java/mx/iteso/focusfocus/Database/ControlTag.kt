package mx.iteso.focusfocus.Database

import android.content.ContentValues
import mx.iteso.focusfocus.Beans.SubTask
import mx.iteso.focusfocus.Beans.Tag

/**
 * Created by Maritza on 22/03/2018.
 */
class ControlTag {

    fun addTag(tag : Tag, dh : DataBaseHandler, idTask : Long): Long {
        var inserted: Long = 0
        var db = dh.writableDatabase
        var values = ContentValues()

        values.put(DataBaseHandler.KEY_TAG_ID_TASK, idTask)
        values.put(DataBaseHandler.KEY_TAG_NAME, tag.name)
        values.put(DataBaseHandler.KEY_TAG_COLOR, tag.color)
        // Inserting Row
        inserted = db.insert(DataBaseHandler.TABLE_TAG, null, values)
        // Closing database connection
        try {
            db.close()
        } catch (e: Exception) {
        }
        db = null
        return inserted;
    }
}