package wayic.Web.imager;

import Breccia.Web.imager.FileTransformer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import wayic.Waybrec.parser.WaybrecCursor;
import wayic.Waybrec.parser.WaybrecXCursor;

import static Breccia.parser.BrecciaXCursor.EMPTY;
import static Breccia.parser.BrecciaXCursor.START_DOCUMENT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Level.FINE;
import static wayic.Web.imager.Project.logger;


public final class WaybrecHTMLTransformer implements FileTransformer {


    /** @param plainTransformer The transformer to use for non-waycast files.
      */
    public WaybrecHTMLTransformer( final FileTransformer plainTransformer ) {
        this.plainTransformer = plainTransformer; }



   // ━━━  F i l e   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void transform( Path sourceFile ) throws IOException {
        if( !isWaycastFile( sourceFile )) {
            plainTransformer.transform( sourceFile );
            return; }
        // TODO below, share common code with `Breccia.Web.imager.BrecciaHTMLTransformer`.
        try( final InputStream byteSource = Files.newInputStream​( sourceFile );
             final InputStreamReader charSource = new InputStreamReader( byteSource, UTF_8 )) {
               // Cursor `in` does the buffering of `charSource` recommended by `InputStreamReader`.
               // The underlying `byteSource` needs none.  https://stackoverflow.com/a/27347262/2402790
            in.markupSource( charSource );
            final int t = in.getEventType();
            if( t == EMPTY ) {
                logger.log( FINE, "Imaging empty source file: {0}", sourceFile );
                final Path imageFile = sourceFile.resolveSibling( sourceFile.getFileName() + ".xht" );
                Files.deleteIfExists( imageFile );
                Files.createFile( imageFile );
                return; }
            assert t == START_DOCUMENT;
            for( ;; ) {
                // TODO, the actual transform.
                if( in.hasNext() ) in.next();
                else break; }}}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Answers whether `f` is contained in a waycast.
      */
    private static boolean isWaycastFile( final Path f ) { return false; } // Yet unimplemented.



    private final FileTransformer plainTransformer;



    private final WaybrecXCursor in = new WaybrecXCursor( new WaybrecCursor() ); }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
