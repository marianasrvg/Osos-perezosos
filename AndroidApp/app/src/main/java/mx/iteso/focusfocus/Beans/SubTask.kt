package mx.iteso.focusfocus.Beans

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Maritza on 19/03/2018.
 */
data class SubTask(var name: String,
                   var done : Boolean) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeByte(if (done) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubTask> {
        override fun createFromParcel(parcel: Parcel): SubTask {
            return SubTask(parcel)
        }

        override fun newArray(size: Int): Array<SubTask?> {
            return arrayOfNulls(size)
        }
    }
}