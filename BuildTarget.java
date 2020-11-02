package wayic.Web.imager;

// Changes to this file immediately affect the next build.  Treat it as a build script.


/** A build target of the present project.  Shell commands will accept abbreviated target names:
  * a target may be specified by any substring of its name that matches no other target name.
  */
public enum BuildTarget {


    /** A software builder compiled from source code into Java class files.
      * All other targets depend on this target and include it implicitly.
      */
    builder, // Mandatory, do not remove.


    /** Java class files compiled from source code.
      */
    Java_class_files; }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
