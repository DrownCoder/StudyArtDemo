package com.study.studyartdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.study.studyartdemo.aidl.Book;
import com.study.studyartdemo.aidl.IBookManager;
import com.study.studyartdemo.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dengzhaoxuan on 2017/4/25.
 */

public class BookManagerService extends Service {
    private static final String TAG = "BMS";
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList =
            new CopyOnWriteArrayList<>();
    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (!mListenerList.contains(listener)) {
                mListenerList.add(listener);
            }else{
                Log.d(TAG, "already exists");
            }
            Log.d(TAG, "registerListener, size:" + mListenerList.size());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (mListenerList.contains(listener)) {
                mListenerList.remove(listener);
                Log.d(TAG, "unregister listener succed.");
            }else{
                Log.d(TAG, "not found, can not unregister.");
            }
            Log.d(TAG, "unregisterListener, current size:" + mListenerList.size());
        }
    };

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "I"));
        new Thread(new ServiceWorker()).start();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                onNewBookArrived(newBook);
            }
        }
    }

    private void onNewBookArrived(Book newBook) {
        mBookList.add(newBook);
        Log.d(TAG, "onNewBookArrived, notify listeners:" + mListenerList.get(0));
        /*for (IOnNewBookArrivedListener item : mListenerList) {
            Log.d(TAG, "onNewBookArrived, notify listener:" + item);
            try {
                item.onNewBookArrived(newBook);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }*/
        for(int i = 0;i<mListenerList.size();i++) {
            IOnNewBookArrivedListener listener = mListenerList.get(i);
            Log.d(TAG, "onNewBookArrived, notify listener:" + listener);
        }
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }
}
