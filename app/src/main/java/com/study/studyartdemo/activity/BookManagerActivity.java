package com.study.studyartdemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.study.studyartdemo.R;
import com.study.studyartdemo.aidl.Book;
import com.study.studyartdemo.aidl.IBookManager;
import com.study.studyartdemo.aidl.IOnNewBookArrivedListener;
import com.study.studyartdemo.service.BookManagerService;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {
    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "receive new book:" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    /**
     * 错误的写法
     */
    /*private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };*/
    /**
     * 正确的写法
     */
    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "query book list, list type:" + list.getClass().getCanonicalName());
                Book newBook = new Book(3, "Android开发艺术探索");
                bookManager.addBook(newBook);
                Log.i(TAG, "addBook" + newBook);
                List<Book> newList = bookManager.getBookList();
                Log.i(TAG, "query book list :" + newList.size());
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.e(TAG, "binder died.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null
                && mRemoteBookManager.asBinder().isBinderAlive()) {
            Log.i(TAG, "unregister listener:" + mOnNewBookArrivedListener);
            try {
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
