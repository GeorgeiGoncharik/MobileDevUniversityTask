package xyz.goshanchik.prodavayka.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SaleAlarmWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {

        return Result.success()
    }
}