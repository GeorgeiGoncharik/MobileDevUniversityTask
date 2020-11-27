package xyz.goshanchik.prodavayka.workers

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber
import xyz.goshanchik.prodavayka.CategoryActivity
import xyz.goshanchik.prodavayka.WORK_OFFER_EMPTY_OUTPUT
import xyz.goshanchik.prodavayka.WORK_OFFER_OUTPUT
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.util.NetManager

class OfferWorker(private val ctx: Context, params: WorkerParameters): Worker(ctx, params) {
    override fun doWork(): Result {
        Timber.d("Is running.")
        val manager = makeStatusNotification("We are looking for the best offer especially for YOU!", context = ctx)
        val repository = Repository(CommerceRoomDatabase.getDatabase(ctx), NetManager(ctx))
        val bestDiscountProduct = repository.getAllProducts().maxBy { it.discount }
        bestDiscountProduct?.let {
            Timber.d("bestDiscountProduct is not null. ${bestDiscountProduct.id}")
            val productIntent = Intent(ctx, CategoryActivity::class.java).apply {
                putExtra("product_id", it.id)
                putExtra("category_id", it.categoryId)
            }
//            val productPendingIntent: PendingIntent = TaskStackBuilder.create(ctx).run {
//                addNextIntentWithParentStack(productIntent)
//                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//            }
            makeStatusNotification("${it.name} is on ${it.discount}% discount!", ctx) //, productPendingIntent
        }
        val output = Data.Builder().putLong(WORK_OFFER_OUTPUT, bestDiscountProduct?.id ?: WORK_OFFER_EMPTY_OUTPUT).build()
        manager?.cancelAll()
        return Result.success(output)
    }
}