# Multi-Pattern FileFind (mff)

This is a multi-pattern file-find utility written in Scala.

## Usage

Run `mff` by itself to see the usage information:

````
$ mff

Usage: mff [options]

  -d, --dir <value>        required; the directory to search
  -f, --filename-pattern [filenamePattern] (like '*.java')
                           required; the filenames to search
  -a, --p1 [searchPattern] (like 'StringBuilder' or '^void.*main.*')
                           required; regex patterns must match the full line
  -b, --p2 [searchPattern] (like 'StringBuilder' or '^void.*main.*')
                           required; regex patterns must match the full line
  -c, --p3 [searchPattern] (like 'StringBuilder' or '^void.*main.*')
                           regex patterns must match the full line
  -b, --before [before] (the number of lines BEFORE the search pattern to print, like 1 or 2)
  -a, --after [after]   (the number of lines AFTER the search pattern to print, like 1 or 2)
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
TODO
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

