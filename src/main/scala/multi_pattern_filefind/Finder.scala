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
    after: Int,
    matchAnyPattern: Boolean
)
extends SimpleFileVisitor[Path] {

    var pathMatcher: PathMatcher = null
    var numFilenameMatches = 0
    var numPatternMatches = 0

    // note: "glob:" is part of the syntax
    // https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileSystem.html#getPathMatcher-java.lang.String-
    pathMatcher = FileSystems.getDefault()
        .getPathMatcher("glob:" + filePattern)

    // compares the glob filePattern against the file or directory name
    def find(file: Path): Unit = {
        val filename: Path = file.getFileName()
        if (filename != null && pathMatcher.matches(filename)) {
            numFilenameMatches += 1
            val canonFilename = file.toAbsolutePath.toString

            // search the file for the patterns
            val fileContents = FileUtils.readFileToSeq(canonFilename)
            if (matchAnyPattern) {
                // the optional use case -- the file can contain any pattern
                printTheResults(canonFilename, fileContents, searchPatterns, before, after)
            } else {
                // the main use case -- the file must contain all patterns
                if (StringUtils.stringContainsAllPatterns(fileContents, searchPatterns)) {
                    printTheResults(canonFilename, fileContents, searchPatterns, before, after)
                }
            }
        }
    }

    private def printTheResults(
        _filename: String,
        _lines: Seq[String],
        _searchPatterns: Seq[String],
        _before: Int,
        _after: Int
    ) = {
        val matchingLineNumbers = findMatchingLineNumbers(
            _lines,
            _searchPatterns
        )
        if (findMatchingLineNumbers(_lines, _searchPatterns).size > 0) {
            numPatternMatches += 1
            printMatchingLines(
                _filename, _lines, matchingLineNumbers, _searchPatterns, _before, _after
            )
        }
    }

    def done() = {
        println(s"Searched $numFilenameMatches $filePattern files, found $numPatternMatches matching files.\n")
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
