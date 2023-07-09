import java.io.BufferedReader
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class FlatJTest {

    private val TEST_FILES_DIR = "src/test/resources/"

    private val testData = mapOf(
        "test_data_01.json" to """+------------------------------------------+
| keys          | types      | occurrences |
|---------------|------------|-------------|
| _id           | String(7)  |           7 |
| about         | String(7)  |           7 |
| address       | String(7)  |           7 |
| age           | Number(7)  |           7 |
| balance       | String(7)  |           7 |
| company       | String(7)  |           7 |
| email         | String(7)  |           7 |
| eyeColor      | String(7)  |           7 |
| favoriteFruit | String(7)  |           7 |
| friends       | Array(7)   |           7 |
| gender        | String(7)  |           7 |
| greeting      | String(7)  |           7 |
| guid          | String(7)  |           7 |
| index         | Number(7)  |           7 |
| isActive      | Boolean(7) |           7 |
| latitude      | Number(7)  |           7 |
| longitude     | Number(7)  |           7 |
| name          | String(7)  |           7 |
| phone         | String(7)  |           7 |
| picture       | String(7)  |           7 |
| registered    | String(7)  |           7 |
| tags          | Array(7)   |           7 |
+------------------------------------------+
""",
        "test_data_02.json" to """+----------------------------------------------------+
| keys          | types                | occurrences |
|---------------|----------------------|-------------|
| _id           | String(7)            |           7 |
| about         | String(6)            |           6 |
| address       | String(6)            |           6 |
| age           | Number(7)            |           7 |
| balance       | String(7)            |           7 |
| company       | String(6), Object(1) |           7 |
| company.name  | String(1)            |           1 |
| email         | String(6)            |           6 |
| eyeColor      | String(7)            |           7 |
| favoriteFruit | String(7)            |           7 |
| friends       | Array(7)             |           7 |
| gender        | String(7)            |           7 |
| greeting      | String(7)            |           7 |
| guid          | String(6)            |           6 |
| index         | Number(6)            |           6 |
| isActive      | Boolean(6)           |           6 |
| latitude      | String(2), Number(5) |           7 |
| longitude     | String(2), Number(5) |           7 |
| name          | String(7)            |           7 |
| phone         | String(7)            |           7 |
| picture       | String(7)            |           7 |
| registered    | String(7)            |           7 |
| tags          | Array(7)             |           7 |
+----------------------------------------------------+
"""
    )

    private fun testJSONInput(inputFile: String, expectedOutput: String) {
        val bufferedReader: BufferedReader = File(inputFile).bufferedReader()
        val jsonString = bufferedReader.use { it.readText() }
        bufferedReader.close()
        val fieldsList = FieldList(jsonString)
        assertEquals(fieldsList.formatTableOutput(), expectedOutput)
    }

    @Test
    fun testInputs() {
        testData.forEach { testJSONInput(TEST_FILES_DIR + it.key, it.value) }
    }
}
