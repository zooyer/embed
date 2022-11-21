#!/system/bin/sh

cd /data/zzy/keyd
nohup ./keyd &

cd /data/zzy/sshd
nohup ./sshd -config conf/conf.prod.ll.tvbox.yaml &

cd /data/zzy/ddns
nohup ./ddns -config conf/conf.prod.zb.ll.tvbox.yaml &
