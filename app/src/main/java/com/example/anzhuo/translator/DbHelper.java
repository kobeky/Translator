package com.example.anzhuo.translator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class DbHelper extends SQLiteOpenHelper{
    static final String DB_NAME="history.db";
    private static final String TBL_NAME="Result";
    private static final int VERSION=1;
    private static final String CREATE_TBL=" create table "
            +" Result(_id integer primary key autoincrement,tv1 text,tv2 text,collection integer)";
    private SQLiteDatabase db;
    public DbHelper(Context context) {
        super(context, DB_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       this.db=db;
        db.execSQL(CREATE_TBL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(ContentValues values)
    {
        //获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //插入
        db.insert(TBL_NAME, null, values);
        //关闭
        db.close();
    }

    public Cursor query(String dbName, Object o, Object o1, Object o2, Object o3, Object o4, Object o5)
    {
        //获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //查询获得Cursor
        Cursor c=db.query(TBL_NAME, null, null, null, null, null, null);
        return c;
    }

    public void delete()
    {
        if(db==null)
        {
            //获得SQLiteDatabase实例
            db=getWritableDatabase();
        }
        //执行删除
        db.delete(TBL_NAME, null, null);
    }

    public void update(TranslateInfo translateInfo){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("collection",translateInfo.getCollection());
        db.update(TBL_NAME,contentValues,"tv1 = ?",new String[]{translateInfo.getTv1().toString()});
    }
}
