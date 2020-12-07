package xyz.goshanchik.prodavayka.workers

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber
import xyz.goshanchik.prodavayka.WORK_OFFER_EMPTY_OUTPUT
import xyz.goshanchik.prodavayka.WORK_OFFER_OUTPUT
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.util.NetManager

class OfferWorker(private val ctx: Context, params: WorkerParameters): Worker(ctx, params) {
    override fun doWork(): Result {
        Timber.d("Is running.")
        val repository = Repository(CommerceRoomDatabase.getDatabase(ctx), NetManager(ctx))
        val bestDiscountProduct = repository.getAllProducts().maxByOrNull { it.discount }
        bestDiscountProduct?.let {
            makeStatusNotification("${it.name} is on ${it.discount}% discount!", ctx) //, productPendingIntent
        }
        val output = Data.Builder().putLong(WORK_OFFER_OUTPUT, bestDiscountProduct?.id ?: WORK_OFFER_EMPTY_OUTPUT).build()
        return Result.success(output)
    }
}