import org.json.JSONArray
import org.json.JSONObject

class FieldList(val jsonString: String) {

    private lateinit var fields: List<Field>

    companion object {
        /**
         * Attempts to discover the FieldType of [node]. [node] can be of any type, as
         * JSON format supports different field types, ranging from internal package defined structures
         * to Int and BigDecimal.
         */
        fun discoverFieldType(node: Any): FieldType {
            return when (node) {
                is Int -> FieldType.NUMBER
                // Some numbers are represented as BigDecimal in JSON-Java lib
                is java.math.BigDecimal -> FieldType.NUMBER
                is String -> FieldType.STRING
                is Boolean -> FieldType.BOOLEAN
                is JSONArray -> FieldType.ARRAY
                // JSONObject.NULL is a singleton, so no equals() check is required
                JSONObject.NULL -> FieldType.NULL
                is JSONObject -> FieldType.OBJECT
                else -> FieldType.UNKNOWN
            }
        }
    }

    init {
        try {
            createFieldList()
        }
        catch (e: org.json.JSONException) {
            // Returning a native JSONException of this package instead
            throw exceptions.JSONException(e.message)
        }
    }

    /**
     * Helper method that receives a [node] of type JSONObject and its [parent], represented as a Field,
     * and the [fieldsMap] that holds all field occurrences until this point.
     * The method discovers new fields or updates the metadata of the existing ones.
     */
    private fun dfs(node: JSONObject, parent: Field, fieldsMap: MutableMap<String, Field>) {
        // For each key (child) of the current node
        node.keys().forEach { childKey ->
            // Get the child node, its name, and its type
            val childNode = node.get(childKey)
            val childName = (if (parent.name != Field.ROOT_NAME) (parent.name + ".") else "") + childKey
            val childField = if (fieldsMap.contains(childName)) fieldsMap[childName]!! else Field(childName)
            val childType = discoverFieldType(childNode)
            childField.addOccurrence(childType)
            if (childType == FieldType.OBJECT) {
                // In case that the child is a JSON object, apply recursively the method to it
                dfs(childNode as JSONObject, childField, fieldsMap)
            } else if (!fieldsMap.contains(childName)) {
                // In case that the child is not a JSON object and is its first occurrence, store it as a field
                fieldsMap[childName] = childField
            }
        }
    }

    /**
     * The starting point of the fields list creation.
     * [createFieldList] makes the starting call(s) to the [dfs] method.
     */
    private fun createFieldList() {
        val fieldsMap: MutableMap<String, Field> = mutableMapOf()
        if (jsonString.startsWith("[")) {
            // In case that the JSON string is a JSON array, e.g. [ { ... }, { ... }, ... ]
            val jsonArray = JSONArray(jsonString)
            for (i in (0..<jsonArray.length())) {
                val arrayItem = jsonArray.get(i)
                if (discoverFieldType(arrayItem) == FieldType.OBJECT) {
                    val jsonField = Field(Field.ROOT_NAME, FieldType.OBJECT)
                    dfs(arrayItem as JSONObject, jsonField, fieldsMap)
                }
            }
        } else {
            // In any other case
            val root = JSONObject(jsonString)
            val rootField = Field(Field.ROOT_NAME, FieldType.OBJECT)
            dfs(root, rootField, fieldsMap)
        }
        fields = fieldsMap.values.toList()
    }

    /**
     * Formats in a table form the discovered fields of the current FieldList
     * by using the [Utils.formatTableOutput] method.
     * The `keys` and `types` columns are aligned to the left while the `occurrences`column is aligned to the right.
     */
    fun formatTableOutput(): String {
        val printListData: MutableList<List<Utils.PrintItem>> = mutableListOf()
        fields.forEach {
            printListData.add(listOf(
                Utils.PrintItem(it.name, Utils.TextAlign.L),
                Utils.PrintItem(it.types.map { t -> "${t.key.typeName}(${t.value})" }.joinToString(", "), Utils.TextAlign.L),
                Utils.PrintItem(it.totalOccurrences().toString(), Utils.TextAlign.R)
            ))
        }
        printListData.sortBy { it[0].content }
        printListData.add(0, listOf(
            Utils.PrintItem("keys", Utils.TextAlign.L),
            Utils.PrintItem("types", Utils.TextAlign.L),
            Utils.PrintItem("occurrences", Utils.TextAlign.R)
        ))
        return Utils.formatTableOutput(printListData)
    }
}
