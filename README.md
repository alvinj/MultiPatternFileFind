# Multi-Pattern FileFind (ffx)

This is a multi-pattern file-find utility written in Scala.

## Usage

Run `ffx` by itself to see the usage information:

````
$ ffx

Usage: ffx [options]

  -d, --d [dirName]        required; the directory to search
  -f, --f [filenamePattern]
                           required; the filenames to search, like '*.java'
  --p1 [searchPattern]     required; strings or patterns to search for; regexes must match the full line
  --p2 [searchPattern]     required; regex patterns are like 'StringBuilder' or '^void.*main.*'
  --p3 [searchPattern]     optional
  --p4 [searchPattern]     optional
  -b, --before [before]    the number of lines BEFORE the search pattern to print, like 1 or 2
  -a, --after [after]      the number of lines AFTER the search pattern to print, like 1 or 2
  -o, --or                 use ‘or’ approach to match *any* pattern instead of *all* patterns
  -i, --i                  ignore case when doing the matching (probably doesn’t work with regex patterns atm)

Usage Examples:
---------------

Search for two patterns:
    ffx -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 ListTile

The same, but also print one line before and one line after each match:
    ffx -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 ListTile -b 1 -a 2

Ignore case when searching:
    ffx -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 listtile -i

Use the “or” option, searching for files containing either pattern:
    ffx -d /Users/al/Projects/Scala -f "*.scala" --p1 ArrayBuffer --p2 ArrayBuilder -o

Search for three patterns:
    ffx -d ~/Scala -f "*.scala" --p1 foo --p2 bar --p3 baz
````


### Example 1

Here’s an example command:

````
ffx -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 ListTile -b 1 -a 2
````

It means:

- Search the directory named */Users/al/Projects/Flutter*
- Search for files ending with the *.dart* extension
- Look for the strings/patterns `ListView` and `ListTile`
- Print 1 line *before* each pattern is found
- Print 2 lines *after* each pattern is found

Sample output looks like this:

````
/Users/al/Projects/Flutter/PracticalFlutterBook/ch_05+06/flutter_book/lib/notes/NotesList.dart
----------------------------------------------------------------------------------------------
  36:             ),
  37:             body : ListView.builder(
  38:               itemCount : notesModel.entityList.length,
  39:               itemBuilder : (BuildContext inBuildContext, int inIndex) {

  66:                       color : color,
  67:                       child : ListTile(
  68:                         title : Text("${note.title}"),
  69:                         subtitle : Text("${note.content}"),

  81:               } /* End itemBuilder. */
  82:             ) /* End End ListView.builder. */
  83:           ); /* End Scaffold. */
  84:         } /* End ScopedModelDescendant builder. */
````

When that output is shown in a terminal window, all occurrences of `ListView` and `ListTile` are underlined.

### Example 2

Here’s a command using the “or” option, which means, “Match *any* pattern, not *all* patterns (which is the default)”:

````
ffx -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 Dismissible -o
````

Its “or” output looks like this, matching all files that contain `ListView` or `Dismissible`:

````
/Users/al/Projects/Flutter/PracticalFlutterBook/ch_05+06/flutter_book/lib/notes/NotesEntry.dart
-----------------------------------------------------------------------------------------------
  79:               child : ListView(

/Users/al/Projects/Flutter/PracticalFlutterBook/ch_05+06/flutter_book/lib/notes/NotesList.dart
----------------------------------------------------------------------------------------------
 102:       barrierDismissible : false,
````


## Building the app

I initially build the app with [sbt-assembly](https://github.com/sbt/sbt-assembly), then create an executable with GraalVM. The steps are:

- Run `sbt assembly`, or run `assembly` from the SBT prompt
- That creates a JAR file named *target/scala-2.12/MultiPatternFileFind-assembly-0.2.jar*
- `cd` into the *Graal* directory
- Source the first file, i.e., `. 1setup_graal` (you’ll need to change that configuration for your system)
- Then run `2compile_graal.sh` to create the `ffx` executable with GraalVM

After that, copy the `ffx` executable to your *~/bin* directory, or somewhere similar.

See the TODO note below, that `native-image` does *not* create a complete standalone image.


## Version history

- 0.1, the initial release
- 0.2, fixed an issue where FileUtils::readFileToSeq was throwing exceptions


## TO-DO

- Feb. 12, 2022: GraalVM native-image: Warning: Image 'ffx' is a fallback image that requires a JDK for 
  execution (use --no-fallback to suppress fallback image generation and to print more detailed information 
  why a fallback image was necessary).
- I need to test REGEX expressions; I haven’t put much work into them.


Alvin Alexander  
https://alvinalexander.com

