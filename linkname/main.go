/**
 * @Author: zzy
 * @Email: zhangzhongyuan@didiglobal.com
 * @Description:
 * @File: main.go
 * @Package: linkname
 * @Version: 1.0.0
 * @Date: 2023/12/27 03:02
 */

//package main
//
//import (
//	"fmt"
//	_ "net"
//	_ "unsafe"
//)
//
////go:linkname lookupStaticHost net.lookupStaticHost
//func lookupStaticHost(host string) []string
//
//func main() {
//	fmt.Println(lookupStaticHost("localhost"))
//}

package main

import (
	"github.com/zooyer/embed/linkname/a"
	"github.com/zooyer/embed/linkname/b"
)

func main() {
	b.B()
	a.PrintA()
}
