package com.example.renato.trabalho1mobile1application;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_VIDEO_CAPTURE = 2;

    private ImageView imageView;
    private VideoView videoView;
    public Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == PICK_IMAGE_REQUEST || requestCode == CAMERA_REQUEST || requestCode == REQUEST_VIDEO_CAPTURE) && resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                this.videoUri = data.getData();
                imageView.setVisibility(imageView.INVISIBLE);
                videoView.setVisibility(videoView.VISIBLE);
//                videoView.start();
            }else {

                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(imageView.VISIBLE);
                        videoView.setVisibility(videoView.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    imageView.setVisibility(imageView.VISIBLE);
                    videoView.setVisibility(videoView.INVISIBLE);
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // primeiro cria a intenção
        Intent it = new Intent("EXECUTAR_ALARME");
        PendingIntent p = PendingIntent.getBroadcast(MainActivity.this, 0, it, 0);

        // cancela o amarme
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.cancel(p);
    }

    public void clickCamera(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void clickVideo(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"), "video/mp4");
        videoView.getContext().startActivity(intent);
    }
    public void clickMap(View v){
        double latitude =  -3.7319;
        double longitude = -38.5267;
        String label = "Perto do Centro de Fortaleza-CE";
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void clickPhone(View v){
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:"));
        startActivity(dial);
    }

    public void clickCalendario(View v){
        long startMillis = System.currentTimeMillis();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        startActivity(intent);
    }

    public void clickGoogle(View v){
        String endereco = "http://www.google.com.br";
        Uri uri = Uri.parse(endereco);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);;
    }


    public void clickAlarme(View view) {
        // primeiro cria a intenção
        Intent it = new Intent("EXECUTAR_ALARME");
        PendingIntent p = PendingIntent.getBroadcast(MainActivity.this, 0, it, 0);

        // precisamos pegar agora + 10segundos
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 10); // +10 segundos

        // agendar o alarme
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = c.getTimeInMillis();
        alarme.set(AlarmManager.RTC_WAKEUP, time, p);
        Toast.makeText(this, "Alarme Agendado!", Toast.LENGTH_SHORT).show();
    }

    public void clickMensagem(View view) {
        Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
        intentsms.putExtra( "sms_body", "Renato é lindo!!!" );
        startActivity( intentsms );
    }

    public void clickAlbum(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void clickGravarVideo(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    public void playVideo(View v){
        videoView.setVideoURI(this.videoUri);
        videoView.requestFocus();
        videoView.start();
    }
}
