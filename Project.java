package wayic.Web.imager;

import java.nio.file.Path;
import java.util.logging.Logger;

import static java.lang.System.getProperty;


/** The present project.
  */
public final class Project {


    private Project() {}



    /** The output directory of the present project.
      */
    public static final Path outputDirectory = Path.of( getProperty("java.io.tmpdir"),
      "wayic.Web.imager_" + getProperty("user.name") );



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** The logger proper to the present project.
      */
    static final Logger logger = Logger.getLogger( "wayic.Web.imager" ); }



                                            // Copyright © 2020, 2022, 2024  Michael Allan.  Licence MIT.
