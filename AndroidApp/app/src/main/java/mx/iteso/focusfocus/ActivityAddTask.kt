package mx.iteso.focusfocus

import android.os.Bundle
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_add_task.titleTask
import kotlinx.android.synthetic.main.activity_add_task.list_tag
import kotlinx.android.synthetic.main.activity_add_task.list_item
import kotlinx.android.synthetic.main.activity_add_task.circle
import kotlinx.android.synthetic.main.activity_add_task.date
import kotlinx.android.synthetic.main.activity_add_task.descriptionTask
import kotlinx.android.synthetic.main.activity_add_task.spinner
import kotlinx.android.synthetic.main.activity_add_task.subtask
import kotlinx.android.synthetic.main.activity_add_task.time
import kotlinx.android.synthetic.main.activity_add_task.toolbar
import kotlinx.android.synthetic.main.activity_add_task.tag
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.ArrayList
import android.support.v4.content.ContextCompat
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.view.MenuItem
import android.widget.TimePicker
import android.widget.Toast
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.OnColorChangedListener
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.Beans.SubTask
import mx.iteso.focusfocus.Beans.Tag
import mx.iteso.focusfocus.Beans.Priority
import mx.iteso.focusfocus.Beans.Status
import mx.iteso.focusfocus.Database.ControlSubTask
import mx.iteso.focusfocus.Database.ControlTag
import mx.iteso.focusfocus.Database.ControlTask
import mx.iteso.focusfocus.Database.DataBaseHandler


class ActivityAddTask : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private var items: ArrayList<SubTask> = ArrayList()
    private var colorTitle: Int = R.color.colorPrimary
    private var tags: ArrayList<Tag> = ArrayList()
    private var estimatedTime = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(toolbar)

        val adapterList = ArrayAdapter (this,
                android.R.layout.simple_list_item_1,
                items )

        list_item.adapter = adapterList

        val adapterTag = ArrayAdapter (this, android.R.layout.simple_list_item_1, tags )
        list_tag.adapter = adapterTag

        val adapterSpinner = ArrayAdapter (this, android.R.layout.simple_spinner_item, Priority.values() )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        val dataPicker = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker,
                                   year: Int,
                                   monthOfYear: Int,
                                   dayOfMonth: Int) {
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
        } )
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
                        } )
                        .setOnColorSelectedListener(OnColorSelectedListener { selectedColor -> colorTitle = selectedColor })
                        .setPositiveButton("ok", ColorPickerClickListener { dialog, selectedColor, allColors ->
                            circle.backgroundTintList = ColorStateList.valueOf(colorTitle) } )
                        .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(this@ActivityAddTask, android.R.color.holo_blue_bright))
                        .build()
                        .show()
            }
        })
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
                if (titleTask.text.toString().equals("") || titleTask.text.toString().equals(" ")) {
                    Toast.makeText(this@ActivityAddTask, "No hay un nombre de tarea", Toast.LENGTH_LONG).show()
                } else {
                    var dh = DataBaseHandler.getInstance(this@ActivityAddTask)
                    val controlTask = ControlTask()
                    val controlSubTask = ControlSubTask()
                    val controlTag = ControlTag()
                    val taskToast = Task( null,
                            titleTask.text.toString(),
                            colorTitle,
                            tags,
                            cal.time,
                            estimatedTime.time,
                            spinner.selectedItem as Priority,
                            Status.NON_START,
                            descriptionTask.text.toString(),
                            items)
                    var value: Long = controlTask.addTask (
                            taskToast,
                            dh)
                    Toast.makeText(this@ActivityAddTask, value.toString(), Toast.LENGTH_LONG).show()
                    for (item in items) {
                        controlSubTask.addSubTask(item, dh, value)
                    }
                    for (tag in tags) {
                        val inserted = controlTag.addTag(tag, dh)
                        controlTag.addTagToTask(inserted, value, dh)
                    }

                    finish()
                }
            }
            R.id.action_close -> {
                finish()
            }
        }
        return false
    }
}
