# HTTP Logger

If you create penetration test, and one http endpoint is needed. Check bash script in this directory.

# curl
`curl ip:port -i`  
# Maven build
`~/java_env/maven/bin/mvn archetype:generate -DgroupId=tech.cybersword -DartifactId=tech.cybersword.java.command.control -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`
# build
`~/java_env/maven/bin/mvn clean package`  
# run
`~/java_env/jdk/Contents/Home/bin/java -jar target/tech.cybersword.java.command.control-1.0-SNAPSHOT.jar`  
# copy
`scp target/tech.cybersword.java.command.control-1.0-SNAPSHOT.jar game@server:/home/game/`  
# crontab
`@reboot /home/game/start.tech.cybersword.java.command.control.sh`  