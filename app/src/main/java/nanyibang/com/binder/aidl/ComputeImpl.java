package nanyibang.com.binder.aidl;

import android.os.RemoteException;

/**
 * @name: nanyibang.com.binder.aidl
 * @description:
 * @author：Administrator
 * @date: 2017-06-29 15:29
 * @company: 上海若美科技有限公司
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
