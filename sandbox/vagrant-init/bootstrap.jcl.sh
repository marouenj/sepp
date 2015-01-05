sudo locale-gen UTF-8

# installs, updates
sudo apt-get update
sudo apt-get -y build-dep openjdk-7
sudo apt-get -y install ccache mercurial openjdk-7-jdk

# clones openjdk 8 source
cd /vagrant && hg clone http://hg.openjdk.java.net/jdk8/jdk8 ./sepp-classpath
cd /vagrant/sepp-classpath && bash ./get_source.sh

# backs up/restores openjdk 8
cd /vagrant && tar cf sepp-classpath.tar sepp-classpath
#cd /vagrant && rm -rf sepp-classpath
#cd /vagrant && tar xf sepp-classpath.tar

# appends openjdk source with sepp additions (sepp lib + classes re-implementation for remote invocation)
# here only ArrayList is re-implemented (not entirely)
#cd /vagrant/vagrant-init/src/jcl/ && find . -name ".DS_Store" -exec rm {} \;
cp -R /vagrant/vagrant-init/src/jcl/java /vagrant/sepp-classpath/jdk/src/share/classes/
cp -R /vagrant/vagrant-init/src/jcl/sepp /vagrant/sepp-classpath/jdk/src/share/classes/

export LANG=C

# builds openjdk 8
cd /vagrant/sepp-classpath && bash ./configure
cd /vagrant/sepp-classpath && make all
