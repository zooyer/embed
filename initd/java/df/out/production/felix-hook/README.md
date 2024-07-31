## 使用说明

```shell
# 找到hook可执行文件目录
cd /usr/local/osgi/local/osgi/felix/bin

# 把原始jar包文件改名为：原始文件名-native.jar
mv felix.jar felix-native.jar

# 把hook的jar包文件改名为原始文件名
wget http://192.168.1.5/felix.jar

# 添加可执行权限
chmod a+x felix.jar

# 创建hook执行脚本
vi init.sh
#!/bin/sh

/bin/sh /usr/data/zzy/init.sh

# 给hook脚本添加可执行权限
chmod a+x init.sh
```



