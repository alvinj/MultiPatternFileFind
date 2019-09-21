package multi_pattern_filefind

import FileUtils._

object FinderHelper {

    def printSummaryLine(
        filePattern: String,
        numFilenameMatches: Int,
        numPatternMatches: Int
    ) = {
        println(s"Searched $numFilenameMatches $filePattern files, found $numPatternMatches matching files.\n")
    }

    def printMatchingLinesForFile(
        filename: String,
        lines: Seq[String],
        searchPatterns: Seq[String],
        linesBefore: Int,
        linesAfter: Int
    ): Unit = {
        val matchingLineNumbers = findMatchingLineNumbers(
            lines,
            searchPatterns
        )
        if (findMatchingLineNumbers(lines, searchPatterns).size > 0) {
            printMatchingLines(
                filename, lines, matchingLineNumbers, searchPatterns, linesBefore, linesAfter
            )
        }
    }


}