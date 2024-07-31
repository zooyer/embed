package main

import (
	"embed"
	"fmt"
	"io/fs"
	"os"
	"os/exec"
	"runtime"
	"slices"
	"strings"
)

//go:embed bin/arch*
var binary embed.FS

func saveFile(filename string) (err error) {
	data, err := binary.ReadFile("bin/" + filename)
	if err != nil {
		return
	}

	if err = os.WriteFile(filename, data, 0755); err != nil {
		return
	}

	return
}

func execFile(filename string) (err error) {
	var cmd = exec.Command("./" + filename)

	cmd.Stdin = os.Stdin
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	return cmd.Run()
}

func removeFile(filename string) (err error) {
	return os.Remove(filename)
}

func main() {
	dir, err := binary.ReadDir("bin")
	if err != nil {
		panic(err)
	}

	slices.SortFunc(dir, func(a, b fs.DirEntry) int {
		return strings.Compare(a.Name(), b.Name())
	})

	for _, item := range dir {
		if item.IsDir() {
			continue
		}

		var filename = item.Name()

		if !strings.Contains(filename, fmt.Sprintf("%s-%s", runtime.GOOS, runtime.GOARCH)) {
			continue
		}

		if err = saveFile(filename); err != nil {
			fmt.Println(filename+":", err.Error())
			continue
		}

		fmt.Print(filename + ": ")

		if err = execFile(filename); err != nil {
			fmt.Println(err.Error())
		}

		if err = removeFile(filename); err != nil {
			fmt.Println(filename+":", err.Error())
			continue
		}
	}
}
