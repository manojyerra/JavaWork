FROM gitimg

RUN apt-get install software-properties-common
RUN apt-add-repository -y ppa:ansible/ansible
RUN apt-get update
RUN apt-get install -y ansible

COPY script.sh /home/script.sh

CMD /home/script.sh

