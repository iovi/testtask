package iovi.testtask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Iovi on 05.09.2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/iovi.testtask/databases/";
    private static String DB_NAME = "testtask_db";

    final static String TABLE_ORGANISATIONS="organisations";
    final static String TABLE_DEPARTMENTS="departments";
    final static String TABLE_NEWS="news";
    final static String ID="_id";
    final static String ORG_ID="organisation_id";
    final static String NAME="name";
    final static String DESCRIPTION="description";
    final static String NEWS_TITLE="title";
    final static String NEWS_TEXT="text";


    private SQLiteDatabase db;

    private final Context context;


    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.context = context;
    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String dbPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{

        InputStream input = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream output = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer))>0){
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();

    }

    public void openDataBase() throws SQLException {

        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(db != null) db.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
