@echo off
echo Executing %1 with JOGL...

java -cp "d:\jogl\jogl.jar;d:\jogl\gluegen-rt.jar;." -Djava.library.path="d:\jogl" -Dsun.java2d.noddraw=true %1 %2

echo Finished.