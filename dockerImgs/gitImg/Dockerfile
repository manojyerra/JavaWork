FROM ubuntu

RUN apt-get update -y
RUN apt-get install -y apt-utils
RUN apt-get install -y software-properties-common
RUN apt-add-repository -y ppa:git-core/ppa
RUN apt-get install -y git

COPY script.sh /home/script.sh

CMD /home/script.sh
