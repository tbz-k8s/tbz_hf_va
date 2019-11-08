package ch.nliechti.util

import kotlin.streams.asSequence

object DeploymentUtil {
    private const val randomStringSource = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    fun getRandomValueForEnv(length: Long = 20): String {
        return java.util.Random().ints(length, 0, randomStringSource.length)
                .asSequence()
                .map(randomStringSource::get)
                .joinToString("")
    }
}