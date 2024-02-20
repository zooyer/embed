/**
 * @Author: zzy
 * @Email: zhangzhongyuan@didiglobal.com
 * @Description:
 * @File: b.go
 * @Package: b
 * @Version: 1.0.0
 * @Date: 2023/12/27 03:03
 */

package b

import (
	_ "github.com/zooyer/embed/linkname/a"
	_ "unsafe"
)

//go:linkname b github.com/zooyer/embed/linkname/a.a
func b()

//go:linkname num github.com/zooyer/embed/linkname/a.num
var num int

func B() {
	b()
	num = 1024
}
