 export JAVA_HOME=`/usr/libexec/java_home -v 1.8` && mvn package && mvn clean &&  mvn clean test package jetty:run 
