package mx.iteso.focusfocus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.OnColorChangedListener
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_add_task.*
import mx.iteso.focusfocus.Beans.*
import mx.iteso.focusfocus.Database.ControlSubTask
import mx.iteso.focusfocus.Database.ControlTag
import mx.iteso.focusfocus.Database.ControlTask
import mx.iteso.focusfocus.Database.DataBaseHandler
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.ArrayList

class ActivityAddTask : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private var items: ArrayList<SubTask> = ArrayList()
    private var colorTitle: Int = R.color.colorPrimary
    private var tags: ArrayList<Tag> = ArrayList()
    private var estimatedTime = Calendar.getInstance()
    private var isEdit = false
    private var taskToLoad: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(toolbar)

        val adapterList = ArrayAdapter (this,
                android.R.layout.simple_list_item_1,
                items )

        list_item.adapter = adapterList

        //val adapterTag = ArrayAdapter (this, android.R.layout.simple_list_item_1, tags )

        val adapterTag = object : ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, tags) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val v = super.getView(position, convertView, parent) as TextView
                v.setBackgroundColor(tags[position].color)
                v.text = tags[position].name
                v.setOnClickListener(
                        View.OnClickListener {
                            val context = this@ActivityAddTask
                            var newColor = 0
                            ColorPickerDialogBuilder
                                    .with(context)
                                    .setTitle("Color")
                                    .initialColor(colorTitle)
                                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                    .density(12)
                                    .setOnColorChangedListener(OnColorChangedListener { selectedColor ->
                                        // Handle on color change
                                        newColor = selectedColor
                                    })
                                    .setOnColorSelectedListener(OnColorSelectedListener { selectedColor -> newColor = selectedColor })
                                    .setPositiveButton("ok", ColorPickerClickListener { dialog, selectedColor, allColors ->
                                        tags[position].color = newColor
                                        notifyDataSetChanged()
                                    })
                                    .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> })
                                    .showColorEdit(true)
                                    .setColorEditTextColor(ContextCompat.getColor(this@ActivityAddTask, android.R.color.holo_blue_bright))
                                    .build()
                                    .show()
                        }
                )
                return v
            }
        }

        list_tag.adapter = adapterTag

        val adapterSpinner = ArrayAdapter (this, android.R.layout.simple_spinner_item, Priority.values() )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        val dataPicker = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                DatePickerDialog(this@ActivityAddTask,
                        dataPicker,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
        var timePicker = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, selectHour: Int, selectMinute: Int) {
                estimatedTime.set(Calendar.HOUR, selectHour)
                estimatedTime.set(Calendar.MINUTE, selectMinute)
                updateTimeInView()
            }
        }
        time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var mTime = Calendar.getInstance()
                TimePickerDialog(this@ActivityAddTask,
                        timePicker,
                        estimatedTime.get(Calendar.HOUR),
                        estimatedTime.get(Calendar.MINUTE), false).show()
            }
        })
        subtask.setOnEditorActionListener() { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ) {
                if (v.text.equals("") || v.text.equals(" ")) {
                } else {
                    items.add( SubTask(v.text.toString(),
                                    false,
                                    null,
                                    null))
                    v.setText("")
                    adapterList.notifyDataSetChanged()
                }
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                true
            } else {
                false
            }
        }

        tag.setOnEditorActionListener() { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ) {
                if (!v.text.equals("")) {
                    tags.add(Tag(
                            v.text.toString(),
                            colorTitle,
                            null))
                    v.setText("")
                    adapterTag.notifyDataSetChanged()
                }
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                true
            } else {
                false
            }
        }

        circle.backgroundTintList = ColorStateList.valueOf(colorTitle)
        circle.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val context = this@ActivityAddTask
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Color")
                        .initialColor(colorTitle)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorChangedListener(OnColorChangedListener { selectedColor ->
                            // Handle on color change
                            colorTitle = selectedColor
                        })
                        .setOnColorSelectedListener(OnColorSelectedListener { selectedColor -> colorTitle = selectedColor })
                        .setPositiveButton("ok", ColorPickerClickListener { dialog, selectedColor, allColors ->
                            circle.backgroundTintList = ColorStateList.valueOf(colorTitle) })
                        .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(this@ActivityAddTask, android.R.color.holo_blue_bright))
                        .build()
                        .show()
            }
        })

        if (intent != null && intent.extras != null)
            taskToLoad = intent.extras["TASK"] as Task
        if (taskToLoad != null) {
            isEdit = true
            loadTaskData(taskToLoad!!)
        }
    }

    private fun loadTaskData(taskToLoad: Task) {
        titleTask.setText(taskToLoad.title)
        spinner.setSelection(taskToLoad.priority.ordinal)
        cal.time = taskToLoad.date
        estimatedTime.time = taskToLoad.estimatedDate
        descriptionTask.setText(taskToLoad.description)
        taskToLoad.subTask.forEach {
            items.add(it)
        }
        (list_item.adapter as ArrayAdapter<SubTask>).notifyDataSetChanged()
        taskToLoad.tags.forEach {
            tags.add(it)
        }
        (list_tag.adapter as ArrayAdapter<SubTask>).notifyDataSetChanged()
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date.setText(sdf.format(cal.getTime()))
    }

    private fun updateTimeInView() {
        val myFormat = "HH:mm" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        time.setText(sdf.format(estimatedTime.getTime()))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_task, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_check -> {
                tryToWriteTask()
            }
            R.id.action_close -> {
                finish()
            }
        }
        return false
    }

    private fun tryToWriteTask() {
        if (titleTask.text.toString().equals("") || titleTask.text.toString().equals(" ")) {
            Toast.makeText(this@ActivityAddTask, "No hay un nombre de tarea", Toast.LENGTH_LONG).show()
        } else {
            var dh = DataBaseHandler.getInstance(this@ActivityAddTask)
            val controlTask = ControlTask()
            val controlSubTask = ControlSubTask()
            val controlTag = ControlTag()
            val taskToast = Task(null,
                    titleTask.text.toString(),
                    colorTitle,
                    tags,
                    cal.time,
                    estimatedTime.time,
                    spinner.selectedItem as Priority,
                    Status.NON_START,
                    descriptionTask.text.toString(),
                    items)

            var value: Long
            if (!isEdit) {
                value = controlTask.addTask(
                        taskToast,
                        dh)
                for (item in items) {
                    controlSubTask.addSubTask(item, dh, value)
                }
                for (tag in tags) {
                    val inserted = controlTag.addTag(tag, dh)
                    controlTag.addTagToTask(inserted, value, dh)
                }
            } else {
                value = controlTask.updateTask(taskToLoad!!.id!!, taskToast, dh)
            }

            Toast.makeText(this@ActivityAddTask, value.toString(), Toast.LENGTH_LONG).show()

            finish()
        }
    }
}
