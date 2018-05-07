package mx.iteso.focusfocus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mx.iteso.focusfocus.Fragments.FragmentWork
import mx.iteso.focusfocus.util.NotificationUtil
import mx.iteso.focusfocus.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                FragmentWork.removeAlarm(context)
                PrefUtil.setTimerState(FragmentWork.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = FragmentWork.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                FragmentWork.removeAlarm(context)
                PrefUtil.setTimerState(FragmentWork.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val stopTime = FragmentWork.setAlarm(context, FragmentWork.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(FragmentWork.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, stopTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val stopTime = FragmentWork.setAlarm(context, FragmentWork.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(FragmentWork.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, stopTime)
            }
        }
    }
}
