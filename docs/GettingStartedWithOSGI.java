Link: https://www.osgi.org/developer/resources/learning-resources-tutorials/tutorial-archive/
1.Your first bundle: {
  - Link: https://www.eclipsezone.com//eclipse/forums/m92130843.html
  - Run: java -Dosgi.noshutdown=true -Dosgi.console.enable.builtin=true -jar org.eclipse.osgi_3.9.1.v20140110-1610.jar -console
  - ss: short status
  - HelloActivator.java: {
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
  - HelloWorld.mf: {
        Manifest-Version: 1.0
	Bundle-Name: HelloWorld
	Bundle-Activator: HelloActivator
	Bundle-SymbolicName: HelloWorld
	Bundle-Version: 1.0.0
	Import-Package: org.osgi.framework
    }
  - Compile & Build: {
	> javac -classpath equinox.jar HelloActivator.java
	> jar -cfm HelloWorld.jar HelloWorld.mf HelloActivator.class
  }
  - install: {
    	> install file:HelloWorld.jar
  }
  - start: {
	start 1
  }
}
2.Interacting with the Framework: {
  - HelloWorldKiller.java: {
	import org.osgi.framework.*;
	public class HelloWorldKiller implements BundleActivator {
		public void start(BundleContext context) {
			System.out.println("HelloWorldKiller searching...");
			Bundle[] bundles = context.getBundles();
			for(int i=0; i<bundles.length; i++) {
				if("HelloWorld".equals(bundles[i]
						      .getSymbolicName())) {
					try {
					    System.out.println("Hello World found, "
							     + "destroying!");
					    bundles[i].uninstall();
					    return;
					} catch (BundleException e) {
					    System.err.println("Failed: "
							     + e.getMessage());
					}
				}
			}
			System.out.println("Hello World bundle not found");
		}
 
		public void stop(BundleContext context) {
			System.out.println("HelloWorldKiller shutting down");
		}
	}
  }
  - HelloWorldKiller.mf: {
    	Manifest-Version: 1.0
	Bundle-Name: HelloWorldKiller
	Bundle-Activator: HelloWorldKiller
	Bundle-SymbolicName: HelloWorldKiller
	Bundle-Version: 1.0.0
	Import-Package: org.osgi.framework
  }
  - Compile & Build: {
	> javac -classpath equinox.jar HelloWorldKiller.java
	> jar -cfm HelloWorldKiller.jar HelloWorldKiller.mf HelloWorldKiller.class
  }
  - install: {
	install file:HelloWorldKiller.jar
  }
  - start: {
	start 2
  }
}
3.Dependencies between Bundles: {
  - Movie.java: {
	package osgitut.movies;
 
	public class Movie {
	    private final String title;
	    private final String director;
	 
	    public Movie(String title, String director) {
		this.title = title; this.director = director;
	    }
	    public String getTitle() { return title; }
	    public String getDirector() { return director; }
	}
  }
  - MovieFinder.java: {
	package osgitut.movies;
 
	public interface MovieFinder {
	    Movie[] findAll();
	}
  }
  - MoviesInterface.mf: {
	Manifest-Version: 1.0
	Bundle-ManifestVersion: 2
	Bundle-Name: Movies Interface
	Bundle-SymbolicName: MoviesInterface
	Bundle-Version: 1.0.0
	Export-Package: osgitut.movies;version="1.0.0"
  }
  - Compile & Build: {
	> javac osgitut/movies/Movie.java osgitut/movies/MovieFinder.java
	> jar -cfm MoviesInterface.jar MoviesInterface.mf osgitut/movies/*.class
  }
  - BasicMovieFinderImpl: {
	package osgitut.movies.impl;
 
	import osgitut.movies.*;
	 
	public class BasicMovieFinderImpl implements MovieFinder {
	  private static final Movie[] MOVIES = new Movie[] {
	    new Movie("The Godfather", "Francis Ford Coppola"),
	    new Movie("Spirited Away", "Hayao Miyazaki")
	  };
	 
	  public Movie[] findAll() { return MOVIES; }
	}
  }
  - BasicMovieFinder.mf: {
	Manifest-Version: 1.0
	Bundle-ManifestVersion: 2
	Bundle-Name: Basic Movie Finder
	Bundle-SymbolicName: BasicMovieFinder
	Bundle-Version: 1.0.0
	Import-Package: osgitut.movies;version="[1.0.0,2.0.0)"
	 
  }	  
  - Compile & Build: {
	> javac -classpath MoviesInterface.jar osgitut/movies/impl/BasicMovieFinderImpl.java 
	> jar -cfm BasicMovieFinder.jar BasicMovieFinder.mf osgitut/movies/impl/*.class
  }
  - install
  - resolve: {
	refresh 4
  }  
  - diagnostic: {
	diag 4 --->
		file:BasicMovieFinder.jar [4]
		Missing imported package osgitut.movies_[1.0.0,2.0.0).
  }
  - so, need to install MoviesInterface.jar and then run "refresh 4"
}