java -server -XX:+UseParallelGC -Xmn10g  -Xms26g -Xmx26g -Djava.library.path=C:/Users/groff/downloads/librocksdbjni-win64.dll -cp \Progra~1\apache\apache-tomcat\lib\Relatrix.jar;\Progra~1\apache\apache-tomcat\lib\RockSack.jar;\Progra~1\apache\apache-tomcat\lib\rocksdbjni-9.10.0-win64.jar;\Progra~1\apache\apache-tomcat\lib\volvex.jar;\Progra~1\apache\apache-tomcat\lib\neurovolve.jar;\Progra~1\apache\apache-tomcat\lib\RoboCore.jar com.neocoretechs.relatrix.server.RelatrixLockingTransactionServer %1 %2 %3