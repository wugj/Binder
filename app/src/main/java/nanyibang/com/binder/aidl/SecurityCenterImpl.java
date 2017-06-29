package nanyibang.com.binder.aidl;

import android.os.RemoteException;

/**
 * @name: nanyibang.com.binder.aidl
 * @description:
 * @author：Administrator
 * @date: 2017-06-29 15:28
 * @company: 上海若美科技有限公司
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub {

    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
