// IBinderPool.aidl
package nanyibang.com.binder.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
