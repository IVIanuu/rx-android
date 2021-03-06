/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.rxandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import io.reactivex.Observable

/**
 * Static factory methods to create [Observable]'s of broadcasts
 */
object RxBroadcastReceiver {

    fun create(
            context: Context,
            intentFilter: IntentFilter,
            broadcastPermission: String? = null,
            schedulerHandler: Handler? = null
    ): Observable<Intent> = Observable.create { e ->
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!e.isDisposed) {
                    e.onNext(intent)
                }
            }
        }

        e.setCancellable { context.unregisterReceiver(broadcastReceiver) }

        context.registerReceiver(
                broadcastReceiver, intentFilter, broadcastPermission, schedulerHandler)
    }
}