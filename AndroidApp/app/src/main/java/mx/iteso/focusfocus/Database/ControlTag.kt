package mx.iteso.focusfocus.Database

import android.content.ContentValues
import mx.iteso.focusfocus.Beans.Tag
import mx.iteso.focusfocus.Beans.Task

/**
 * Created by Maritza on 22/03/2018.
 */
class ControlTag {

    fun addTag(tag: Tag, dh: DataBaseHandler): Long {
        var inserted: Long = 0
        var db = dh.writableDatabase
        var values = ContentValues()

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
        return inserted
    }

    fun getTagByIdTask (id: Int ?, dh: DataBaseHandler): ArrayList<Tag> {
        val tags = ArrayList<Tag>()

        val selectQuery = ("SELECT T." + DataBaseHandler.KEY_TAG_ID + ", T." +
                DataBaseHandler.KEY_TAG_NAME + ", T." +
                DataBaseHandler.KEY_TAG_COLOR + " FROM " +
                DataBaseHandler.TABLE_TAG + " T , " +
                DataBaseHandler.TABLE_TASK_TAG + " TT WHERE TT." +
                DataBaseHandler.KEY_TASK_TAG_ID_TASK + " = " +
                id + " AND TT." + DataBaseHandler.KEY_TASK_TAG_ID_TAG + " = T." +
                DataBaseHandler.KEY_TAG_ID)
        val db = dh.getReadableDatabase()
        val cursor = db.rawQuery(selectQuery, null)
        while (cursor.moveToNext()) {
            val tag = Tag(
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(0))
            tags.add(tag)
        }
        try {
            cursor.close()
            db.close()
        } catch (e: Exception ) {
        }
        return tags
    }

    fun getTags(dh: DataBaseHandler): ArrayList<Tag> {
        val tags = ArrayList<Tag>()
        val selectQuery = ("SELECT * FROM " + DataBaseHandler.TABLE_TAG)
        val db = dh.getReadableDatabase()
        val cursor = db.rawQuery(selectQuery, null)
        while (cursor.moveToNext()) {
            val tag = Tag(
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(0))
            tags.add(tag)
        }
        try {
            cursor.close()
            db.close()
        } catch (e: Exception ) {
        }
        return tags
    }

    fun addTagToTask(idTag: Long, idTask: Long, dh: DataBaseHandler): Long {
        var inserted: Long = 0
        var db = dh.writableDatabase
        var values = ContentValues()

        values.put(DataBaseHandler.KEY_TASK_TAG_ID_TASK, idTask)
        values.put(DataBaseHandler.KEY_TASK_TAG_ID_TAG, idTag)
        // Inserting Row
        inserted = db.insert(DataBaseHandler.TABLE_TASK_TAG, null, values)
        // Closing database connection
        try {
            db.close()
        } catch (e: Exception) {
        }
        db = null
        return inserted
    }

    fun changeColor(idTag: Long, color : Long,  dh : DataBaseHandler) {
        var db = dh.writableDatabase
        var values = ContentValues()
        values.put(DataBaseHandler.KEY_TAG_COLOR, color)
        db.update(DataBaseHandler.TABLE_TAG, values,
                "id = ?",
                Array<String>(1){idTag.toString()})

    }

    fun removeTag(tag: Tag, dh: DataBaseHandler): Int {
        val deleted: Long = 0
        var db = dh.writableDatabase
        val values = ContentValues()
        val cout = db!!.delete(DataBaseHandler.TABLE_TAG,
                DataBaseHandler.KEY_TAG_ID + " = ?",
                Array<String>(1) { tag.idTag.toString() })

        try {
            db.close()
        } catch (e: Exception) {
        }

        db = null
        return cout
    }

    fun removeTagTask(tag: Tag, dh: DataBaseHandler): Int {
        val deleted: Long = 0
        var db = dh.writableDatabase
        val values = ContentValues()
        val cout = db!!.delete(DataBaseHandler.TABLE_TASK_TAG,
                DataBaseHandler.KEY_TASK_TAG_ID_TAG + " = ?",
                Array<String>(1) { tag.idTag.toString() })

        try {
            db.close()
        } catch (e: Exception) {
        }

        db = null
        return cout
    }
}