package com.example.audiorecorderjava.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiorecorderjava.R;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private File[] audio_files;

    //Create another adapter for receiving all the files
    public AudioListAdapter(File[] audio_files){
        this.audio_files = audio_files;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.audio_title.setText(audio_files[position].getName());
        holder.audio_date.setText(audio_files[position].lastModified() + "");

    }

    @Override
    public int getItemCount() {
        return audio_files.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder{

        private ImageView audio_img;
        private TextView audio_title, audio_date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            audio_img = itemView.findViewById(R.id.img_audio_list);
            audio_date = itemView.findViewById(R.id.audio_date);
            audio_title = itemView.findViewById(R.id.audio_title);
        }
    }
}
