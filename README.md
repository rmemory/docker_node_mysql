Find a host onto which you'd like to run Jenkins. Install Jenkins into its own
Docker container. For example,

```
# install docker
apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'
apt-get update
apt-get install -y docker-engine
systemctl enable docker
systemctl start docker
usermod -aG docker ubuntu


# run jenkins
mkdir -p /var/jenkins_home
chown -R 1000:1000 /var/jenkins_home/
docker run -p 8080:8080 -p 50000:50000 -v /var/jenkins_home:/var/jenkins_home -d --name jenkins jenkins/jenkins:lts


# show endpoint
echo 'Jenkins installed'
echo 'You should now be able to access jenkins at: http://'$(curl -s ifconfig.co)':8080'
```

Note, the Jenkins data is stored in a volume external to the Docker container.
This allows the Jenkins instance running in the Docker container to be 
destroyed and updated at will without affecting any of the data.

However, to be able to use Docker from within Jenkins, it also needs to have 
access to the Docker socket on the host. To do that, use this Dockerfile to install
Jenkins. It adds Docker cli, which will call the docker socket on the host (see the
run command)

```
FROM jenkins/jenkins
USER root


#Install Docker CLI into container
RUN mkdir -p /tmp/download && \
 curl -L https://download.docker.com/linux/static/stable/x86_64/docker-18.03.1-ce.tgz | tar -xz -C /tmp/download && \
 rm -rf /tmp/download/docker/dockerd && \
 mv /tmp/download/docker/docker* /usr/local/bin/ && \
 rm -rf /tmp/download && \
 groupadd -g 999 docker && \
 usermod -aG staff,docker jenkins


USER jenkins
```

In Jenkins, install the CloudBees Docker Build and Publish plugin. In a project, add the Docker Build and Publish build step. Add a repository (typically on Docker Hub). Add registry credentials for Docker Hub. 

To run it, use this command:

```
docker run -p 8080:8080 -p 50000:50000 -v /var/jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -d --name jenkins-docker jenkins-docker
```

And finally, to start the application, use this:

```
docker run -p 3000:3000 -d --name my-nodejs-app richardmemory/node
```