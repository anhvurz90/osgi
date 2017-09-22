Link: https://www.osgi.org/developer/resources/learning-resources-tutorials/tutorial-archive/
1.Your first bundle: {
  - Link: https://www.eclipsezone.com//eclipse/forums/m92130843.html
  - Run: java -Dosgi.noshutdown=true -Dosgi.console.enable.builtin=true -jar org.eclipse.osgi_3.9.1.v20140110-1610.jar -console
  - ss: short status
  - HelloActivator: {
    import org.osgi.framework.*;
 
    public class HelloActivator implements BundleActivator {
    	public void start(BundleContext context) {
    	    System.out.println("Hello EclipseZone Readers!");
  	}
 
  	public void stop(BundleContext context) {
    	    System.out.println("Goodbye EclipseZone Readers!");
  	}
    }
  } 
}
