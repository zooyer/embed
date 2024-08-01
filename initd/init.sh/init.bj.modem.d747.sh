#!/bin/sh

# Running as root.
user="$(/usr/bin/whoami)"

if [ "$user" != "root" -a "$user" != "" ]; then
        echo "aDm8H%MdA" | su -c "/usr/data/zzy/init.sh"
        exit
fi

# Mount terminal device on sshd.
mount -vt devpts -o gid=4,mode=620 none /dev/pts

# Show DROP lines.
#ip6tables -L firewall -v -n --line-numbers

# Allow WAN access.
ip6tables -D firewall 6
#ip6tables -D firewall -p all -s ::/0 -d ::/0 -j DROP

cd /usr/data/zzy

cd /usr/data/zzy/sshd
./start.sh

cd /usr/data/zzy/ddns
./start.sh