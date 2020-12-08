package wayic.Web.imager;

import Breccia.Web.imager.*;
import java.io.IOException;
import java.nio.file.Path;
import javax.xml.stream.XMLStreamWriter;


public class WaybreccianFileTransform implements FileTransform {


    /** @param f The path of the Waybreccian source file to transform.
      */
    public WaybreccianFileTransform( final Path f ) { reader = new WaybrecReader( f ); }



   // ━━━  A u t o   C l o s e a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public void close() throws IOException {}



   // ━━━  F i l e   T r a n s f o r m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public BrecciaReader input() { return reader; }



    public XMLStreamWriter output() { return null; }



    public MarkupTransformer transformer() { return transformer; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final WaybrecReader reader;



    private final MarkupTransformer transformer = new WaybrecToHTML(); }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
