package filefind

import util.control.Breaks._

object StringUtils {

    /**
     * Highlights the search pattern within the string `line`.
     * It currently makes the search pattern bold and underlined.
     * The output string is intended to be shown in an ANSI terminal,
     * and I can confirm that it works with the MacOS Terminal.
     * 
     * More info on escape sequences: 
     * stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
     */
    def highlightSearchPatternForAnsiTerminals(line: String, searchPatterns: Seq[String]): String = {
        val matchingPattern = getPatternThatMatchesLine(line, searchPatterns)
        line.replaceAll(
            matchingPattern,
            s"\033[4m${matchingPattern}\033[0m"
        )
    }

    /**
     * Only use this method if you’re sure that one of the `patterns` matches
     * the `line`.
     */
    def getPatternThatMatchesLine(line: String, patterns: Seq[String]): String = {
        for (p <- patterns) {
            if (line.contains(p)) return p
            else if (line.matches(p)) return p
        }
        ""
    }

    def stringContainsAnyPattern(line: String, patterns: Seq[String]): Boolean = {
        for (p <- patterns) {
            if (line.contains(p)) return true
            else if (line.matches(p)) return true
        }
        false
    }

    /**
     * Returns true if `s` contains *all* of the patterns.
     */
    def stringContainsAllPatterns(s: String, patterns: Seq[String]): Boolean = {
        var foundAllPatternsSoFar = false
        for (p <- patterns) {
            breakable {
                if (s.contains(p) || s.matches(p)) {
                    foundAllPatternsSoFar = true
                    break //go on to the next pattern
                }
                else {
                    return false
                }
            }
        }
        foundAllPatternsSoFar
    }

    /**
     * Create an “underline” string that’s the same length as the input string.
     * {{{
     *     val s = "foobar"
     *     val u = makeUnderline(s)
     *     println(u)  // "------"
     * }}}
     */
    def makeUnderline(s: String) = List.fill(s.length)('-').mkString
    //def dash(s: String) = "-"*s.length  //via twitter

}