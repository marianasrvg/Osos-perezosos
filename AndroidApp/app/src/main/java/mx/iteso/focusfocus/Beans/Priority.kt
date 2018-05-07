package mx.iteso.focusfocus.Beans

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Maritza on 19/03/2018.
 */
enum class Priority() : Parcelable {

    HIGH, MEDIUM, LOW;

    constructor(parcel: Parcel) : this() {
        parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(parcel.readInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Priority> {
        override fun createFromParcel(parcel: Parcel): Priority {
            return Priority.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<Priority?> {
            return arrayOfNulls(size)
        }
    }
}
