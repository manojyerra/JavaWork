# Install NGINX to test.
# RUN apt-get install nginx -y


#git installation
# RUN apt-get update -y
# RUN apt-get install apt-utils -y
# RUN apt-get install git -y
# RUN apt-get install software-properties-common -y
# RUN apt-add-repository ppa:git-core/ppa -y
# RUN apt-get update -y
# RUN apt-get install git -y



FROM gitimg

RUN apt-add-repository ppa:webupd8team/java -y
RUN apt-get update -y
RUN /bin/echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN /bin/echo debconf shared/accepted-oracle-license-v1-1 seen true | /usr/bin/debconf-set-selections
RUN apt-get install oracle-java8-installer -y



COPY script.sh /home/script.sh

CMD /home/script.sh

