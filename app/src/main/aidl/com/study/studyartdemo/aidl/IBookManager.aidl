// IBookManager.aidl
package com.study.studyartdemo.aidl;

// Declare any non-default types here with import statements
import com.study.studyartdemo.aidl.Book;
import com.study.studyartdemo.aidl.IOnNewBookArrivedListener;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
