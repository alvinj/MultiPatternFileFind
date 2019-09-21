package multi_pattern_filefind

import StringUtils._
import FileUtils._
import scala.collection.mutable.ArrayBuffer

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

    /**
     * Find all of the line numbers in the Seq[String] that match the pattern.
     */
    def findMatchingLineNumbers(lines: Seq[String], patterns: Seq[String]): Seq[Int] = {
        val matchingLineNumbers = ArrayBuffer[Int]()
        var lineNum = 0
        for (line <- lines) {
            lineNum += 1
            if (StringUtils.stringContainsAnyPattern(line, patterns)) matchingLineNumbers += lineNum
        }
        matchingLineNumbers.toSeq
    }

    /**
     * Only call this method when you know the `filename` contains matches.
     */
    def printMatchingLines(
        filename: String,
        lines: Seq[String],
        lineNumsWherePatternFound: Seq[Int],
        searchPatterns: Seq[String],
        linesBefore: Int,
        linesAfter: Int
    ): Unit = {

        printFilenameHighlighted(filename)

        val allLinesToPrint = createListOfLinesToPrint(
            lineNumsWherePatternFound, linesBefore, linesAfter
        )

        var inARange = false
        var lineNum = 0
        for (line <- lines) {
            lineNum += 1
            if (inListOfLinesToPrint(lineNum, allLinesToPrint)) {
                inARange = true
                //println(f"${lineNum}%4d: $line")
                //TODO add highlighting back in?
                println(f"${lineNum}%4d: ${highlightSearchPatternForAnsiTerminals(line, searchPatterns)}")
                //println(f"${x}%4d:")
            }
            else if (inARange && !inListOfLinesToPrint(lineNum, allLinesToPrint)) {
                // you were in a range, and now you’re not
                inARange = false
                println("")
            }
        }
    }

    private def printFilenameHighlighted(filename: String): Unit = {
        val underline = makeUnderline(filename)
        println(s"\n${filename}\n${underline}")
    }

    /**
     * Create a list of all lines in the file that should be printed.
     */
    private def createListOfLinesToPrint(
        lineNumsWherePatternFound: Seq[Int],
        linesBefore: Int,
        linesAfter: Int
    ): Set[Int] = {
        val allLinesToPrint = ArrayBuffer[Int]()
        for (lineNum <- lineNumsWherePatternFound) {
            val firstLine = lineNum - linesBefore  //NOTE could be < 0
            val lastLine = lineNum + linesAfter    //NOTE could be > file_length
            if (firstLine == lastLine) {
                allLinesToPrint += firstLine
            } else {
                allLinesToPrint ++= Range(firstLine, lastLine+1)
            }
        }
        allLinesToPrint.toSet
    }

    private def inListOfLinesToPrint(lineNum: Int, list: Set[Int]) = list.contains(lineNum)

}