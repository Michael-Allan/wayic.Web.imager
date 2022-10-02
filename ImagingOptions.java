package wayic.Web.imager;


/** @see <a href='http://reluk.ca/project/wayic/Web/imager/bin/waycast-web-image.brec.xht#positional,argument,arguments'>
  *   Options for the `waycast-web-image` command</a>
  */
public class ImagingOptions extends Breccia.Web.imager.ImagingOptions {


    public ImagingOptions( String commandName ) { super( commandName ); } // [SLA]



    protected @Override boolean initialize( final String arg ) { return super.initialize( arg ); }}



// NOTE
// ────
//   SLA  Source-launch access.  This member would have `protected` access were it not needed by
//        class `WaycastWebImageCommand`.  Source launched and loaded by a separate class loader,
//        that class is treated at runtime as residing in a separate package.



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
