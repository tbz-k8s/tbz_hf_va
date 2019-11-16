package ch.nliechti.service

import ch.nliechti.SchoolClass
import ch.nliechti.Trainee
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object TraineeService {
    fun readTrainees(inputStream: InputStream): SchoolClass {
        val trainees = mutableListOf<Trainee>()
        var className = ""
        var isFirstLine = true
        val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream, Charsets.ISO_8859_1))
        reader.forEachLine() { line ->
            if (isFirstLine) {
                className = extractClassName(line)
                isFirstLine = false
            }
            val splitted = line.split(";")
            if (splitted.size == 18) {
                trainees.add(Trainee(splitted[0] + splitted[1], splitted[16]))
            }
        }
        return SchoolClass(className, trainees)
    }

    private fun extractClassName(line: String): String {
        var name: String = line
        name = name.replace("\"", "")
        name = name.replace(" ", "")
        name = splitClassName(name)
        return name
    }

    private fun splitClassName(name: String): String {
        val split = name.split(":", ",")
        return "${split[1]}_${split[3]}_${split[5]}"
    }
}