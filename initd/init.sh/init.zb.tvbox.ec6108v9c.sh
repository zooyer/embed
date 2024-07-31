#!/system/bin/sh

cd /data/zzy/keyd
./start.sh

cd /data/zzy/sshd
./start.sh

#cd /data/zzy/ddns
#nohup ./ddns -config conf/conf.prod.zb.ll.tvbox.yaml &
