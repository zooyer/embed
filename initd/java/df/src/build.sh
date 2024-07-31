#!/bin/bash

rm -rf Main.class felix.jar
# Main.class当前jar入口类，直接java -jar运行的类，然后会调用Hook主流程类。
# com/zzy/main/Main.class当前Hook主流程类，一般不会直接指定该类入口运行。
# org/apache/felix/main/Main.class指定jar包入口类，可能之前运行jar包是指定jar包中的这个类，所以Hook该入口，然后调用Hook主流程类。
javac Main.java && jar -cvfm felix.jar META-INF/MANIFEST.MF Main.class com/zzy/main/Main.class org/apache/felix/main/Main.class