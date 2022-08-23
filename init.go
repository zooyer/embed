package embed

import "flag"

func Init() {
	if !flag.Parsed() {
		flag.Parse()
	}

	if *nohup {
		initNoSighup()
	}
}

func init() {
	//Init()
}
