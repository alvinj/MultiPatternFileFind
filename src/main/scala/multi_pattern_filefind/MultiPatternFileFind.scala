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
        searchPattern4: String = "",    //4th pattern to look for inside files
        filenamePattern: String = "",   //filename pattern to search for
        before: Int = 0,                //lines to print before each match
        after: Int = 0,                 //lines to print after each match
        orBehavior: Boolean = false     //search using "or" rather than the default "and", i.e.,
                                        //this says, "match any pattern" as opposed to the default
                                        //"match all patterns"
    )

    val builder = OParser.builder[Config]
    val parser1: OParser[Unit,Config] = {
      import builder._
      OParser.sequence(
        programName("ffx"),
        head("ffx", "0.1"),
        opt[String]('d', "d")
            .required()
            .valueName("[dirName]")
            .action((x, c) => c.copy(searchDir = x))
            .text("required; the directory to search"),
        opt[String]('f', "f")
            .required()
            .valueName("[filenamePattern]")
            .action((x, c) => c.copy(filenamePattern = x))
            .text("required; the filenames to search, like '*.java'"),
        opt[String]("p1")  //required
            .required()
            .valueName("[searchPattern]")
            .action((x, c) => c.copy(searchPattern1 = x))
            .text("required; strings or patterns to search for; regexes must match the full line"),
        opt[String]("p2")  //required
            .required()
            .valueName("[searchPattern]")
            .action((x, c) => c.copy(searchPattern2 = x))
            .text("required; regex patterns are like 'StringBuilder' or '^void.*main.*'"),
        opt[String]("p3")
            .valueName("[searchPattern]")
            .action((x, c) => c.copy(searchPattern3 = x))
            .text("optional"),
        opt[String]("p4")
            .valueName("[searchPattern]")
            .action((x, c) => c.copy(searchPattern4 = x))
            .text("optional"),
        opt[Int]('b', "before")
            .valueName("[before]")
            .text("the number of lines BEFORE the search pattern to print, like 1 or 2")
            .action((x, c) => c.copy(before = x)),
        opt[Int]('a', "after")
            .valueName("[after]")
            .text("the number of lines AFTER the search pattern to print, like 1 or 2")
            .action((x, c) => c.copy(after = x)),
        opt[Unit]('o', "or")
            .text("use ‘or’ approach to match *any* pattern instead of *all* patterns")
            .action((_, c) => c.copy(orBehavior = true))
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
        if (config.searchPattern4.trim != "") searchPatterns += config.searchPattern4

        val finder = new Finder(
            config.filenamePattern, 
            searchPatterns,
            config.before,
            config.after,
            config.orBehavior
        )
        Files.walkFileTree(
            Paths.get(config.searchDir), 
            finder
        )
        finder.done()    
    }


}





