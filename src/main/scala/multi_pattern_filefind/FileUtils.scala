package multi_pattern_filefind

import scala.io.Source

case class LineRange(firstLine: Int, lastLine: Int)

object FileUtils {

    def readFileToString(canonFilename: String): String = {
        val bufferedSource = Source.fromFile(canonFilename)
        val fileContents = bufferedSource.getLines.mkString
        bufferedSource.close
        fileContents
    }

    def readFileToSeq(canonFilename: String): Seq[String] = {
        val bufferedSource = Source.fromFile(canonFilename)
        val fileContents = bufferedSource.getLines.toList
        bufferedSource.close
        fileContents
    }

}