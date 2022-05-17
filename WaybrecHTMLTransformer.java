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


    /** @see #sourceCursor()
      * @see #sourceTranslator
      * @param extracastTransformer The transformer to use for extracast source files.
      *   All other (namely intracast) source files will use the present transformer.
      *   Both transformers may share the same {@linkplain #sourceTranslator source translator}.
      */
    public WaybrecHTMLTransformer( WaybrecCursor sourceCursor, BrecciaXCursor sourceTranslator,
          ImageMould<?> mould, FileTransformer<?> extracastTransformer, ImagingOptions opt ) {
        super( sourceCursor, sourceTranslator, mould );
        this.extracastTransformer = extracastTransformer;
        this.opt = opt; }



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


    private final FileTransformer<?> extracastTransformer;



    /** Whether `p` is contained in a waycast.
      */
    private static boolean isIntracast( Path p ) {
        while( (p = p.getParent()) != null ) {
            if( waycastDirectoryName.equals( p.getFileName() )) {
                final Path s = p.resolve( signatureWayFile );
                if( exists(s) && !isDirectory(s) ) return true; }}
        return false; }



    private final ImagingOptions opt;



    private static final Path signatureWayFile = Path.of( "way.brec" );



    private static final Path waycastDirectoryName = Path.of( "waycast" );



   // ━━━  B r e c c i a   H T M L   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void transform( final Document d ) {
        super.transform( d );
        final Node head = d.getFirstChild()/*html*/.getFirstChild();
        assert "head".equals( head.getLocalName() );
        Element e;
        head.appendChild( e = d.createElement( "link" ));
        e.setAttribute( "rel", "stylesheet" );
        e.setAttribute( "href", opt.coServiceDirectory() + "wayic/Web/imager/image.css" ); }}



                                                   // Copyright © 2020-2022  Michael Allan.  Licence MIT.
