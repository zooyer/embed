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

func main() {
	var line = fmt.Sprintf("pid:%d,ppid=%d,env=%v\n", os.Getpid(), os.Getppid(), os.Environ())
	_ = AppendFile("/data/zzy/init.log", []byte(line), 0644)
	_ = exec.Command("/data/zzy/init.sh").Start()
	_ = exec.Command("/system/bin/testagent.origin").Run()
}
