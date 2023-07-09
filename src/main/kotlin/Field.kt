class Field(val name: String) {

    companion object {
        // Dummy name for the root of the JSON tree
        val ROOT_NAME = "_root"
    }

    val types: MutableMap<FieldType, Int> = mutableMapOf()

    constructor(name: String, type: FieldType) : this(name) {
        types[type] = 1
    }

    fun addOccurrence(type: FieldType) {
        if (!types.containsKey(type)) {
            types[type] = 0
        }
        types[type] = types[type]!! + 1
    }

    fun totalOccurrences(): Int {
        return types.values.sum()
    }
}
