
Chapter 16. Touring the World

From:
  Pro Java 6 3D Game Development
  Andrew Davison
  Apress, April 2007
  ISBN: 1590598172 
  http://www.apress.com/book/bookDisplay.html?bID=10256
  Web Site for the book: http://fivedots.coe.psu.ac.th/~ad/jg2


Contact Address:
  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat Yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th


If you use this code, please mention my name, and include a link
to the book's Web site.

Thanks,
  Andrew


==================================
TourGL

A simple 3D world using the JOGL active rendering 
programming framework. 

Files and directories here:
  * TourGL.java, TourCanvasGL.java, GroundShapes.java
	// Java code

  * images/	// a directory holding 6 textures and images
      - earth.jpg, hourglass.jpg, r.gif,
        robot.gif, stars.jpg, tree.gif

  * compileGL.bat, runGL.bat	// batch files to simplify compilation
                                   and execution of JOGL code


==================================
Requirements:

* J2SE 5.0 or later from http://java.sun.com/j2se/
  (I use its nanosecond timer, System.nanoTime()).
  I recommend the release version of Java SE 6.

* JOGL, the JSR-231 1.1.0 RC 2, from 
  https://jogl.dev.java.net/

==================================
JOGL Installation

* I downloaded JSR-231 1.1.0 release candidate 2 of JOGL 
  from https://jogl.dev.java.net. I chose the Windows build from 
  January 23rd, jogl-1.1.0-rc2-windows-i586.zip, which contains a lib\ 
  subdirectory holding two JARS (jogl.jar and gluegen-rt.jar) and 
  four DLLs (jogl.dll, gluegen-rt.dll, jogl_awt.dll, and jogl_cg.dll).

* I extracted the lib\ directory, renamed it to jogl\, and stored it 
  on my test machine's d: drive (i.e. as d:\jogl\).

* Use the two batch files, compileGL.bat and runGL.bat, in this
  directory to compile and run JOGL programs. They assume that the JAR 
  and DLLs are in d:\jogl. 

-----
Compilation: 

Use the compileGL.bat batch file.

$ compileGL  *.java

-----
Execution: 

Use the runGL.bat batch file, with the name of the program,
TourGL, and an optional FPS value.

$ runGL TourGL <FPS value>
e.g.
  $ runGL TourGL 80
  $ runGL TourGL 100
or
  $ runGL TourGL
     // this uses a default FPS of 80

-----------
Changes (8th Feb 2007)

The changes fall into two groups:
  1. Changes to use features of JSR 231 1.1.0 RC 2,
     including texture coordinate generation, and
     TextRenderer.

  2. The creation of a new GroundShapes class to deal
     with ground shape initialization and rendering.

Details can be found in TourCanvasGL.java

-----------
Last updated: 4th March 2007