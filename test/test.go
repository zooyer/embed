/**
 * @Author: zzy
 * @Email: zhangzhongyuan@didiglobal.com
 * @Description:
 * @File: test.go
 * @Package: test
 * @Version: 1.0.0
 * @Date: 2023/10/30 18:59
 */

package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	const password = "/Users/zhangzhongyuan/Desktop/account.txt"
	const checked = "/Users/zhangzhongyuan/Desktop/checked.txt"

	data, err := os.ReadFile(checked)
	if err != nil {
		panic(err)
	}

	var (
		lines  = strings.Split(string(data), "\r\n")
		passed = make(map[string]bool)
	)

	for _, line := range lines {
		var filed = strings.Split(line, " ")
		if filed[1] == "密码正确！" {
			passed[filed[0]] = true
		}
	}

	data, err = os.ReadFile(password)
	if err != nil {
		panic(err)
	}

	lines = strings.Split(string(data), "\r\n")
	for i := 0; i < len(lines); i += 2 {
		if passed[lines[i]] {
			fmt.Println(lines[i])
			fmt.Println(lines[i+1])
		}
	}
}
