package mx.iteso.focusfocus.Beans

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

/**
 * Created by Maritza on 19/03/2018.
 */
data class Task(
        var id: Int?,
        var title: String,
        var color: Int,
        var tags: ArrayList<Tag>,
        var date: Date,
        var estimatedDate: Date,
        var priority: Priority,
        var status: Status,
        var description: String,
        var subTask: ArrayList<SubTask>
): Parcelable, Comparable<Task> {


    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readInt(),
            TODO("tags"),
            TODO("date"),
            TODO("estimatedDate"),
            TODO("priority"),
            TODO("status"),
            parcel.readString(),
            TODO("subTask")) {
    }
    fun subtaskDone(): Int {
        var dones: Int = 0
        for (item: SubTask in subTask) {
            if (item.done) dones++
        }
        return dones
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeInt(color)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(other: Task): Int {
        if (other.date == this.date) {
            return other.priority.compareTo(this.priority)
        } else {
            return other.date.compareTo(this.date)
        }
    }

}