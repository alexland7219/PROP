#!/bin/bash
cd ../../FONT
rm classes/*.class
javac --release 11 -cp classes/miglayout-core-4.3-20121116.151612-3.jar:classes/miglayout-swing-4.3-20121116.151626-3.jar:. classes/CtrlPresentacio.java
java -cp classes/miglayout-core-4.3-20121116.151612-3.jar:classes/miglayout-swing-4.3-20121116.151626-3.jar:. classes.CtrlPresentacio 2 &> log.txt
