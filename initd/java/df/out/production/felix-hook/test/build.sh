#!/bin/bash

rm -rf Test.class test.jar
javac Test.java && jar -cvfm test.jar META-INF/MANIFEST.MF Test.class