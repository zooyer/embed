package log

import (
	"fmt"
	"io"
	"net"
	"os"
	"strings"
	"sync"
	"syscall"
)

type Config struct {
	Enable bool
	Color  bool
	Level  string
	Output string // stdout/stderr/ip:port
}

const (
	traceLevel = "TRACE"
	debugLevel = "DEBUG"
	infoLevel  = "INFO"
	warnLevel  = "WARN"
	errorLevel = "ERROR"
)

var (
	mutex      sync.Mutex
	writer     io.WriteCloser
	config     *Config
	level      = make(map[string]int)
	levelIndex = []string{
		traceLevel,
		debugLevel,
		infoLevel,
		warnLevel,
		errorLevel,
	}
)

func init() {
	for i, lv := range levelIndex {
		level[lv] = i
	}

	config = &Config{
		Enable: true,
		Color:  true,
		Level:  "TRACE",
		Output: "stdout",
	}
}

func ignore(lv string) bool {
	levelIndex, exists := level[strings.ToUpper(config.Level)]
	if !exists {
		return false
	}

	return level[lv] < levelIndex
}

func client() io.WriteCloser {
	mutex.Lock()
	defer mutex.Unlock()
	var err error

	// 变更输出，关闭writer
	switch w := writer.(type) {
	case *os.File:
		if w.Name() != config.Output {
			w.Close()
			w = nil
		}
	case net.Conn:
		if w.RemoteAddr().String() != config.Output {
			w.Close()
			w = nil
		}
	}

	// 打开writer
	if writer == nil {
		switch config.Output {
		case "":
			return nil
		case "stdout":
			return os.NewFile(uintptr(syscall.Stdout), "stdout")
		case "stderr":
			return os.NewFile(uintptr(syscall.Stdout), "stderr")
		default:
			if !strings.HasPrefix(config.Output, "tcp") && !strings.HasPrefix(config.Output, "udp") {
				break
			}

			if !strings.Contains(config.Output, "://") {
				break
			}

			fields := strings.Split(config.Output, "://")
			if len(fields) != 2 {
				break
			}

			if writer, err = net.Dial(fields[0], fields[1]); err != nil {
				return nil
			}

			return writer
		}

		if writer, err = os.OpenFile(config.Output, os.O_RDWR|os.O_CREATE|os.O_APPEND, 0644); err != nil {
			return nil
		}
	}

	return writer
}

func cyan(s string) string {
	return fmt.Sprintf("\033[1;36m%s\033[0m", s)
}

func green(s string) string {
	return fmt.Sprintf("\033[1;32m%s\033[0m", s)
}

func blue(s string) string {
	return fmt.Sprintf("\033[1;34m%s\033[0m", s)
}

func purple(s string) string {
	return fmt.Sprintf("\033[1;35m%s\033[0m", s)
}

func red(s string) string {
	return fmt.Sprintf("\033[1;31m%s\033[0m", s)
}

func output(v ...interface{}) {
	if !config.Enable {
		if writer != nil {
			writer.Close()
			writer = nil
		}
		return
	}

	if writer = client(); writer == nil {
		return
	}
	if _, err := writer.Write([]byte(fmt.Sprint(v...))); err != nil {
		writer.Close()
		writer = nil
	}
}

func ZTrace(in ...interface{}) func(v ...interface{}) {
	if ignore(traceLevel) {
		return func(v ...interface{}) {}
	}

	var tag = "[Z-TRACE-IN]"
	var color = config.Color
	if color {
		tag = cyan(tag)
	}

	output(tag+" ", fmt.Sprintln(in...))

	return func(v ...interface{}) {
		var tag = "[Z-TRACE-OUT]"
		if color {
			tag = cyan(tag)
		}

		output(tag+" ", fmt.Sprintln(v...))
	}
}

func ZDebug(v ...interface{}) {
	if ignore(debugLevel) {
		return
	}

	var tag = "[Z-DEBUG]"
	if config.Color {
		tag = green(tag)
	}
	output(tag+" ", fmt.Sprintln(v...))
}

func ZInfo(v ...interface{}) {
	if ignore(infoLevel) {
		return
	}

	var tag = "[Z-INFO]"
	if config.Color {
		tag = blue(tag)
	}
	output(tag+" ", fmt.Sprintln(v...))
}

func ZWarn(v ...interface{}) {
	if ignore(warnLevel) {
		return
	}

	var tag = "[Z-WARN]"
	if config.Color {
		tag = purple(tag)
	}
	output(tag+" ", fmt.Sprintln(v...))
}

func ZError(v ...interface{}) {
	if ignore(errorLevel) {
		return
	}

	var tag = "[Z-ERROR]"
	if config.Color {
		tag = red(tag)
	}
	output(tag+" ", fmt.Sprintln(v...))
}

func Init(conf *Config) {
	if conf == nil {
		return
	}

	config = conf
}
