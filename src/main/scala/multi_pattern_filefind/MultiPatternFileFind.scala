package filefind

import java.io._
import java.nio.file._
import java.nio.file.attribute._
import java.util._
import java.nio.file.FileVisitResult._
import java.nio.file.FileVisitOption._
import scopt.OParser
import scala.collection.mutable.ArrayBuffer

object MultiPatternFileFind extends App {

    // use the “scopt” library to get the command-line options
    case class Config(
        searchDir: String = "",         //directory to search
        searchPattern1: String = "",    //1st pattern to look for inside files
        searchPattern2: String = "",    //2nd pattern to look for inside files
        searchPattern3: String = "",    //3rd pattern to look for inside files
        filenamePattern: String = "",   //filename pattern to search for
        before: Int = 0,                //lines to print before each match
        after: Int = 0                  //lines to print after each match
    )

    val builder = OParser.builder[Config]
    val parser1: OParser[Unit,Config] = {
      import builder._
      OParser.sequence(
        programName("mff"),
        head("mff", "0.1"),
        opt[String]('d', "dir")
            .required()
            .action((x, c) => c.copy(searchDir = x))
            .text("required; the directory to search"),
        opt[String]('f', "filename-pattern")
            .required()
            .valueName("[filenamePattern] (like '*.java')")
            .action((x, c) => c.copy(filenamePattern = x))
            .text("required; the filenames to search"),
        opt[String]('a', "p1")  //required
            .required()
            .valueName("[searchPattern] (like 'StringBuilder' or '^void.*main.*')")
            .action((x, c) => c.copy(searchPattern1 = x))
            .text("required; regex patterns must match the full line"),
        opt[String]('b', "p2")  //required
            .required()
            .valueName("[searchPattern] (like 'StringBuilder' or '^void.*main.*')")
            .action((x, c) => c.copy(searchPattern2 = x))
            .text("required; regex patterns must match the full line"),
        opt[String]('c', "p3")
            .valueName("[searchPattern] (like 'StringBuilder' or '^void.*main.*')")
            .action((x, c) => c.copy(searchPattern3 = x))
            .text("regex patterns must match the full line"),
        opt[Int]('b', "before")
            .valueName("[before] (the number of lines BEFORE the search pattern to print, like 1 or 2)")
            .action((x, c) => c.copy(before = x)),
        opt[Int]('a', "after")
            .valueName("[after]   (the number of lines AFTER the search pattern to print, like 1 or 2)")
            .action((x, c) => c.copy(after = x))
      )
    }

    OParser.parse(parser1, args, Config()) match {
        case Some(config) =>
            doTheSearch(config)
        case _ =>
            // arguments are bad, error message will have been displayed
            println("")
    }

    def doTheSearch(config: Config) = {

        val searchPatterns = ArrayBuffer[String](
            config.searchPattern1,
            config.searchPattern2
        )
        if (config.searchPattern3.trim != "") searchPatterns += config.searchPattern3

        val finder = new Finder(
            config.filenamePattern, 
            searchPatterns,
            config.before,
            config.after
        )
        Files.walkFileTree(
            Paths.get(config.searchDir), 
            finder
        )
        finder.done()    
    }


}





