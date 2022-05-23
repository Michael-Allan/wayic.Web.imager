package wayic.Web.imager;


public class ImagingOptions extends Breccia.Web.imager.ImagingOptions {


    /** @see #commandName
      */
    public ImagingOptions( String commandName ) { super( commandName ); } // [SLA]



    protected @Override boolean initialize( final String arg ) {
     // boolean isGo = true;
     // String s;
     // if( arg.startsWith( s = "--foo=" )) foo = value( arg, s );
     // else isGo = super.initialize( arg );
     // return isGo; }}
        return super.initialize( arg ); }}



// NOTE
// ────
//   SLA  Source-launch access.  This member would have `protected` access if access were not needed by
//        the `WaycastWebImageCommand` class.  Source launched and loaded by a separate class loader,
//        that class is treated at runtime as residing in a separate package.



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
