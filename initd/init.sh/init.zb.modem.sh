#!/bin/sh

user="$(/usr/bin/whoami)"
#su="$(which su)"

#if [ "$user" != "root" -a "$user" != "" -a "$su" != "" ]; then
#        echo "Zxic521" | su -c "/usr/data/zzy/init.sh"
#        exit
#fi

if [ "$user" != "root" -a "$user" != "" ]; then
        echo "Zxic521" | ./busybox su -c "/usr/data/zzy/init.sh"
        exit
fi

# sshd
mount -vt devpts -o gid=4,mode=620 none /dev/pts

cd /usr/data/zzy

cd /usr/data/zzy/ddns
./start.sh

cd /usr/data/zzy/sshd
./start.sh