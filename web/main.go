package main

import (
	"flag"
	"fmt"

	"github.com/gin-gonic/gin"
)

var port = flag.Int("port", 80, "listen port")

func init() {
	flag.Parse()
}

func main() {
	engine := gin.Default()
	engine.Static("/", "./")
	engine.Run(fmt.Sprintf(":%d", *port))
}
