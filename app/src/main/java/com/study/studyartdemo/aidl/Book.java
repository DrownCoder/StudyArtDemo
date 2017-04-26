package com.study.studyartdemo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dengzhaoxuan on 2017/4/20.
 */

public class Book implements Parcelable{
    private int bookId;
    private String bookName;

    public Book(int bookId, String booName) {
        this.bookId = bookId;
        this.bookName = booName;
    }
    protected Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }
}
