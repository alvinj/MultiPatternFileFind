# Multi-Pattern FileFind (mff)

This is a multi-pattern file-find utility written in Scala.

## Usage

Run `mff` by itself to see the usage information:

````
$ mff

Usage: mff [options]

  -d, --dir [dirName]      required; the directory to search
  -f, --filename-pattern [filenamePattern]
                           required; the filenames to search, like '*.java'
  --p1 [searchPattern]     required; strings or patterns to search for; regexes must match the full line
  --p2 [searchPattern]     required; regex patterns are like 'StringBuilder' or '^void.*main.*'
  --p3 [searchPattern]     optional
  -b, --before [before]    the number of lines BEFORE the search pattern to print, like 1 or 2
  -a, --after [after]      the number of lines AFTER the search pattern to print, like 1 or 2
  -o, --or                 use ‘or’ approach to match *any* pattern instead of *all* patterns
````

Here’s an example command:

````
mff -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 ListTile -b 1 -a 2
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

Here’s a command using the “or” option, which means, “Match *any* pattern, not *all* patterns (which is the default)”:

````
mff -d /Users/al/Projects/Flutter -f "*.dart" --p1 ListView --p2 Dismissible -o
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




## Building the app (TODO: UPDATE THIS SECTION)

I initially build the app with [sbt-assembly](https://github.com/sbt/sbt-assembly), then create an executable with GraalVM. The steps are:

- Run `sbt assembly`, or run `assembly` from the SBT prompt
- That creates a JAR file named *target/scala-2.12/FileFind-assembly-0.1.jar*
- `cd` into the *Graal* directory
- Source the first file, i.e., `. 1setup_graal` (you’ll need to change that configuration for your system)
- Then run `2compile_graal.sh` to create the `ff` executable with GraalVM

After that, copy the `ff` executable to your *~/bin* directory, or somewhere similar.



## More information (TODO: UPDATE THIS SECTION)

For more information, see my [Scala “file find” command blog post](https://alvinalexander.com/scala/scala-file-find-utility-command).

Alvin Alexander  
https://alvinalexander.com

