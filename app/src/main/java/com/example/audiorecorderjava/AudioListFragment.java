package com.example.audiorecorderjava;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.audiorecorderjava.adapter.AudioListAdapter;

import java.io.File;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick {

    private RecyclerView audio_list;

    private File[] audio_files;

    private AudioListAdapter audioListAdapter;


    public AudioListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audio_list = view.findViewById(R.id.audio_list_view);

        //Get all the files from the directory
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        audio_files = directory.listFiles();

        //Use a recyclerView to populate the files
        audioListAdapter = new AudioListAdapter(audio_files, this);
        audio_list.setHasFixedSize(true);
        audio_list.setLayoutManager(new LinearLayoutManager(getContext()));
        audio_list.setAdapter(audioListAdapter);

    }

    @Override
    public void onClickListener(File file, int position) {
        Log.d("PLAY", "File playing is: " + file.getName());
    }
}