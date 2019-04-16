@echo off
rem LAF=-Dswing.defaultlaf=org.jvnet.substance.SubstanceLookAndFeel
rem G:\SDK\jdk1.6.0\bin\
rem -Dsun.java2d.accthreshold=0 -Dsun.java2d.translaccel=true -Dsun.java2d.d3d=true
rem C:\bin\Java\jdk1.5.0_09\bin\java.exe   -Dsun.java2d.opengl=True -classpath lib/RXTXcomm.jar;bin ru.dz.shipMaster.Main
rem C:\bin\Java\jdk1.5.0_09\bin\java.exe   -Dsun.java2d.opengl=True -classpath lib/RXTXcomm.jar;bin ru.dz.shipMaster.Main
rem java -server -Xmx256m -Xms128m -classpath lib/RXTXcomm.jar;bin;./lib;lib/rxtxSerial.dll  ru.dz.shipMaster.Main

rem java  -server -Xmx256m -Xms128m -classpath lib/RXTXcomm.jar;bin;lib;./lib;./lib/rxtxSerial.dll ru.dz.shipMaster.Main
java  -Xmx512m -Xms256m -classpath lib/RXTXcomm.jar;bin;lib;./lib;./lib/rxtxSerial.dll ru.dz.shipMaster.Main %1 %2 %3 %4

rem java -server -Xmx256m -Xms128m -Djava.library.path=lib ru.dz.shipMaster.Main -Djava.util.logging.config.file=logging.properties

