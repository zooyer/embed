package auth

import "testing"

func TestAuth(t *testing.T) {
	t.Log(Auth("zhangzhongyuan", "xxx"))
	t.Log(Auth("zhangzhongyuan", "386143717"))
}
