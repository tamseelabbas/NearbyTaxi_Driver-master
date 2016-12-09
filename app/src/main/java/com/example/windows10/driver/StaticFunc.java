package com.example.windows10.driver;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by WINDOWS10 on 11/30/2016.
 */

public class StaticFunc {
    private static DatabaseReference mDatabase;
    public static void writeStringInFireBase(String path,String value){

        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        mDatabase.setValue(value);

    }
    public static void writeLongInFireBase(String path,Long value){

        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        mDatabase.setValue(value);

    }
}
