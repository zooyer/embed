package log

import (
	"testing"
)

func TestLog(t *testing.T) {
	defer ZTrace("追踪入")("追踪出")
	ZDebug("调试")
	ZInfo("信息")
	ZWarn("警告")
	ZError("错误")
}
