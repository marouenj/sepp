
sudo locale-gen UTF-8

sudo apt-get update
sudo apt-get -y install lighttpd openjdk-7-jdk

###

sudo cp /vagrant/vagrant-init/hosts.server /etc/hosts

export CLASSPATH=/vagrant/srv/lib/sepp/src
export CODEBASE=/vagrant/srv/lib/sepp/www

mkdir -p ${CLASSPATH}/launch
mkdir -p ${CLASSPATH}/sepp/jcl/shared
mkdir -p ${CLASSPATH}/sepp/rmi/server
mkdir -p ${CODEBASE}

cd /vagrant/vagrant-init/src/server/ && find . -name "*.DS_Store" -exec rm {} \;
cd /vagrant/vagrant-init/src/server/ && find . -name "*.class" -exec rm {} \;
cd /vagrant/vagrant-init/src/server/ && find . -name "*.java" -exec javac -cp . {} \;

jar cf compute.jar sepp/jcl/shared/*.class
mv compute.jar ${CODEBASE}/

cp launch/Main.class ${CLASSPATH}/launch
cp sepp/rmi/server/ComputeImpl.class ${CLASSPATH}/sepp/rmi/server/
cp sepp/rmi/server/KvsImpl.class ${CLASSPATH}/sepp/rmi/server/

#sudo /etc/init.d/lighttpd stop
#kill -9 $(lsof -t -i:2099)
lighttpd -D -f /vagrant/vagrant-init/lighttpd.conf.server &

#kill -9 $(lsof -t -i:1099)
rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false &

cd ${CLASSPATH}
export INET_ADDR=192.168.50.10
java -cp ${CLASSPATH}:${CODEBASE}/compute.jar -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.codebase=http://${INET_ADDR}:2099/compute.jar -Djava.rmi.server.hostname=${INET_ADDR} -Djava.security.policy=/vagrant/vagrant-init/policy.server launch/Main
