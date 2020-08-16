package duodev.take.eazy.utils

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T): NetworkResult<T>()
    data class Failure(val exception: String?): NetworkResult<Nothing>()
}