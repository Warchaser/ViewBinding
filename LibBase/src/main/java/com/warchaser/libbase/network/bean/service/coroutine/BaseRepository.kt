package com.warchaser.libbase.network.bean.service.coroutine


open class BaseRepository{

//    suspend fun <T : Any> apiCall(call : suspend () -> Response<T>) : Response<T> = call.invoke()
//
//    suspend fun <T : Any> safeApiCall(call : suspend () -> Result<T>, errorMsg : String) : Result<T> = try {
//        call()
//    } catch (e : Exception){
//        Result.Error(errorMsg)
//    }

//    suspend fun <T : Any> executeResponse(response: Response<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null, errorBlock: (suspend  CoroutineScope.() -> Unit)? = null): Result<T> = coroutineScope{
//        if(response.errorCode == -1){
//            errorBlock?.let { it() }
//            Result.Error(response.errorMsg)
//        } else {
//            successBlock?.let { it() }
//            Result.Success(response.body)
//        }
//    }
//
//    suspend fun <T : Any> executeResponseFlow(response : Response<T>): Flow<Result<T>> = flow {
//        response.run {
//            if(errorCode == -1){
//                emit(Result.Error(errorMsg))
//            } else {
//                emit(Result.Success(body))
//            }
//        }
//    }
//    .flowOn(Dispatchers.IO)
//    .catch {
//        emit(Result.Error("Network Error"))
//    }

}