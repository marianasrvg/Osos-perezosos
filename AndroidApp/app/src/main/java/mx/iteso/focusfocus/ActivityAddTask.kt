package mx.iteso.focusfocus

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.ArrayList
import android.widget.*

import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.OnColorChangedListener
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.dialog_tag.view.*
import mx.iteso.focusfocus.Beans.Priority
import mx.iteso.focusfocus.Beans.SubTask
import mx.iteso.focusfocus.Beans.Tag
import mx.iteso.focusfocus.Beans.Task
import mx.iteso.focusfocus.Beans.Status
import mx.iteso.focusfocus.Database.ControlSubTask
import mx.iteso.focusfocus.Database.ControlTag
import mx.iteso.focusfocus.Database.ControlTask
import mx.iteso.focusfocus.Database.DataBaseHandler

class ActivityAddTask : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    private var cal = Calendar.getInstance()
    private var items: ArrayList<SubTask> = ArrayList()
    private var tags: ArrayList<Tag> = ArrayList()
    private var estimatedTime = Calendar.getInstance()
    private var isEdit = false
    private var taskToLoad: Task? = null

    var adapterTag : ArrayAdapter<Tag>? = null
    var adapterList : ArrayAdapter<*>? = null


    /**
     * Date Picker
     */
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(toolbar)
        adapterList = ArrayAdapter (this,
                android.R.layout.simple_list_item_1,
                items )
        list_item.adapter = adapterList
        val adapterSpinner = ArrayAdapter (this, android.R.layout.simple_spinner_item, Priority.values() )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner
        date.setOnClickListener(this)
        time.setOnClickListener(this)
        subtask.setOnEditorActionListener(this)
        tag.setOnClickListener(this)
        circle.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        circle.setOnClickListener(this)
        if (intent != null && intent.extras != null)
            taskToLoad = intent.extras["TASK"] as Task
        if (taskToLoad != null) {
            isEdit = true
            loadTaskData(taskToLoad!!)
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            date.id -> {
                DatePickerDialog(this@ActivityAddTask,
                        dataPicker,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            time.id -> createDialogTime(time)
            circle.id -> colorPickerView(circle)
            tag.id -> createDialogTag()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when(v!!.id) {
            subtask.id -> {
                if (actionId == EditorInfo.IME_ACTION_DONE ) {
                    if (v.text.equals("") || v.text.equals(" ")) {
                    } else {
                        items.add( SubTask(v.text.toString(),
                                false,
                                null,
                                null))
                        v.setText("")
                        adapterList!!.notifyDataSetChanged()
                    }
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    true
                } else {
                    false
                }
            }
        }
        return true
    }

    private fun loadTaskData(taskToLoad: Task) {
        titleTask.setText(taskToLoad.title)
        circle.backgroundTintList = ColorStateList.valueOf(taskToLoad.color)
        spinner.setSelection(taskToLoad.priority!!.ordinal)
        cal.time = taskToLoad.date
        estimatedTime.time = taskToLoad.estimatedDate
        descriptionTask.setText(taskToLoad.description)
        taskToLoad.subTask!!.forEach {
            items.add(it)
        }
        (list_item.adapter as ArrayAdapter<SubTask>).notifyDataSetChanged()
        var i = 0;
        taskToLoad.tags.forEach {
            tags.add(it)
            when(i) {
                0 -> {
                    tag1.setText(it.name)
                    tag1.setBackgroundColor(it.color)
                }
                1 -> {
                    tag2.setText(it.name)
                    tag2.setBackgroundColor(it.color)
                }
                2 -> {
                    tag3.setText(it.name)
                    tag3.setBackgroundColor(it.color)
                }
            }
            i++;
        }
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

    fun getAllTags() : ArrayList<Tag> {
        val controlTag = ControlTag()
        var dh = DataBaseHandler.getInstance(this@ActivityAddTask)
        return controlTag.getTags(dh)
    }

    fun createDialogTag() {
        var allTags = getAllTags()
        var tagSelected = ArrayList<Tag>()
        val builder = AlertDialog.Builder(this@ActivityAddTask)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.dialog_tag, null)
        builder.setView(view)
        view.circleD.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        view.circleD.setOnClickListener(View.OnClickListener { colorPickerView(view.circleD) })
        view.tagNameD.setOnEditorActionListener(this)
        view.tag1D.setOnLongClickListener(View.OnLongClickListener { showPopUpTagSelected(view.tag1D, tagSelected, allTags, tagSelected.get(0)) })
        view.tag2D.setOnLongClickListener(View.OnLongClickListener { showPopUpTagSelected(view.tag2D, tagSelected, allTags, tagSelected.get(1)) })
        view.tag3D.setOnLongClickListener(View.OnLongClickListener { showPopUpTagSelected(view.tag3D, tagSelected, allTags, tagSelected.get(2)) })
        view.tagAdd.setOnClickListener(View.OnClickListener {
            if(view.tagNameD.text.toString() != "" && view.tagNameD.text.toString() != " ") {
                var tag = addNewTag(Tag(view.tagNameD.text.toString(),
                        view.circleD.backgroundTintList.defaultColor,
                        0))
                view.tagNameD.text =  SpannableStringBuilder("");
                allTags.add(tag)
                adapterTag!!.notifyDataSetChanged()
                view.circleD.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            }
            else {
                Toast.makeText(this@ActivityAddTask, "Nombre de etiqueta inválido", Toast.LENGTH_LONG).show()
            }
        })
        adapterTag = object : ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1,allTags) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val v = super.getView(position, convertView, parent) as TextView
                v.setBackgroundColor(allTags[position].color)
                v.gravity = Gravity.CENTER
                v.text = allTags[position].name
                v.setOnClickListener(
                        View.OnClickListener {
                            addTagTask(view.tag1D, view.tag2D, view.tag3D, tagSelected, allTags, position)
                            adapterTag!!.notifyDataSetChanged()
                        }
                )
                v.setOnLongClickListener {
                    showPopUpTag(v, allTags, position)
                    true
                }
                return v
            }
        }
        view.grid.adapter = adapterTag
        builder.setPositiveButton("ACCEPT", DialogInterface.OnClickListener{
            dialogInterface, i ->
            Log.v("hola", "tag")
            acceptTags(tagSelected)}).setNegativeButton("CANCEL", DialogInterface.OnClickListener {
            dialogInterface, i ->
            dialogInterface.dismiss() })
        builder.create().show()
    }

    private fun acceptTags(tagS : ArrayList<Tag>) {
        if(!tagS.isEmpty()) {
            if(tagS.size == 3) {
                tag1.setText(tagS.get(0).name)
                tag1.setBackgroundColor(tagS.get(0).color)
                tag2.setText(tagS.get(1).name)
                tag2.setBackgroundColor(tagS.get(1).color)
                tag3.setText(tagS.get(2).name)
                tag3.setBackgroundColor(tagS.get(2).color)
            } else if( tagS.size == 2) {
                tag1.setText(tagS.get(0).name)
                tag1.setBackgroundColor(tagS.get(0).color)
                tag2.setText(tagS.get(1).name)
                tag2.setBackgroundColor(tagS.get(1).color)
            } else {
                tag1.setText(tagS.get(0).name)
                tag1.setBackgroundColor(tagS.get(0).color)
            }
        }
        tags = tagS
    }

    private fun addNewTag(tag : Tag) : Tag {
        var controlTag = ControlTag()
        var dh = DataBaseHandler.getInstance(this@ActivityAddTask)
        var id = controlTag.addTag(tag, dh)
        tag.idTag = id.toInt()
        return tag
    }

    private fun showPopUpTagSelected(v: TextView, tagSelected : ArrayList<Tag>, allTags : ArrayList<Tag>, tag : Tag): Boolean {
        val popup = PopupMenu(this@ActivityAddTask, v)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            menuItemClickTagSelected(it, v, tagSelected, allTags, tag)
        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_task_popup, popup.menu)
        popup.show()
        return true
    }

    private fun menuItemClickTagSelected(it: MenuItem, v : TextView, tagSelected : ArrayList<Tag>, allTags : ArrayList<Tag>, tag : Tag): Boolean {
        return when (it.itemId) {
            R.id.menu_task_popup_Edit -> {
                colorPickerTag(tag, v)
                true
            }
            R.id.menu_task_popup_Delete -> {
                Log.d("Delete", "delete")
                v.text = ""
                v.setBackgroundColor(resources.getColor(R.color.transparent))
                tagSelected.remove(tag)
                allTags.add(tag)
                adapterTag!!.notifyDataSetChanged()
                true
            }
            else -> {
                false
            }
        }
    }



    private fun addTagTask(tag1 : TextView, tag2 : TextView, tag3 : TextView, tagSelected: ArrayList<Tag>, allTags : ArrayList<Tag>, position : Int) {
        if(tag1.text.equals(" ") || tag1.text.equals("")){
            tag1.text = allTags[position].name
            tag1.setBackgroundColor(allTags[position].color)
            if(tagSelected.isEmpty()) {
                tagSelected.add(allTags.get(position))
            }
            else {
                tagSelected.set(0, allTags.get(position))
            }
            allTags.removeAt(position)
            adapterTag!!.notifyDataSetChanged()
        }
        else if(tag2.text.equals("")) {
            tag2.text = allTags[position].name
            tag2.setBackgroundColor(allTags[position].color)
            if(tagSelected.size <= 1) {
                tagSelected.add(allTags.get(position))
            }
            else {
                tagSelected.set(1, allTags.get(position))
            }
            allTags.removeAt(position)
            adapterTag!!.notifyDataSetChanged()
        }
        else if(tag3.text.equals("")) {
            tag3.text = allTags[position].name
            tag3.setBackgroundColor(allTags[position].color)
            if(tagSelected.size <= 2) {
                tagSelected.add(allTags.get(position))
            }
            else {
                tagSelected.set(1, allTags.get(position))
            }
            allTags.removeAt(position)
            adapterTag!!.notifyDataSetChanged()
        }
        else {
            Toast.makeText(this@ActivityAddTask,
                    "No se pueden agregar más de 3 categorías",
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun showPopUpTag(v: View, allTags : ArrayList<Tag>, position : Int) {
        val popup = PopupMenu(this@ActivityAddTask, v)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            menuItemClickTag(it, allTags,  position, v )
        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_task_popup, popup.menu)
        popup.show()
    }

    private fun menuItemClickTag(it: MenuItem, allTags : ArrayList<Tag>, position : Int, v: View): Boolean {
        return when (it.itemId) {
            R.id.menu_task_popup_Edit -> {
                colorPickerTag(allTags.get(position), v)
                adapterTag!!.notifyDataSetChanged()
                //updateTag(allTags, position)
                true
            }

            R.id.menu_task_popup_Delete -> {
                Log.d("Delete", "delete")
                removeTagWithId(allTags, position)
                true
            }

            else -> {
                false
            }
        }
    }


    private fun removeTagWithId(allTags : ArrayList<Tag>, position : Int) {
        var dh = DataBaseHandler.getInstance(this@ActivityAddTask)
        val controlTag = ControlTag()
        if (allTags[position].idTag != null) {
            //Eliminar de la tabla TAG - TASK
            controlTag.removeTagTask(allTags[position], dh)
            controlTag.removeTag(allTags[position], dh)
            allTags.removeAt(position)
        }
        adapterTag!!.notifyDataSetChanged()
    }


    private fun createDialogTime(tv: TextView) {
        val builder = AlertDialog.Builder(this@ActivityAddTask)
        val inflater = this.getLayoutInflater()
        val v = inflater.inflate(R.layout.dialog_time, null)
        builder.setView(v)
        val hour = v.findViewById<NumberPicker>(R.id.dialog_hour)
        val minute = v.findViewById<NumberPicker>(R.id.dialog_minute)
        builder.setPositiveButton("ACCEPT", DialogInterface.OnClickListener { dialogInterface, i ->
            tv.setText(hour.value.toString() + ":" + minute.value.toString())
        }).setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
        builder.create().show()
    }


    private fun colorPickerView(v : View) {
        val context = this@ActivityAddTask
        var newColor = 0
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Color")
                .initialColor(resources.getColor(R.color.colorPrimary))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(OnColorChangedListener { selectedColor ->
                    // Handle on color change
                    newColor = selectedColor
                })
                .setOnColorSelectedListener(OnColorSelectedListener { selectedColor ->
                    newColor = selectedColor
                })
                .setPositiveButton("ok", ColorPickerClickListener { dialog, selectedColor, allColors ->
                    v.backgroundTintList = ColorStateList.valueOf(newColor) })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> })
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(this@ActivityAddTask, android.R.color.holo_blue_bright))
                .build()
                .show()
    }

    private fun colorPickerTag(tag : Tag, v : View) {
        val context = this@ActivityAddTask
        var newColor = 0
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Color")
                .initialColor(resources.getColor(R.color.colorPrimary))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(OnColorChangedListener { selectedColor ->
                    // Handle on color change
                    newColor = selectedColor
                })
                .setOnColorSelectedListener(OnColorSelectedListener { selectedColor -> newColor = selectedColor })
                .setPositiveButton("ok", ColorPickerClickListener { dialog, selectedColor, allColors ->
                    tag.color = newColor
                    v.setBackgroundColor(tag.color)
                    var controlTag = ControlTag()
                    var db = DataBaseHandler.getInstance(this@ActivityAddTask)
                    controlTag.changeColor(tag.idTag, tag.color, db)
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> })
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(this@ActivityAddTask, android.R.color.holo_blue_bright))
                .build()
                .show()
    }

    private fun tryToWriteTask() {
        if (titleTask.text.toString().equals("") || titleTask.text.toString().equals(" ")) {
            Toast.makeText(this@ActivityAddTask, "No hay un nombre de tarea", Toast.LENGTH_LONG).show()
        } else {
            var dh = DataBaseHandler.getInstance(this@ActivityAddTask)
            val controlTask = ControlTask()
            val controlSubTask = ControlSubTask()
            val controlTag = ControlTag()
            val priority = if (spinner.selectedItem == null) Priority.HIGH else spinner.selectedItem as Priority
            val taskToast = Task(null,
                    titleTask.text.toString(),
                    circle.backgroundTintList.defaultColor,
                    tags,
                    cal.time,
                    estimatedTime.time,
                    priority,
                    Status.NON_START,
                    descriptionTask.text.toString(),
                    items)

            var value: Int
            if (!isEdit) {
                value = controlTask.addTask(
                        taskToast,
                        dh)
                for (item in items) {
                    controlSubTask.addSubTask(item, dh, value)
                }
                for (tag in tags) {
                    val inserted = tag.idTag
                    controlTag.addTagToTask(inserted, value, dh)
                }
            } else {
                for(tagDel in taskToLoad!!.tags) {
                    controlTag.removeTagFromTask(taskToLoad!!.id!!, dh)
                }

                taskToLoad!!.tags = tags
                controlTask.updateTask(taskToLoad!!.id!!, taskToast, dh)

                for (tag in taskToLoad!!.tags) {
                    controlTag.addTagToTask(tag.idTag, taskToLoad!!.id!!, dh)
                }
            }

            //Toast.makeText(this@ActivityAddTask, value.toString(), Toast.LENGTH_LONG).show()

            finish()
        }
    }
}
