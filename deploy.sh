# Remove any existing code
# Grab the new code

# Stop tomcat
/usr/app/example-service/init/stop.sh

# Run maven to intall
sudo mvn clean integration-test -Dmaven.tomcat.deploy=true -Ddeploy.environment=dev

# Start tomcat
/usr/app/example-service/init/start.sh