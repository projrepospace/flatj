object Utils {

    enum class TextAlign { L, R }

    class PrintItem(val content: String, val align: TextAlign)

    /**
     * Helper method that generates a cell of the table that is being printed.
     * The [item] will be placed inside a table cell of at most [charNum] characters length.
     * The string content of the [item] has to have always at most [charNum] characters length.
     * An [extraChar] is being also being placed at the beginning and at the end of the cell.
     */
    private fun formatContentLR(item: PrintItem, charNum: Int, extraChar: Char = ' '): String {
        val len = item.content.length
        if (item.align == TextAlign.L) {
            return extraChar + item.content + extraChar.toString().repeat(charNum - len) + extraChar
        } else {
            return extraChar + extraChar.toString().repeat(charNum - len) + item.content + extraChar
        }
    };

    /**
     * Formats in a tabular format the 2D array [rows] that contains [PrintItem] objects.
     */
    fun formatTableOutput(rows: List<List<PrintItem>>): String {
        var output = ""

        if (rows.isEmpty())
            return output;

        // Find the maximum character length for each field
        val colCount = rows[0].size
        val maxFieldLen = (0..<colCount).map { i -> rows.map { row -> row[i].content.length }.max() }

        // Find the total character length of each row
        var totalRowLen = 3 * colCount - 1 + maxFieldLen.sum()

        // Create the first and the last line
        val firstLastLine = '+' + "-".repeat(totalRowLen) + "+"

        // Print the table
        output += firstLastLine + "\n"
        // Headers
        output += "|"
        for (j in (0..<colCount)) {
            output += formatContentLR(rows[0][j], maxFieldLen[j]) + "|"
        }
        output += "\n|"
        for (j in (0..<colCount)) {
            output += formatContentLR(PrintItem("-".repeat(maxFieldLen[j] - 1), TextAlign.L), maxFieldLen[j], '-') + "|"
        }
        output += "\n"
        // Body
        rows.drop(1).forEach { row ->
            output += "|"
            for (j in (0..<colCount)) {
                output += formatContentLR(row[j], maxFieldLen[j]) + "|"
            }
            output += "\n"
        }
        output += firstLastLine + "\n"
        return output
    }
}
