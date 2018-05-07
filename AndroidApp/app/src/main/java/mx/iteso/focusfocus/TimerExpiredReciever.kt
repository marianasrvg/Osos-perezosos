package mx.iteso.focusfocus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mx.iteso.focusfocus.Fragments.FragmentWork
import mx.iteso.focusfocus.util.NotificationUtil
import mx.iteso.focusfocus.util.PrefUtil

class TimerExpiredReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(FragmentWork.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)

    }
}
