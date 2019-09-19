package filefind

import java.io._
import java.nio.file._
import java.nio.file.attribute._
import java.nio.file.FileVisitResult._
import java.nio.file.FileVisitOption._
import scala.io.Source
import StringUtils._
import FileUtils._

class Finder (
    filePattern: String, 
    searchPatterns: Seq[String],
    before: Int,
    after: Int
)
extends SimpleFileVisitor[Path] {

    var pathMatcher: PathMatcher = null
    var numMatches = 0

    // note: "glob:" is part of the syntax
    // https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileSystem.html#getPathMatcher-java.lang.String-
    pathMatcher = FileSystems.getDefault()
        .getPathMatcher("glob:" + filePattern)

    // compares the glob filePattern against the file or directory name
    def find(file: Path): Unit = {
        val filename: Path = file.getFileName()
        if (filename != null && pathMatcher.matches(filename)) {
            numMatches += 1
            val canonFilename = file.toAbsolutePath.toString

            // search the file for the patterns
            val fileContents = FileUtils.fileToString(canonFilename)
            if (StringUtils.stringContainsAllPatterns(fileContents, searchPatterns)) {
                // main use case -- the file must contain all patterns
                val matchingLineNumbers = findMatchingLineNumbers(
                    canonFilename, 
                    searchPatterns
                )
                if (findMatchingLineNumbers(canonFilename, searchPatterns).size > 0) {
                    printMatchingLines(
                        canonFilename, matchingLineNumbers, searchPatterns, before, after
                    )
                }
            }

        }
    }

    def done() = {
        println(s"Searched $numMatches $filePattern files.\n")
    }
    
    // invoke the filePattern matching method on each file
    override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        find(file)
        return CONTINUE
    }

    // invoke the filePattern matching method on each directory.
    // (ff: do nothing for directories)
    override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
        return CONTINUE
    }

    override def visitFileFailed(file: Path, ioe: IOException): FileVisitResult = {
        System.err.println(ioe)
        return CONTINUE
    }
    

}
