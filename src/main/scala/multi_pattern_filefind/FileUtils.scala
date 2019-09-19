package filefind

import StringUtils._
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class LineRange(firstLine: Int, lastLine: Int)

object FileUtils {

    /**
     * Only call this method when you know the `filename` contains matches.
     */
    def printMatchingLines(
        filename: String, 
        lineNumsWherePatternFound: Seq[Int],
        searchPatterns: Seq[String],
        linesBefore: Int,
        linesAfter: Int
    ): Unit = {

        printFilename(filename)

        //val allLinesToPrint = lineNumsWherePatternFound.toSet
        val allLinesToPrint = createListOfLinesToPrint(
            lineNumsWherePatternFound, linesBefore, linesAfter
        )

        var inARange = false
        var lineNum = 0
        val bufferedSource = Source.fromFile(filename)
        for (line <- bufferedSource.getLines) {
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
        bufferedSource.close
    }

    private def printFilename(filename: String): Unit = {
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

    /**
     * Find all of the line numbers in the file that match the pattern.
     */
    def findMatchingLineNumbers(filename: String, patterns: Seq[String]): Seq[Int] = {
        val matchingLineNumbers = ArrayBuffer[Int]()
        var lineNum = 0
        val bufferedSource = Source.fromFile(filename)
        for (line <- bufferedSource.getLines) {
            lineNum += 1
            if (StringUtils.lineContainsStringOrPattern(line, patterns)) matchingLineNumbers += lineNum
        }
        bufferedSource.close
        matchingLineNumbers.toSeq
    }


}