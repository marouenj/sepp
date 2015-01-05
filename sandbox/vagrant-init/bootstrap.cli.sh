
sudo locale-gen UTF-8

sudo apt-get update

sudo apt-get -y install lighttpd openjdk-7-jdk

# THE REAL THING
cd /vagrant/vagrant-init/src/app/ && /vagrant/sepp-classpath/build/linux-x86_64-normal-server-release/jdk/bin/javac -cp .:/vagrant/sepp-classpath/build/linux-x86_64-normal-server-release/jdk/classes launch/Main.java
cd /vagrant/vagrant-init/src/app/ && /vagrant/sepp-classpath/build/linux-x86_64-normal-server-release/jdk/bin/javac -cp .:/vagrant/sepp-classpath/build/linux-x86_64-normal-server-release/jdk/classes launch/MainSepp.java

#sudo apt-get -y install antlr ecj zlib1g-dev

# BUILD SEPP
export CLASSPATH=/vagrant/cli/lib/sepp/src
export CODEBASE=/vagrant/cli/lib/sepp/www

mkdir -p ${CLASSPATH}/launch
mkdir -p ${CLASSPATH}/sepp/client
mkdir -p ${CLASSPATH}/sepp/jcl/rmi/client
mkdir -p ${CLASSPATH}/sepp/jcl/shared
mkdir -p ${CLASSPATH}/util
mkdir -p ${CODEBASE}

cd /vagrant/vagrant-init/src/sepp-cli

find . -name ".DS_Store" -exec rm {} \;
find . -name "*.class" -exec rm {} \;
find . -name "*.java" -exec javac -cp .:/vagrant/sepp_classpath {} \;

jar cf compute.jar sepp/jcl/rmi/compute/*.class
mv compute.jar ${CODEBASE}/

cp launch/Main.class ${CLASSPATH}/launch/
cp launch/MainSepp.class ${CLASSPATH}/launch/
cp sepp/client/SeppContainer.class ${CLASSPATH}/sepp/client/
cp sepp/jcl/ObjectKeyGen.class ${CLASSPATH}/sepp/jcl/
cp sepp/jcl/SingletonCompute.class ${CLASSPATH}/sepp/jcl/
cp sepp/jcl/rmi/client/TaskJoker.class ${CLASSPATH}/sepp/jcl/rmi/client/
cp sepp/jcl/rmi/compute/Compute.class ${CLASSPATH}/sepp/jcl/rmi/compute/
cp sepp/jcl/rmi/compute/Kvs.class ${CLASSPATH}/sepp/jcl/rmi/compute/
cp sepp/jcl/rmi/compute/Task.class ${CLASSPATH}/sepp/jcl/rmi/compute/
cp util/FileIO.class ${CLASSPATH}/util/

# GNU classpath
rm -r /vagrant/gnu_classpath-0.99
cd /vagrant && tar xzf vagrant-init/classpath-0.99.tar.gz
mv /vagrant/classpath-0.99 /vagrant/gnu_classpath-0.99
cd /vagrant/gnu_classpath-0.99
rm -r ./sepp
cp -r ${CLASSPATH}/sepp ./
rm -r sepp/client
cp /vagrant/vagrant-init/ReflectiveOperationException.java /vagrant/gnu_classpath-0.99/java/lang/

make clean
./configure --disable-gconf-peer --disable-gtk-peer --disable-plugin --disable-Werror --with-glibj=flat && make && sudo make install

# Sepp classpath
mkdir -p /vagrant/sepp_classpath/java/util
cp /vagrant/vagrant-init/sepp_classpath/_ArrayList_.java /vagrant/sepp_classpath/java/util/
cd /vagrant/sepp_classpath
#javac -cp /vagrant/gnu_classpath-0.99:/vagrant/gnu_classpath-0.99/vm/reference:${CLASSPATH} java/util/_ArrayList_.java
ecj -cp /vagrant/gnu_classpath-0.99:/vagrant/gnu_classpath-0.99/vm/reference:${CLASSPATH} java/util/_ArrayList_.java
cd java/util
sudo cp _ArrayList_.class /usr/local/classpath/share/classpath/java/util

cd /usr/local/classpath/share/classpath
sudo rm -rf sepp
sudo cp -r /vagrant/gnu_classpath-0.99/sepp ./
sudo /usr/bin/jar cf glibj.zip gnu java javax org sepp sun META-INF

# jamVM
rm -r /vagrant/jamvm-2.0.0
cd /vagrant && tar xzvf vagrant-init/jamvm-2.0.0.tar.gz
cd /vagrant/jamvm-2.0.0
./configure  && make && sudo make install

sudo /etc/init.d/lighttpd stop
kill -9 $(lsof -t -i:2099)
lighttpd -D -f /vagrant/vagrant-init/lighttpd.cli.conf.2 &

cp /vagrant/vagrant-init/input /tmp/

cd ${CLASSPATH}
LOCAL_INET_ADDR=192.168.50.11
REMOT_INET_ADDR=192.168.50.10
/usr/local/jamvm/bin/jamvm -cp ${CLASSPATH}:${CODEBASE}/compute.jar -Djava.rmi.server.codebase=http://${LOCAL_INET_ADDR}:2099/ -Djava.security.policy=/vagrant/vagrant-init/cli.policy launch.MainSepp

java -cp ${CLASSPATH}:${CODEBASE}/compute.jar -Djava.rmi.server.codebase=http://${LOCAL_INET_ADDR}:2099/ -Djava.security.policy=/vagrant/vagrant-init/cli.policy launch.MainSepp
