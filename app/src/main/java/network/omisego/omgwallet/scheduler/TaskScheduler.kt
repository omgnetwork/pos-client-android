package network.omisego.omgwallet.scheduler

import co.omisego.omisego.custom.retrofit2.adapter.OMGCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.omisego.omgwallet.extension.safeExecute
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.util.IdlingResourceUtil

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class MainScheduler<T>(
    private val backgroundTask: IOTasks<T>,
    private val mainTask: (result: List<APIResult>) -> Unit
) {
    fun run() {
        GlobalScope.launch(Dispatchers.Main) {
            IdlingResourceUtil.idlingResource.increment()
            mainTask.invoke(
                withContext(Dispatchers.Default) { backgroundTask() }
            )
            IdlingResourceUtil.idlingResource.decrement()
        }
    }
}

class IOTasks<T>(private vararg val chains: OMGCall<T>) {
    suspend operator fun invoke(): List<APIResult> {
        return withContext(Dispatchers.IO) {
            chains.map { it.safeExecute() }
        }
    }
}
