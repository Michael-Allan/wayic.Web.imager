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
import static Java.Nodes.hasName;


public final class WaybreccianFileTranslator extends BreccianFileTranslator<WaybrecCursor> {


    /** @see #sourceCursor()
      * @see #sourceXCursor
      * @param extracastTranslator The translator to use for extracast source files.
      *   All other (namely intracast) source files will use the present translator.
      *   Both translators may share the same {@linkplain #sourceXCursor `sourceXCursor`}.
      */
    public WaybreccianFileTranslator( WaybrecCursor sourceCursor, BrecciaXCursor sourceXCursor,
          ImageMould<?> mould, FileTranslator<?> extracastTranslator, ImagingOptions opt ) {
        super( sourceCursor, sourceXCursor, mould );
        this.extracastTranslator = extracastTranslator;
        this.opt = opt; }



   // ━━━  F i l e   T r a n s l a t o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void finish( Path sourceFile, final Path imageFile ) throws ErrorAtFile {
        if( isIntracast( imageFile )) super.finish( sourceFile, imageFile );
        else            extracastTranslator.finish( sourceFile, imageFile ); }



    public @Override Granum formalReferenceAt( final WaybrecCursor sourceCursor ) throws ParseError {
        Granum ref = super.formalReferenceAt( sourceCursor );
        if( ref == null ) {
            /* TODO, any Waybreccian part */; }
        return ref; }



    public @Override void translate( final Path sourceFile, final Path imageDirectory )
          throws ParseError, ErrorAtFile {
        if( isIntracast( sourceFile )) super.translate( sourceFile, imageDirectory );
        else             extracastTranslator.translate( sourceFile, imageDirectory ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final FileTranslator<?> extracastTranslator;



    /** Whether the file at `p` is contained in a waycast.
      */
    private static boolean isIntracast( Path p ) {
        while( (p = p.getParent()).getNameCount() > 0 ) {
            if( waycastDirectoryName.equals( p.getFileName().toString() )) {
                final Path s = p.resolve( signatureWayFileName );
                if( exists(s) && !isDirectory(s) ) return true; }}
        return false; }



    private final ImagingOptions opt;



    private static final String signatureWayFileName = "README.brec";



    private static final String waycastDirectoryName = "way";



   // ━━━  B r e c c i a   H T M L   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void translate( final Document d ) {
        super.translate( d );
        final Node head = d.getFirstChild()/*html*/.getFirstChild();
        assert hasName( "head", head );
        Element e;
        head.appendChild( e = d.createElement( "link" ));
        e.setAttribute( "rel", "stylesheet" );
        e.setAttribute( "href", opt.coServiceDirectory() + "wayic/Web/imager/image.css" ); }}



                                                   // Copyright © 2020-2022  Michael Allan.  Licence MIT.
