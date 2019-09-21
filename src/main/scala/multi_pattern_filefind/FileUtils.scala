package multi_pattern_filefind

import scala.io.Source

object FileUtils {

    def readFileToSeq(canonFilename: String): Seq[String] = {
        val bufferedSource = Source.fromFile(canonFilename)
        val fileContents = bufferedSource.getLines.toList
        bufferedSource.close
        fileContents
    }

}