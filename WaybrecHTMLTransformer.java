package wayic.Web.imager;

import Breccia.parser.ParseError;
import Breccia.Web.imager.FileTransformer;
import java.io.*;
import java.nio.file.Path;
import Java.Unhandled;
import javax.xml.stream.XMLStreamException;
import wayic.Waybrec.parser.WaybrecCursor;
import wayic.Waybrec.parser.WaybrecXCursor;

import static Breccia.parser.BrecciaXCursor.EMPTY;
import static Breccia.parser.BrecciaXCursor.START_DOCUMENT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.newInputStream;
import static wayic.Web.imager.Project.logger;


public final class WaybrecHTMLTransformer implements FileTransformer {


    /** @param plainTransformer The transformer to use for non-waycast files.
      */
    public WaybrecHTMLTransformer( final FileTransformer plainTransformer ) {
        this.plainTransformer = plainTransformer; }



   // ━━━  F i l e   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void transform( final Path sourceFile, final Path imageDirectory )
          throws IOException, ParseError {
        if( !isWaycastFile( sourceFile )) {
            plainTransformer.transform( sourceFile, imageDirectory );
            return; }
        // TODO below, share common code with `Breccia.Web.imager.BrecciaHTMLTransformer`.
        try( final InputStream byteSource = newInputStream​( sourceFile );
             final InputStreamReader charSource = new InputStreamReader( byteSource, UTF_8 )) {
               // Cursor `in` does the buffering of `charSource` recommended by `InputStreamReader`.
               // The underlying `byteSource` needs none.  https://stackoverflow.com/a/27347262/2402790
            in.markupSource( charSource );
            final int t = in.getEventType();
            if( t == EMPTY ) {
                logger.fine( () -> "Imaging empty source file: " + sourceFile );
                final Path imageFile = imageDirectory.resolve( sourceFile.getFileName() + ".xht" );
                createFile( imageFile );
                return; }
            assert t == START_DOCUMENT;
            for( ;; ) {
                // TODO, the actual transform.
                if( !in.hasNext() ) break;
                try { in.next(); }
                catch( final XMLStreamException x ) {
                    final var cause = x.getCause();
                    if( cause instanceof ParseError ) throw (ParseError)cause;
                    throw new Unhandled( x ); }}}}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Whether `f` is contained in a waycast.
      */
    private static boolean isWaycastFile( final Path f ) { return false; } // Yet unimplemented.



    private final FileTransformer plainTransformer;



    private final WaybrecXCursor in = new WaybrecXCursor( new WaybrecCursor() ); }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
