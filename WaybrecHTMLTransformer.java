package wayic.Web.imager;

import Breccia.parser.*;
import Breccia.Web.imager.FileTransformer;
import Breccia.Web.imager.TransformError;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Path;
import Java.Unhandled;
import javax.xml.stream.XMLStreamException;
import wayic.Waybrec.parser.WaybrecCursor;
import wayic.Waybrec.parser.WaybrecXCursor;

import static Breccia.parser.BrecciaXCursor.EMPTY;
import static Breccia.parser.Project.newSourceReader;
import static Breccia.Web.imager.Imaging.imageSimpleName;
import static java.nio.file.Files.createFile;
import static wayic.Web.imager.Project.logger;


public final class WaybrecHTMLTransformer implements FileTransformer<WaybrecCursor> {


    /** @see #inX
      * @param plainTransformer The transformer to use for non-waycast files.
      */
    public WaybrecHTMLTransformer( WaybrecXCursor inX,
          FileTransformer<BrecciaCursor> plainTransformer ) {
        this.inX = inX;
        this.plainTransformer = plainTransformer; }



    /** The source translator to use during calls to this transformer for waycast files.
      * Between calls, it may be used for other purposes.
      */
    public final WaybrecXCursor inX;



   // ━━━  F i l e   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Markup formalReferenceAt( final WaybrecCursor sourceCursor ) {
        Markup markup = plainTransformer.formalReferenceAt( sourceCursor );
        if( markup == null ) {
            /* TODO, the Waybreccian part */; }
        return markup; }



    public @Override WaybrecCursor sourceCursor() { return inX.sourceCursor(); }



    public @Override void transform( final Path sourceFile, final Path imageDirectory )
          throws ParseError, TransformError {
        if( !isWaycastFile( sourceFile )) {
            plainTransformer.transform( sourceFile, imageDirectory );
            return; }
        // TODO below, share common code with `Breccia.Web.imager.BrecciaHTMLTransformer`.
        try( final Reader source = newSourceReader​( sourceFile )) {
            inX.markupSource( source ); /* Better not to parse functionally using `inX.perState`
              and mess with shipping a checked `TransformError` out of the lambda function. */
            for( ;; ) {
                switch( inX.getEventType() ) {
                    case EMPTY -> {
                        logger.fine( () -> "Imaging empty source file: " + sourceFile );
                        final Path imageFile = imageDirectory.resolve( imageSimpleName( sourceFile ));
                        createFile( imageFile ); }}
                    // TODO, the actual transform for other states.
                if( !inX.hasNext() ) break;
                try { inX.next(); }
                catch( final XMLStreamException x ) { throw (ParseError)(x.getCause()); }}}
        catch( IOException x ) { throw new Unhandled( x ); }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Whether `f` is contained in a waycast.
      */
    private static boolean isWaycastFile( final Path f ) { return false; } // Yet unimplemented.



    private final FileTransformer<BrecciaCursor> plainTransformer; }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
