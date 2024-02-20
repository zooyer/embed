package main

/*
//#cgo LDFLAGS: -lpmapi
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>

// 在C代码中不能定义函数，只能声明，可以在go中export导出函数
// 如果在这里定了函数，会导致函数定义重复错误

//int sky_pm_get_telnet_dynamic_password(char *buf, int size);
*/
import "C"
import (
	"fmt"
	//"github.com/ebitengine/purego"
)

func main() {
	fmt.Println("call sky_pm_get_telnet_dynamic_password")

	//var buf = make([]byte, 1024)
	//n := C.sky_pm_get_telnet_dynamic_password((*C.char)(unsafe.Pointer(&buf[0])), C.int(len(buf)))
	//fmt.Print("ret:", n)
	//if n > 0 {
	//	fmt.Println("buf:", buf[:n])
	//}

	//so, err := purego.Dlopen("libpmapi.so", purego.RTLD_NOW|purego.RTLD_GLOBAL)
	//if err != nil {
	//	panic(err)
	//}
	//
	//var buf = make([]byte, 1024)
	//var getTelnetDynamicPassword func(buf *byte, len int) int
	//purego.RegisterLibFunc(&getTelnetDynamicPassword, so, "sky_pm_get_telnet_dynamic_password")
	//var n = getTelnetDynamicPassword(&buf[0], len(buf))
	//fmt.Print("ret:", n)
	//if n > 0 {
	//	fmt.Println("buf:", buf[:n])
	//}
}
