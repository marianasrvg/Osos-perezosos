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
    var tags: List<Tag>,
    var date: Date,
    var estimatedDate: Date,
    var priority: Priority,
    var status: Status,
    var description: String,
    var subTask: List<SubTask>
) : Comparable<Task>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readInt(),
            parcel.createTypedArrayList(Tag),
            Date(parcel.readLong()),
            Date(parcel.readLong()),
            parcel.readParcelable(Priority::class.java.classLoader),
            parcel.readParcelable(Status::class.java.classLoader),
            parcel.readString(),
            parcel.createTypedArrayList(SubTask)) {
    }

    fun subtaskDone(): Int {
        var dones: Int = 0
        for (item: SubTask in subTask) {
            if (item.done) dones++
        }
        return dones
    }

    override fun compareTo(other: Task): Int {
        if (other.date == this.date) {
            return other.priority.compareTo(this.priority)
        } else {
            return other.date.compareTo(this.date)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeInt(color)
        parcel.writeTypedList(tags)
        parcel.writeLong(date.time)
        parcel.writeLong(estimatedDate.time)
        parcel.writeParcelable(priority, flags)
        parcel.writeParcelable(status, flags)
        parcel.writeString(description)
        parcel.writeTypedList(subTask)
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

}