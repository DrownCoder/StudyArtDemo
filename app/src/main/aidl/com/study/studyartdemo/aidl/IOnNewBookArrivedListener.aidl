// IOnNewBookArrivedListener.aidl
package com.study.studyartdemo.aidl;

// Declare any non-default types here with import statements
import com.study.studyartdemo.aidl.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
