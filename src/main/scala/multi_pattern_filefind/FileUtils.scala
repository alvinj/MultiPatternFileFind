package multi_pattern_filefind

import java.io.{BufferedReader, FileNotFoundException, IOException}
import java.nio.file.{Files, Paths}

object FileUtils {

    /**
      * This method no longer throws exceptions.
      */
    def readFileToSeq(canonFilename: String): Seq[String] = {
        var br: BufferedReader = null
        val emptySeq = Seq[String]()
        try {
            br = Files.newBufferedReader(Paths.get(canonFilename))
            val lines = br.lines.toArray().map(o => o.toString)
            lines
        } catch {
            case e: FileNotFoundException =>
                System.err.println(s"Couldn’t find the file '$canonFilename'.")
                emptySeq
            case e: IOException => 
                System.err.println(s"Got an IOException trying to read '$canonFilename'")
                emptySeq
            case t: Throwable =>
                // failing on _main.dart files throws a java.io.UncheckedIOException
                System.err.println(s"Throwable: Filename: '$canonFilename', \nMessage: ${t.getMessage()}")
                emptySeq
        } finally {
            if (br != null) br.close
        }
    }

}