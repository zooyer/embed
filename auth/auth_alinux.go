package auth

/*
#include <shadow.h>
int auth(char *user, char *passwd){
    char *obtpwd;
    struct spwd *spasswd;

    spasswd = getspnam(user);
    obtpwd = crypt(passwd, spasswd->sp_pwdp);

    if (strcmp(spasswd->sp_pwdp, obtpwd) == 0) {
    	return 1;
    }

    return 0;
}
*/
import "C"

func auth(username, password string) bool {
	return C.auth(C.CString(username), C.CString(password)) == 0
}
