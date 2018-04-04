package mx.iteso.focusfocus.Beans

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Maritza on 19/03/2018.
 */
enum class Status() : Parcelable {
    IN_PROGRESS, DONE, NON_START;

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Status> {
        override fun createFromParcel(parcel: Parcel): Status {
            return Status.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<Status?> {
            return arrayOfNulls(size)
        }
    }
}