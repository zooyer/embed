#!/bin/bash

rm -rf Main.class felix.jar
javac Main.java && jar -cvfm felix.jar META-INF/MANIFEST.MF Main.class
