/**
 * @Author: zzy
 * @Email: zhangzhongyuan@didiglobal.com
 * @Description:
 * @File: dns_test.go
 * @Package: dns
 * @Version: 1.0.0
 * @Date: 2023/12/27 02:55
 */

package dns

import "testing"

func TestHook(t *testing.T) {
	var err = Hook("/etc/resolve.conf")
	if err != nil {
		t.Fatal(err)
	}
}
