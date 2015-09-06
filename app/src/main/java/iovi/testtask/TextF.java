package iovi.testtask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by Iovi on 06.09.2015.
 */
public class TextF extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.text_info, null);
        ((TextView)v.findViewById(R.id.text_information)).setText(getArguments().getString("Text"));
        return v;
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getArguments().getString("Title"));
    }

}
