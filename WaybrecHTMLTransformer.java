package wayic.Web.imager;

import Breccia.Web.imager.FileTransformer;
import java.nio.file.Path;
import wayic.Waybrec.parser.WaybrecReader;
import wayic.Waybrec.parser.WaybrecXTranslator;


public final class WaybrecHTMLTransformer implements FileTransformer {


    /** @param plainTransformer The transformer to use for non-waycast files.
      */
    public WaybrecHTMLTransformer( final FileTransformer plainTransformer ) {
        this.plainTransformer = plainTransformer; }



   // ━━━  F i l e   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void transform( Path sourceFile ) {
        if( !isWaycastFile( sourceFile )) {
            plainTransformer.transform( sourceFile );
            return; }
        try( final WaybrecReader source = new WaybrecReader( sourceFile );
             final WaybrecXTranslator in = new WaybrecXTranslator( source ); ) {
            for( ;; ) {
                // TODO, the actual transform.
                if( in.hasNext() ) in.next();
                else break; }}}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Answers whether `f` is contained in a waycast.
      */
    private static boolean isWaycastFile( final Path f ) { return false; } // Yet unimplemented.



    private final FileTransformer plainTransformer; }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
