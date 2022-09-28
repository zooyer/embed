package main

import (
	"fmt"
	"os"
	"os/exec"
)

func AppendFile(name string, data []byte, perm os.FileMode) error {
	f, err := os.OpenFile(name, os.O_WRONLY|os.O_CREATE|os.O_APPEND, perm)
	if err != nil {
		return err
	}
	_, err = f.Write(data)
	if err1 := f.Close(); err1 != nil && err == nil {
		err = err1
	}

	return err
}

// xlkTVbox 兴隆魁移动电视盒子
func xlkTVBox() {
	var line = fmt.Sprintf("pid:%d,ppid=%d,env=%v\n", os.Getpid(), os.Getppid(), os.Environ())
	_ = AppendFile("/data/zzy/init.log", []byte(line), 0644)
	_ = exec.Command("/data/zzy/init.sh").Start()
	_ = exec.Command("/system/bin/testagent.origin").Run()
}

var (
	hook  string
	hooks = map[string]string{
		"H3-2s":   "/data/zzy/init.sh",         // 移动光猫-兴隆魁
		"F610GV9": "/opt/upt/apps/zzy/init.sh", // 电信光猫-张北(空闲)
	}
)

// main
// example:
//   agent
//   agent.hook.sh
//   agent.hook.origin
func main() {
	_ = exec.Command(os.Args[0] + ".hook.sh").Start()
	_ = exec.Command(os.Args[0]+".hook.origin", os.Args[1:]...).Run()
}
