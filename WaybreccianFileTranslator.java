package wayic.Web.imager;

import Breccia.parser.*;
import Breccia.Web.imager.*;
import Breccia.XML.translator.BrecciaXCursor;
import Java.CharacterPointer;
import java.net.URI;
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
        if( isIntracast( sourceFile )) super.finish( sourceFile, imageFile );
        else             extracastTranslator.finish( sourceFile, imageFile ); }



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



    /** Whether the given file is contained in a waycast.
      */
    private static boolean isIntracast( final Path f ) { return ownerWaycast(f.getParent()) != null; }



    private final ImagingOptions opt;



    /** Returns the directory of the apparent waycast wherein `p` is contained (which may be `p` itself),
      * or null if `p` lies outside of a waycast.
      */
    private static Path ownerWaycast( Path p ) {
        while( p.getNameCount() > 0 ) {
            if( waycastDirectoryName.equals( p.getFileName().toString() )) {
                final Path s = p.resolve( signatureWayFileName );
                if( exists(s) && !isDirectory(s) ) return p; }
            p = p.getParent(); }
        return null; }



    private static final String signatureWayFileName = "README.brec";



    private static final String waycastDirectoryName = "way";



   // ━━━  B r e c c i a   H T M L   T r a n s f o r m e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override String hRefLocal( final Path f, final Element eRef, final String sRef,
          final boolean isAlteredRef, final URI uRef, final Path pRef, final Path pRefAbsolute ) {
        if( !isAlteredRef ) { // For what follows serves as a lint check on the original source only.
            final String message; {
                if( pRef.getRoot() != null) { // Not a relative-path reference. [RR]
                    message = "Absolute-path reference in waycast"; }
                else if( !pRefAbsolute.normalize().startsWith( ownerWaycast(f).normalize() )) {
                    message = "Referent lies outside of the waycast"; }
                else message = null; }
            if( message != null  &&  !isPrivatized( contextFractum( eRef ))) { /*
                  Abiding here (as in `hRefRemote`) by an equivalent of a constraint on way models.
                  http://reluk.ca/project/wayic/model/working_notes.brec.xht#reference,malformed,following */
                final CharacterPointer p = characterPointer( eRef );
                mould.warn( f, p, message + "; consider marking this reference as private:\n"
                  + mould.markedLine( sRef, p, isAlteredRef )); }} /* Yet proceed with hyperlinking,
                  for the purpose here is satified by flagging the fault in the waysource. */
        return super.hRefLocal( f, eRef, sRef, isAlteredRef, uRef, pRef, pRefAbsolute ); }



    protected @Override String hRefRemote( final Path f, final Element eRef, final String sRef,
          final boolean isAlteredRef, final URI uRef ) {
        if( !isAlteredRef ) { // For what follows serves as a lint check on the original source only.
            if( uRef.getScheme() == null  &&  !isPrivatized( contextFractum( eRef ))) { /*
                  Abiding here (as in `hRefLocal`) by an equivalent of a constraint on way models.
                  http://reluk.ca/project/wayic/model/working_notes.brec.xht#reference,malformed,following */
                final CharacterPointer p = characterPointer( eRef );
                mould.warn( f, p, // ↓ [RR]
                  "Network-path reference in waycast; consider marking this reference as private:\n"
                    + mould.markedLine( sRef, p, isAlteredRef )); }} /* Yet proceed with hyperlinking,
                  for the purpose here is satified by flagging the fault in the waysource. */
        return super.hRefRemote( f, eRef, sRef, isAlteredRef, uRef ); }



    protected @Override void translate( final Path sourceFile, final Document d ) {
        super.translate( sourceFile, d );
        final Node head = d.getFirstChild()/*html*/.getFirstChild();
        assert hasName( "head", head );
        Element e;
        head.appendChild( e = d.createElement( "link" ));
        e.setAttribute( "rel", "stylesheet" );
        e.setAttribute( "href", opt.coServiceDirectory() + "wayic/Web/imager/image.css" ); }}



// NOTE
// ────
//   RR · Relative reference.  https://www.rfc-editor.org/rfc/rfc3986#section-4.2



                                                   // Copyright © 2020-2022  Michael Allan.  Licence MIT.
