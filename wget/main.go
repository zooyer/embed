/**
 * @Author: zzy
 * @Email: zhangzhongyuan@didiglobal.com
 * @Description:
 * @File: main.go
 * @Package: wget
 * @Version: 1.0.0
 * @Date: 2022/11/21 14:53
 */

package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
)

func help() {
	fmt.Println("usage: wget url filename")
	fmt.Println()
}

func main() {
	if len(os.Args) < 3 {
		help()
		return
	}

	resp, err := http.Get(os.Args[1])
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()

	data, err := io.ReadAll(resp.Body)
	if err != nil {
		panic(err)
	}

	if err = os.WriteFile(os.Args[2], data, 0644); err != nil {
		panic(err)
	}
}
