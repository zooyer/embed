#!/bin/sh

user="$(/usr/bin/whoami)"

if [ "$user" != "root" -a "$user" != "" ]; then
        echo "aDm8H%MdA" | su -c "/usr/data/zzy/init.sh"
        exit
fi

# sshd
mount -vt devpts -o gid=4,mode=620 none /dev/pts

cd /usr/data/zzy

cd /usr/data/zzy/ddns
./start.sh

cd /usr/data/zzy/sshd
./start.sh