// IOnNewBookArrivedListener.aidl
package nanyibang.com.binder.aidl;

import nanyibang.com.binder.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
