package mx.iteso.focusfocus.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Maritza on 20/03/2018.
 */
class DataBaseHandler (context : Context,
                       name : String ?,
                       factory : SQLiteDatabase.CursorFactory ?,
                       version : Int) :
        SQLiteOpenHelper(context,
                DATABASE_NAME,
                factory,
                DATABASE_VERSION) {

    companion object {

        /**
         * Table name of task.
         */
        @JvmField val TABLE_TASK = "task"
        /**
         * Column idTask
         */
        @JvmField val KEY_TASK_ID = "idTask"
        /**
         * Column Title.
         */
        @JvmField val KEY_TASK_TITLE = "title"
        /**
         * Column Date.
         */
        @JvmField val KEY_TASK_DATE = "date"
        /**
         * Column Estimated Time
         */
        @JvmField val KEY_TASK_ESTIMATED_TIME = "estimatedTime"
        /**
         * Column Status.
         */
        @JvmField val KEY_TASK_STATUS = "status"
        /**
         * Column Priority.
         */
        @JvmField val KEY_TASK_PRIORITY = "priority"
        /**
         * Column Description.
         */
        @JvmField val KEY_TASK_DESCRIPTION = "description"
        /**
         * Column Color.
         */
        @JvmField val KEY_TASK_COLOR = "color"

        /**
         * Table Subtask.
         */
        @JvmField val TABLE_SUBTASK = "subtask"
        /**
         * Column id subtask.
         */
        @JvmField val KEY_SUBTASK_ID = "idSubtask"
        /**
         * ID of Task on Subtask
         */
        @JvmField val KEY_SUBTASK_ID_TASK = "idTask"
        /**
         * Column name subtask.
         */
        @JvmField val KEY_SUBTASK_NAME = "name"
        /**
         * Column status subtask.
         */
        @JvmField val KEY_SUBTASK_DONE = "done"

        /**
         * Table Tag.
         */
        @JvmField val TABLE_TAG = "tag"
        /**
         * Column id Tag.
         */
        @JvmField val KEY_TAG_ID = "idTag"
        /**
         * Column name Tag.
         */
        @JvmField val KEY_TAG_NAME = "name"
        /**
         * Column color Tag.
         */
        @JvmField val KEY_TAG_COLOR = "color"

        /**
         * Table detail tag - task
         */
        @JvmField val TABLE_TASK_TAG = "taskTag"
        /**
         * Column idTag
         */
        @JvmField val KEY_TASK_TAG_ID_TAG = "idTag"
        /**
         * Column idTask
         */
        @JvmField val KEY_TASK_TAG_ID_TASK = "idTask"

        /**
         * Name database.
         */
        private val DATABASE_NAME = "FocusFocus.db"
        /**
         * Version of database.
         */
        private val DATABASE_VERSION = 1

        private var dataBaseHandler : DataBaseHandler ? = null

        fun getInstance(context: Context): DataBaseHandler {
            if (dataBaseHandler == null) {
                dataBaseHandler = DataBaseHandler(context, DATABASE_NAME, null, DATABASE_VERSION)
            }
            return dataBaseHandler as DataBaseHandler
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableTask())
        db.execSQL(createTableTag())
        db.execSQL(createTableSubTask())
        db.execSQL(createTableTaskTag())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun createTableTask(): String {
        val CREATE_TASK_TABLE = (
                "CREATE TABLE " + TABLE_TASK + "("
                        + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_TASK_TITLE + " TEXT, "
                        + KEY_TASK_DATE + " INTEGER, "
                        + KEY_TASK_ESTIMATED_TIME + " INTEGER,"
                        + KEY_TASK_STATUS + " TEXT, "
                        + KEY_TASK_PRIORITY + " TEXT, "
                        + KEY_TASK_DESCRIPTION + " TEXT, "
                        + KEY_TASK_COLOR + " INTEGER)"
                )
        return CREATE_TASK_TABLE
    }

    fun createTableTag(): String {
        val CREATE_TAG_TABLE = (
                "CREATE TABLE " + TABLE_TAG + "("
                        + KEY_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_TAG_NAME + " TEXT, "
                        + KEY_TAG_COLOR + " INTEGER)"
                )
        return CREATE_TAG_TABLE
    }

    fun createTableTaskTag() : String {
        val CREATE_TABLE_TASK_TAG = (
                "CREATE TABLE " + TABLE_TASK_TAG + "("
                + KEY_TASK_TAG_ID_TASK + " INTEGER, "
                + KEY_TASK_TAG_ID_TAG + " INTEGER, "
                + "FOREIGN KEY(" + KEY_TASK_TAG_ID_TASK  + ") REFERENCES " + TABLE_TASK + "(" + KEY_TASK_ID + ")"
                + "FOREIGN KEY(" + KEY_TASK_TAG_ID_TAG  + ") REFERENCES " + TABLE_TAG + "(" + KEY_TAG_ID + "))"
                )
        return CREATE_TABLE_TASK_TAG
    }

    fun createTableSubTask(): String {
        val CREATE_SUBTASK_TABLE = (
                "CREATE TABLE " + TABLE_SUBTASK + "("
                        + KEY_SUBTASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_SUBTASK_ID_TASK + " INTEGER,"
                        + KEY_SUBTASK_NAME + " TEXT,"
                        + KEY_SUBTASK_DONE + " INTEGER,"
                        + "FOREIGN KEY(" + KEY_SUBTASK_ID_TASK  + ") REFERENCES " + TABLE_TASK + "(" + KEY_TASK_ID + "))"

                )
        return CREATE_SUBTASK_TABLE
    }

}