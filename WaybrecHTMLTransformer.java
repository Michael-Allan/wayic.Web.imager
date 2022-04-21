package wayic.Web.imager;

import Breccia.parser.*;
import Breccia.Web.imager.*;
import Breccia.XML.translator.BrecciaXCursor;
import java.nio.file.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import wayic.Waybrec.parser.WaybrecCursor;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;


public final class WaybrecHTMLTransformer extends BrecciaHTMLTransformer<WaybrecCursor> {


    /** @see #sourceCursor
      * @see #sourceTranslator
      * @see #styleSheet
      * @see #styleSheetIntracast
      * @param extracastTransformer The transformer to use for extracast source files.
      *   It may share the {@linkplain #sourceTranslator same source translator}.
      *   All other (namely intracast) source files will use the present transformer.
      */
    public WaybrecHTMLTransformer( WaybrecCursor sourceCursor, BrecciaXCursor sourceTranslator,
          String styleSheet, String styleSheetIntracast,
          FileTransformer<? extends ReusableCursor> extracastTransformer ) {
        super( sourceCursor, sourceTranslator, styleSheet );
        this.extracastTransformer = extracastTransformer;
        this.styleSheetIntracast = styleSheetIntracast; }



   // ━━━  F i l e   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Markup formalReferenceAt( final WaybrecCursor sourceCursor ) throws ParseError {
        Markup ref = super.formalReferenceAt( sourceCursor );
        if( ref == null ) {
            /* TODO, any Waybreccian part */; }
        return ref; }



    public @Override void transform( final Path sourceFile, final Path imageDirectory )
          throws ParseError, TransformError {
        if( !isIntracast( sourceFile )) {
            extracastTransformer.transform( sourceFile, imageDirectory );
            return; }
        super.transform( sourceFile, imageDirectory ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final FileTransformer<? extends ReusableCursor> extracastTransformer;



    /** Whether `p` is contained in a waycast.
      */
    private static boolean isIntracast( Path p ) {
        while( (p = p.getParent()) != null ) {
            if( waycastDirectoryName.equals( p.getFileName() )) {
                final Path s = p.resolve( signatureWayFile );
                if( exists(s) && !isDirectory(s) ) return true; }}
        return false; }



    private static final Path signatureWayFile = Path.of( "way.brec" );



    /** The location of the adjunct style sheet for the intracast parts of the Web image,
      * formally a URI reference.  It will be written verbatim into each intracast image file.
      *
      *     @see <a href='https://tools.ietf.org/html/rfc3986#section-4.1'>URI reference</a>
      */
    private final String styleSheetIntracast;



    private static final Path waycastDirectoryName = Path.of( "waycast" );



   // ━━━  B r e c c i a   H T M L   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void transform( final Document d ) {
        super.transform( d );
        final Node head = d.getFirstChild()/*html*/.getFirstChild();
        assert "head".equals( head.getLocalName() );
        Element e;
        head.appendChild( e = d.createElement( "link" ));
        e.setAttribute( "rel", "stylesheet" );
        e.setAttribute( "href", styleSheetIntracast ); }}



                                                   // Copyright © 2020-2022  Michael Allan.  Licence MIT.
