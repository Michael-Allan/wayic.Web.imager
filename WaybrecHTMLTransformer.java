package wayic.Web.imager;

import Breccia.parser.*;
import Breccia.Web.imager.FileTransformer;
import Breccia.Web.imager.TransformError;
import Breccia.XML.translator.BrecciaXCursor;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Path;
import Java.Unhandled;
import javax.xml.stream.XMLStreamException;
import wayic.Waybrec.parser.WaybrecCursor;

import static Breccia.parser.plain.Project.newSourceReader;
import static Breccia.Web.imager.Imaging.imageSimpleName;
import static Breccia.XML.translator.BrecciaXCursor.EMPTY;
import static java.nio.file.Files.createFile;
import static wayic.Web.imager.Project.logger;


public final class WaybrecHTMLTransformer implements FileTransformer<WaybrecCursor> {


    /** @see #sourceCursor
      * @see #sourceTranslator
      * @param plainTransformer The transformer to use for non-waycast files.
      */
    public WaybrecHTMLTransformer( WaybrecCursor sourceCursor, BrecciaXCursor sourceTranslator,
          FileTransformer<ReusableCursor> plainTransformer ) {
        this.sourceCursor = sourceCursor;
        this.sourceTranslator = sourceTranslator;
        this.plainTransformer = plainTransformer; }



    /** The source translator to use during calls to this transformer for waycast files.
      * Between calls, it may be used for other purposes.
      */
    public final BrecciaXCursor sourceTranslator;



   // ━━━  F i l e   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Markup formalReferenceAt( final WaybrecCursor sourceCursor ) throws ParseError {
        Markup ref = plainTransformer.formalReferenceAt( sourceCursor );
        if( ref == null ) {
            /* TODO, any Waybreccian part */; }
        return ref; }



    public @Override WaybrecCursor sourceCursor() { return sourceCursor; }



    public @Override void transform( final Path sourceFile, final Path imageDirectory )
          throws ParseError, TransformError {
        if( !isWaycastFile( sourceFile )) {
            plainTransformer.transform( sourceFile, imageDirectory );
            return; }
        // TODO below, reuse code of `Breccia.Web.imager.BrecciaHTMLTransformer` where common.
        try( final Reader sourceReader = newSourceReader​( sourceFile )) {
            sourceCursor.markupSource( sourceReader );
            sourceTranslator.markupSource( sourceCursor ); /* Better not to parse functionally using its
              `perState` and mess with shipping a checked `TransformError` out of the lambda function. */
            for( final BrecciaXCursor inX = sourceTranslator;; ) {
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



    private final FileTransformer<ReusableCursor> plainTransformer;



    private final WaybrecCursor sourceCursor; }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
