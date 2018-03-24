package mx.iteso.focusfocus.Beans

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by Maritza on 19/03/2018.
 */
data class Task(var title : String,
                var color : String,
                var tags : ArrayList<Tag>,
                var date: Date,
                var estimatedDate : Date,
                var priority : Priority,
                var status : Status,
                var description : String,
                var subTask : ArrayList<SubTask>) : Parcelable{


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            TODO("tags"),
            TODO("date"),
            TODO("estimatedDate"),
            TODO("priority"),
            TODO("status"),
            parcel.readString(),
            TODO("subTask")) {
    }

    fun subtaskDone() : Int {
        var dones : Int = 0
        for(item : SubTask in subTask){
            if(item.done) dones++
        }
        return dones
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(color)
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

}