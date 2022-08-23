package embed

import (
	"flag"
	"os"
	"os/signal"
	"syscall"
)

var nohup = flag.Bool("nohup", false, "nohup")

func initNoSighup() {
	signals := make(chan os.Signal, 128)
	signal.Notify(signals, syscall.SIGHUP)
}
