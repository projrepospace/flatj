import java.io.BufferedReader
import java.io.File

object App {
    const val appName = "flatj"
    const val version = "0.0.1"
}

fun printUsage() {
    val usage = listOf(
        "Usage: flatj options_list",
        "Options:",
        "    --inputString, -s JSON_string -> Analyze a JSON string provided in the command line",
        "    --inputFile, -f file_path -> Analyze a JSON string from a file",
        "    --version, -v -> Print version",
        "    --help, -h -> Usage info)"
    )
    println(usage.joinToString("\n"))
}

fun printVersion() {
    println("${App.appName} v.${App.version}")
}

fun parseJSONString(jsonString: String) {
    try {
        val fieldsList = FieldList(jsonString)
        print(fieldsList.formatTableOutput())
    } catch (e: Exception) {
        println("[-] parse error: ${e.message}")
        kotlin.system.exitProcess(1)
    }
}

fun parseInputFile(inputFile: String) {
    var jsonString = ""
    try {
        val bufferedReader: BufferedReader = File(inputFile).bufferedReader()
        jsonString = bufferedReader.use { it.readText() }
        bufferedReader.close()
    }
    catch (e: Exception) {
        println("[-] file input error: ${e.message}")
        kotlin.system.exitProcess(1)
    }
    parseJSONString(jsonString)
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printUsage()
    } else {
        if (listOf("-s", "--inputString").contains(args[0])) {
            if (args.size > 1) {
                parseJSONString(args[1])
            } else {
                println("--inputString, -s option requires a parameter")
            }
        } else if (listOf("-f", "--inputFile").contains(args[0])) {
            if (args.size > 1) {
                parseInputFile(args[1])
            } else {
                println("--inputFile, -f option requires a parameter")
            }
        } else if (listOf("-v", "--version").contains(args[0])) {
            printVersion()
        } else if (listOf("-h", "--help").contains(args[0])) {
            printUsage()
        } else {
            println("Invalid argument: ${args[0]}")
        }
    }
}
