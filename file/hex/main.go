package main

import (
	"encoding/hex"
	"fmt"
	"os"
	"strings"
)

func main() {
	const filename = "/Users/zhangzhongyuan/Desktop/session 2.log"
	const output = "/Users/zhangzhongyuan/Desktop/jffs2.img"

	data, err := os.ReadFile(filename)
	if err != nil {
		panic(err)
	}

	var rawLines = strings.Split(string(data), "\r\n")
	fmt.Println("raw lines:", len(rawLines))

	var lines = make([]string, 0, 1024)
	for i, line := range rawLines {
		if line == "" {
			continue
		}

		var index = strings.IndexByte(line, ':')
		if index < 0 {
			continue
		}

		line = strings.ReplaceAll(line[index+2:index+53], " ", "")
		//fmt.Println(line)
		if i > 5 {
		}
		lines = append(lines, line)
	}

	fmt.Println("lines:", len(lines))

	var dataString = strings.Join(lines, "")

	fmt.Println("data string len:", len(dataString))
	fmt.Println("demo string:", dataString[:1024])

	if data, err = hex.DecodeString(dataString); err != nil {
		panic(err)
	}

	fmt.Println("len:", len(data))

	if err = os.WriteFile(output, data, 0644); err != nil {
		panic(err)
	}
}
