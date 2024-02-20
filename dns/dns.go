/**
 * @Author: zzy
 * @Email: zhangzhongyuan@didiglobal.com
 * @Description:
 * @File: dns.go
 * @Package: dns
 * @Version: 1.0.0
 * @Date: 2023/12/27 02:39
 */

package dns

import (
	_ "net"
	"os"
	"sync"
	"sync/atomic"
	"time"
	_ "unsafe"
)

type dnsConfig struct {
	servers       []string      // server addresses (in host:port form) to use
	search        []string      // rooted suffixes to append to local name
	ndots         int           // number of dots in name to trigger absolute lookup
	timeout       time.Duration // wait before giving up on a query, including retries
	attempts      int           // lost packets before giving up on server
	rotate        bool          // round robin among servers
	unknownOpt    bool          // anything unknown was encountered
	lookup        []string      // OpenBSD top-level database "lookup" order
	err           error         // any error that occurs during open of resolv.conf
	mtime         time.Time     // time of resolv.conf modification
	soffset       uint32        // used by serverOffset
	singleRequest bool          // use sequential A and AAAA queries instead of parallel queries
	useTCP        bool          // force usage of TCP for DNS resolutions
	trustAD       bool          // add AD flag to queries
	noReload      bool          // do not check for config file updates
}

// A resolverConfig represents a DNS stub resolver configuration.
type resolverConfig struct {
	initOnce sync.Once // guards init of resolverConfig

	// ch is used as a semaphore that only allows one lookup at a
	// time to recheck resolv.conf.
	ch          chan struct{} // guards lastChecked and modTime
	lastChecked time.Time     // last time resolv.conf was checked

	dnsConfig atomic.Pointer[dnsConfig] // parsed resolv.conf structure used in lookups
}

//go:linkname resolvConf net.resolvConf
var resolvConf resolverConfig

//go:linkname dnsReadConfig net.dnsReadConfig
func dnsReadConfig(filename string) *dnsConfig

func Hook(filename string) (err error) {
	if _, err = os.Stat(filename); err != nil {
		return
	}

	resolvConf.dnsConfig.Store(dnsReadConfig(filename))

	return err
}
