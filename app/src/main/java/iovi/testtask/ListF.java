package iovi.testtask;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Iovi on 02.09.2015.
 */
public class ListF extends ListFragment {


    final static String TABLE="table";
    final static String COLUMN="column";
    final static String SELECTION="selection";
    final static String ARGS="args";

    private static String TAG="TestTask";
    private DataBaseHelper dbHelper;
    private String title;



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        String data[]=MakeOutputArray(
                 getArguments().getString(TABLE),
                 getArguments().getString(COLUMN),
                 getArguments().getString(SELECTION),
                 getArguments().getStringArray(ARGS)
         );


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.list_item,data);
        setListAdapter(adapter);
}

    @Override
    public void onResume(){
        super.onResume();
            getActivity().setTitle(getArguments().getString("Title"));
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        Fragment fragment=new ListF();
        Bundle args = new Bundle();
        switch (getArguments().getString(TABLE)){
            case DataBaseHelper.TABLE_ORGANISATIONS:
                args.putString(TABLE,DataBaseHelper.TABLE_DEPARTMENTS);
                args.putString(COLUMN, DataBaseHelper.NAME);
                args.putString(SELECTION, DataBaseHelper.ORG_ID + "=?");

                args.putStringArray(ARGS,
                    new String[]{String.valueOf(position)}
                );
            break;

            case DataBaseHelper.TABLE_DEPARTMENTS:
                fragment=new TextF();
                String descr[]=MakeOutputArray(
                        DataBaseHelper.TABLE_DEPARTMENTS,
                        DataBaseHelper.DESCRIPTION,
                        DataBaseHelper.NAME + "=?",
                        new String[]{(String) l.getItemAtPosition(position)});

                args.putString("Text", descr[0]);
            break;

            case DataBaseHelper.TABLE_NEWS:
                fragment=new TextF();

                String text[]=MakeOutputArray(
                        DataBaseHelper.TABLE_NEWS,
                        DataBaseHelper.NEWS_TEXT,
                        DataBaseHelper.NEWS_TITLE + "=?",
                        new String[]{(String) l.getItemAtPosition(position)});
                args.putString("Text", text[0]);
            break;


        }

        args.putString("Title",(String) l.getItemAtPosition(position));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String [] MakeOutputArray(String tableName, String columnName, String selection, String[] args){


        dbHelper=new DataBaseHelper(getActivity());
        try {

            dbHelper.createDataBase();

        } catch (IOException ioe) {

            Toast.makeText(getActivity(), ioe.getMessage(), Toast.LENGTH_SHORT).show();

        }

        try {

            dbHelper.openDataBase();

        }catch(SQLException sqle){

            Toast.makeText(getActivity(),sqle.getMessage(),Toast.LENGTH_SHORT).show();

        }

        Cursor cursor=dbHelper.getReadableDatabase().query(tableName,null,selection,args,null,null,null);
        String outputArray[]=new String[cursor.getCount()];
        for(int i=0;cursor.moveToNext();i++){
            outputArray[i] = cursor.getString(cursor.getColumnIndex(columnName));
        }
        return outputArray;
    }

}
