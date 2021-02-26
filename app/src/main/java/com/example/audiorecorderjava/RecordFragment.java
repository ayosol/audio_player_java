package com.example.audiorecorderjava;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_CODE = 100;

    private NavController navController;

    private ImageButton btn_record_list, btn_record;
    private TextView txt_filename;
    private boolean isRecording = false;

    private MediaRecorder mediaRecorder;
    private String record_file;

    private Chronometer timer;


    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btn_record_list = view.findViewById(R.id.btn_record_list);
        btn_record = view.findViewById(R.id.btn_record);
        txt_filename = view.findViewById(R.id.record_filename);
        timer = view.findViewById(R.id.record_timer);

        btn_record_list.setOnClickListener(this);
        btn_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_record_list:
                navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                break;
            case R.id.btn_record:
                if (isRecording){
                    //Stop recording
                    stopRecording();
                    btn_record.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    isRecording = false;
                }
                else {
                    //Start recording
                    if (checkPermissions()) {
                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        startRecording();
                        isRecording = true;
                        Toast.makeText(getContext(), "Recording started", Toast.LENGTH_LONG).show();

                    }
                }
                break;

        }
    }

    private void stopRecording() {
        //Stop timer
        timer.stop();

        txt_filename.setText("Recording stopped, File Saved as " + record_file);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(getContext(), "Recording stopped", Toast.LENGTH_LONG).show();
    }

    private void startRecording() {
        //Start timer
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //Create FilePath
        String record_path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault());
        Date date = new Date();
        record_file = "recording" + dateFormat.format(date) + ".3gp";
        txt_filename.setText("Recording, File name: " + record_file);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(record_path + "/" + record_file);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaRecorder != null){
            stopRecording();
            Toast.makeText(getContext(), "Recording stopped", Toast.LENGTH_LONG).show();
        }
    }
}