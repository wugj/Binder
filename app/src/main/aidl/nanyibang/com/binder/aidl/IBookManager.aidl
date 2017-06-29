// IBookManager.aidl
package nanyibang.com.binder.aidl;

// Declare any non-default types here with import statements

import nanyibang.com.binder.aidl.Book;
import nanyibang.com.binder.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
