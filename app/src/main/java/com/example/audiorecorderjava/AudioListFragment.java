package com.example.audiorecorderjava;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiorecorderjava.adapters.AudioListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick {

    private RecyclerView audio_list;

    private ConstraintLayout player_sheet;
    BottomSheetBehavior bottomSheetBehavior;
    private File[] audio_files;

    private AudioListAdapter audioListAdapter;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private File fileToPlay;

    //UI Elements
    private ImageButton btn_play;
    private TextView txt_file_name, txt_player_status;
    private SeekBar player_seek_bar;
    private Handler seekBarHandler;
    private Runnable updateSeekBar;

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

        player_sheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(player_sheet);
        audio_list = view.findViewById(R.id.audio_list_view);
        btn_play = view.findViewById(R.id.btn_play);
        txt_file_name = view.findViewById(R.id.txt_file_name);
        txt_player_status = view.findViewById(R.id.txt_player_status);
        player_seek_bar = view.findViewById(R.id.player_seek_bar);


        //Get all the files from the directory
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        audio_files = directory.listFiles();

        //Use a recyclerView to populate the files
        audioListAdapter = new AudioListAdapter(audio_files, this);
        audio_list.setHasFixedSize(true);
        audio_list.setLayoutManager(new LinearLayoutManager(getContext()));
        audio_list.setAdapter(audioListAdapter);

        btn_play.setOnClickListener(v -> {
            if (fileToPlay != null){
                if (isPlaying){
                    pauseAudio();
                }
                else{
                    resumeAudio();
                }
            }
        });

        player_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (fileToPlay != null) {
                    if (isPlaying){
                        pauseAudio();
                        resumeAudio();
                    }
                    else {
                        pauseAudio();
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (fileToPlay != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    if (isPlaying){
                        resumeAudio();
                    }
                }
            }
        });
    }

    @Override
    public void onClickListener(File file, int position) {
        Log.d("PLAY", "File playing is: " + file.getName());
        fileToPlay = file;
        if (isPlaying){
            pauseAudio();
        }
        playAudio(fileToPlay);
    }

    private void resumeAudio() {
        isPlaying = true;
        btn_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.player_pause_btn));
        mediaPlayer.start();
    }

    private void pauseAudio() {
        isPlaying = false;
        btn_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.player_play_btn));
        txt_player_status.setText("Paused");
        mediaPlayer.pause();
    }

    private void playAudio(File fileToPlay) {

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //btn_play.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn));
        btn_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.player_pause_btn));
        txt_file_name.setText(fileToPlay.getName());
        txt_player_status.setText("Playing");

        isPlaying = true;

        mediaPlayer.setOnCompletionListener(mp -> {
            pauseAudio();
            txt_player_status.setText("Finished");
        });

        //Add SeekBar Syncing
        player_seek_bar.setMax(mediaPlayer.getDuration());

        seekBarHandler = new Handler();
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                player_seek_bar.setProgress(mediaPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this, 250);
            }

        };
        seekBarHandler.postDelayed(updateSeekBar, 0);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null){
            isPlaying = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            Toast.makeText(getContext(), "MediaPlayer stopped", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}