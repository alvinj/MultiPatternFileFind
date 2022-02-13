package multi_pattern_filefind

import java.io._
import java.nio.file._
import java.nio.file.attribute._
import java.nio.file.FileVisitResult._
import java.nio.file.FileVisitOption._
import scala.io.Source
import StringUtils._
import FileUtils._
import FinderHelper._

class Finder (
    filePattern: String, 
    searchPatterns: Seq[String],
    before: Int,
    after: Int,
    matchAnyPattern: Boolean,
    ignoreCase: Boolean
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

            // the current filename matches the user’s filename pattern
            numFilenameMatches += 1
            val canonFilename = file.toAbsolutePath.toString

            // now search the current file for the patterns
            val fileContents: Seq[String] = FileUtils.readFileToSeq(canonFilename)
            if (matchAnyPattern) {
                // match *any* pattern (the -o option)
                if (seqOfStringContainsAnyPattern(fileContents, searchPatterns, ignoreCase)) {
                    numPatternMatches += 1
                    printMatchingLinesForFile(canonFilename, fileContents, searchPatterns, before, after, ignoreCase)
                }
            } else {
                // match *all* patterns (the -o option)
                if (seqOfStringContainsAllPatterns(fileContents, searchPatterns, ignoreCase)) {
                    numPatternMatches += 1
                    printMatchingLinesForFile(canonFilename, fileContents, searchPatterns, before, after, ignoreCase)
                }
            }
        }
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
    
    def printSummaryLine() = FinderHelper.printSummaryLine(
        filePattern,
        numFilenameMatches,
        numPatternMatches
    )

}
