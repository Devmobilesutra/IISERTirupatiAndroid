



package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.AppConstant;
import com.mobilesutra.iiser_tirupati.Config.Base64;
import com.mobilesutra.iiser_tirupati.Config.DocumentType;
import com.mobilesutra.iiser_tirupati.Config.FileUtils;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sony on 11/04/2017.
 */
public class ActivityAddAssignment extends Activity {

    static String LOG_TAG = "ActivityAddAssignment";
    String filename_image = "";
    String str_image_path = "";
    Context context = null;
    public static String new_str_path = "", img_path = "", photoString = "", audio_file = "", audio_file_name = "",
            audio_file_path = "", sec_time1 = "", str = "", str_file_name = "", str_file_path = "", course= "";
    ByteArrayOutputStream compressphotobytearrray;
    MediaRecorder recorder;
    int audio_duration2 = 60000;
    public static Timer recording_timer;
    public static MediaPlayer player_ask;
    int curTime = 0, newTime = 0, newTime1 = 0;
    int flag1 = 0, flag2 = 0, flag3 = 0, flag5 = 5;
    int finalTime = 0, finalTime1 = 0;
    String selected_date = "", display_date = "";
    CharSequence assignment_text = "";
    public static SimpleDateFormat dFyyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    ProgressDialog dialog = null;
    Spinner spnr_course1 = null;
    List<LinkedHashMap<String, String>> spnr_course=null;

    EditText edit_answer = null;
    Button image_capture = null, btn_submit = null, record_audio = null, play_btn = null, delete_audio = null;
    ImageView display_camera_image = null;
    SeekBar seek_bar = null;
    TextView timer, timer2, txtvw_assign_date, txt_file_name = null, txt_document = null;

    File img_file;
    int PERMISSION_REQUEST_DOCUMENT = 2, PERMISSION_REQUEST_CAMERA = 1;
    Boolean flag_isCamera = false;
    String str_byte_string="",str_doc_string="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_add_assignment);
        IISERApp.log(LOG_TAG, "in activity AddAssignment");

        context = this;
        getIntentData();
        initComponents();
        initComponentsListeners();
        bindComponentData();
    }

    private void getIntentData() {
    }

    private void initComponents() {
        edit_answer = (EditText) findViewById(R.id.edit_answer);
        image_capture = (Button) findViewById(R.id.image_capture);
        display_camera_image = (ImageView) findViewById(R.id.display_camera_image);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        record_audio = (Button) findViewById(R.id.record_audio);
        //record_audio.setVisibility(View.GONE);
        play_btn = (Button) findViewById(R.id.play_btn);
      //  play_btn.setVisibility(View.GONE);
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        //seek_bar.setVisibility(View.GONE);
        timer = (TextView) findViewById(R.id.texttime);
        timer2 = (TextView) findViewById(R.id.texttime2);
        spnr_course1= (Spinner) findViewById(R.id.edtxt_course);


        delete_audio = (Button) findViewById(R.id.delete_audio);
        txtvw_assign_date = (TextView) findViewById(R.id.txtvw_assign_date);
        txt_file_name = (TextView) findViewById(R.id.txt_file_name);
        txt_file_name.setText(str_file_name);
        txt_document = (TextView) findViewById(R.id.txt_document);
    }

    private void initComponentsListeners() {




      /*  String courses = ((IISERApp) getApplication()).db.get_courses_spnr();
       //IISERApp.logi(LOG_TAG, "In lhmBeat_List" + Beat_List);
        courses = new String[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            courses[i] = courses.get(i).get("Courses are");

        }

        ArrayAdapter<String> courses_spinner = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, spnr_course);
        courses_spinner.setDropDownViewResource(R.layout.spinner_properties);
        spinner_beat.setAdapter(courses_spinner);
*/

///////////to take  from database and set to spinner

        spnr_course = TABLE_TIMETABLE.get_courses_spnr();
        String[] course = null;
        IISERApp.log(LOG_TAG, "In course_List" + spnr_course);
        course = new String[spnr_course.size()];
        for (int i = 0; i < spnr_course.size(); i++) {
            course[i] = spnr_course.get(i).get("course_id");

        }
        ArrayAdapter semester_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, course);
        semester_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_course1.setAdapter(semester_adapter);

        /*ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,R.id.,course);
        adapter4.setDropDownViewResource(R.id.spnr_course1);
        spnr_course1.setAdapter(adapter4);
*/


        edit_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                assignment_text = charSequence;
                // Log.e("tag", "Question_replay" + value);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtvw_assign_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_date();
            }
        });
        txt_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(txt_document, "Documents Allowed: csv,pdf,xlsx,xls,txt,rtf,docx,doc,pptx,ppt",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestForDocument();
                        // txt_file_name.setText(str_file_name);
                    }
                }).show();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generate_image_byte_string();
                //pass data given below like photostring,assignment_text,assignment date,


                IISERApp.log(LOG_TAG, "photoString:" + str_byte_string);
                IISERApp.log(LOG_TAG, "assignment_text:" + assignment_text);
                IISERApp.log(LOG_TAG, "assignment_date:" + txtvw_assign_date.getText().toString());
                IISERApp.log(LOG_TAG, "document_file:" + str_file_path);
                IISERApp.log(LOG_TAG, "audio_file:" + audio_file_name);
                IISERApp.log(LOG_TAG, "mime_type is:" + IISERApp.get_session(IISERApp.SESSION_MIME_TYPE));
                if (audio_file.contains(".mp3")) {
                    Log.e("Socket", "ReturnFile sound" + audio_file);

                    if (((IISERApp) getApplication()).isInternetAvailable()) {
                        HttpAudio httpAudio = new HttpAudio();
                        httpAudio.execute();


                    } else
                        getdialog_showResponse(context, getString(R.string.internet_problem));

                } else
                    audio_file = "";

                IISERApp.log(LOG_TAG,"audio_file:"+audio_file);

                if (((IISERApp) getApplication()).isInternetAvailable()) {
                    HttpDocument httpDocument = new HttpDocument();
                    httpDocument.execute();


                } else
                    getdialog_showResponse(context, getString(R.string.internet_problem));
                if (((IISERApp) getApplication()).isInternetAvailable()) {
                    Bundle bundle = new Bundle();
                    Intent intent1 = new Intent(context, IISERIntentService.class);
                    bundle.putString("course_id", "CHM121");
                    IISERApp.set_session(IISERApp.SESSION_IMAGE_STRING, str_byte_string);
                    // bundle.putString("photoString", str_byte_string);
                    bundle.putString("submitted_date", txtvw_assign_date.getText().toString());
                    bundle.putString("assignment_text", assignment_text.toString());
                    // bundle.putString("document_file", str_file_path);
                    IISERApp.set_session(IISERApp.SESSION_DOCUMENT, str_doc_string);
                    bundle.putString("audio_file", audio_file_name);

                    // Intent intent1 = new Intent(context, IISERIntentService.class);

                    bundle.putString(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_SEND_ASSIGNMENT);
                    //intent1.putExtras(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_SEND_ASSIGNMENT);
                    intent1.putExtras(bundle);
                    dialog = ProgressDialog.show(context, "IISER", "Please Wait... Sending assignment to server");
                    context.startService(intent1);


                } else {
                    Snackbar.make(btn_submit, "No Internet available...", Snackbar.LENGTH_LONG).show();
                }


            }
        });


        image_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IISERApp.log(LOG_TAG, "Image Clicked");
                String current_date = ((IISERApp) getApplication()).get_current_date();
                IISERApp.log(LOG_TAG, "current date is: " + current_date);
                try {
                    IISERApp.log(LOG_TAG, "In try");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File imageStorageDir = new File(Environment.getExternalStorageDirectory(), "Agreeta");
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }
                    filename_image = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
//                    str_name=str_name + ".png";
                    str_image_path = imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

                    IISERApp.log(LOG_TAG, "image path is:" + str_image_path);
                    img_file = new File(str_image_path);


                    IISERApp.log(LOG_TAG, "Image STRING IS : "+str_byte_string);

                    IISERApp.log(LOG_TAG, "image file is:" + img_file);
                  //  IISERApp.set_session(IISERApp.SESSION_IMG_PATH, str_image_path);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img_file));

                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    IISERApp.log(LOG_TAG, "In catch");
                    Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);

                }
            }
        });

























       /* image_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IISERApp.log(LOG_TAG, "Image Clicked");

                String current_date = ((IISERApp) getApplication()).get_current_date();
                IISERApp.log(LOG_TAG, "current date is: " + current_date);
                try {
                    IISERApp.log(LOG_TAG, "In try");
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File imageStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstant.PHOTO_ALBUM);
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }
                    filename_image = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    str_image_path = imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    img_file = new File(str_image_path);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img_file));


                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    IISERApp.log(LOG_TAG, "In catch");

                    Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);
                }

            }
        });*/

        record_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IISERApp.log(LOG_TAG, "on click record_audio");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityAddAssignment.this);
                if (flag5 == 5) {
                    alertDialog.setTitle("Audio Record ");
                    alertDialog.setMessage("Please audio record your assignment in 1 minute. and click stop button once complete.");
                    alertDialog.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (flag1 == 0) {
                                flag3 = 3;
                                recorder = new MediaRecorder();
                                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                recorder.setOutputFile(get_audioFilePath());
                                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                                recorder.setMaxDuration(audio_duration2);
                                seek_bar.setMax(audio_duration2);
                                try {
                                    play_btn.setVisibility(View.VISIBLE);
                                    seek_bar.setVisibility(View.VISIBLE);
                                    timer.setVisibility(View.VISIBLE);
                                    timer2.setVisibility(View.VISIBLE);
                                    recording_timer = new Timer();
                                    recording_timer.schedule(new Updater(seek_bar, timer, audio_duration2, record_audio), 1000, 1000);
                                    timer2.setText(formatMilliSecondsToTime(audio_duration2));
                                    recorder.prepare();
                                    recorder.start();
                                    seek_bar.setVisibility(View.VISIBLE);
                                    record_audio.setBackgroundResource(R.drawable.audioredbutten);

                                    flag5 = 6;
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                                flag1 = 1;
                                getdialog_showResponse(getApplicationContext(), "Audio Record Start !");


                            } else {

                                if (flag1 == 1 && recorder != null) {
                                    try {
                                        //	AudioPlay.setVisibility(View.VISIBLE);
                                        //	check_Audio.setVisibility(View.VISIBLE);
                                        //Mic.setBackgroundResource(R.drawable.new_mic_box_green);
                                        recorder.stop();
                                        recording_timer.cancel();
                                        record_audio.setBackgroundResource(R.drawable.audiogreenbutten);
                                        recorder.release();
                                        recorder = null;
                                        play_btn.setVisibility(View.VISIBLE);
                                        delete_audio.setVisibility(View.VISIBLE);
                                        newTime1 = 0;
                                        timer.setText("");
                                        timer2.setVisibility(View.VISIBLE);

                                    } catch (Exception e) {
                                        e.getStackTrace();
                                    }
                                    recording_timer.cancel();
                                    getdialog_showResponse(getApplicationContext(), "Audio Record Stop !");

                                }
                                recording_timer.purge();
                            }
                            Log.e("Socket", "ReturnFile name" + audio_file_name);
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event


                        }
                    });
                    alertDialog.show();
                }

                if (flag5 == 6) {


                    if (recorder != null) {
                        recorder.stop();
                        recording_timer.cancel();
                        record_audio.setBackgroundResource(R.drawable.audiogreenbutten);

                        recorder = null;
                        play_btn.setVisibility(View.VISIBLE);
                        seek_bar.setVisibility(View.VISIBLE);
                        timer.setVisibility(View.VISIBLE);
                        timer2.setVisibility(View.VISIBLE);
                        getdialog_showResponse(getApplicationContext(), "Audio Record Stop !");
                        newTime1 = 0;
                        timer.setText("");
                        delete_audio.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flag2 == 0) {
                    player_ask = new MediaPlayer();

                    try {
                        flag3 = 4;
                        // player_ask.setDataSource("http://smartbridges.co.in/admin/androidNew1/" + Audio);
                        audio_file_path = IISERApp.AudioPathLocal + audio_file;
                        player_ask.setDataSource(audio_file_path);
                        player_ask.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        seek_bar.setVisibility(View.VISIBLE);

                    } catch (IllegalArgumentException e) {
                        Log.i("tag", "Audioname 1" + e);
                    } catch (SecurityException e) {
                        Log.i("tag", "Audioname 11" + e);
                    } catch (IllegalStateException e) {
                        Log.i("tag", "Audioname 111" + e);
                    } catch (IOException e) {
                        Log.i("tag", "Audioname 1111" + e);
                    }
                    try {
                        player_ask.prepare();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    play_btn.setBackgroundResource(R.drawable.play);
                    finalTime = player_ask.getDuration();
                    sec_time1 = formatMilliSecondsToTime(finalTime);
                    Log.i("tag", "Audioname 1" + finalTime);
                    recording_timer = new Timer();
                    recording_timer.schedule(new Updater(seek_bar, timer, finalTime, play_btn), 1000, 1000);
                    timer2.setText(sec_time1);

                    seek_bar.setMax(finalTime);
                    try {
                        Log.d("SchoolNotice", "if---:" + flag1);
                        timer2.setText(sec_time1);
                        play_btn.setBackgroundResource(R.drawable.play);
                        player_ask.start();
                        finalTime = 0;
                        flag2 = 1;

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else {
                    Log.d("SchoolNotice", "else---:" + flag1);
                    play_btn.setBackgroundResource(R.drawable.playw);
                    player_ask.pause();
                    flag2 = 0;
                    newTime1 = 0;
                    recording_timer.cancel();

                }


            }
        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (player_ask != null) {
                    if (player_ask.isPlaying()) {
                        if (b) {
                            cmdSeekTo(i);
                        }
                        i = i / 1000;
                        i = i * 1000;
                        timer.setText(formatMilliSecondsToTime(i) + " /");
                    } else {

                        seekBar.setProgress(0);
                        play_btn.setBackgroundResource(R.drawable.playw);

                    }
                } else {

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        delete_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityAddAssignment.this);


                alertDialog.setTitle("Confirm Delete");
                alertDialog.setMessage("Delete audio file?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(IISERApp.AudioPathLocal, audio_file);
                        if (file.exists() == true) {
                            if (player_ask != null) {
                                if (player_ask.isPlaying()) {
                                    player_ask.stop();
                                }
                                file.delete();
                                audio_file = "";
                                flag1 = 0;
                                flag5 = 5;
                                play_btn.setVisibility(View.GONE);
                                timer.setVisibility(View.GONE);
                                timer2.setVisibility(View.GONE);
                                seek_bar.setVisibility(View.GONE);
                                delete_audio.setVisibility(View.GONE);
                                getdialog_showResponse(getApplicationContext(), "Audio successfully deleted!");
                            } else {
                                file.delete();
                                audio_file = "";
                                flag5 = 5;
                                flag1 = 0;

                                play_btn.setVisibility(View.GONE);
                                timer.setVisibility(View.GONE);
                                timer2.setVisibility(View.GONE);
                                seek_bar.setVisibility(View.GONE);
                                delete_audio.setVisibility(View.GONE);
                                getdialog_showResponse(getApplicationContext(), "Audio successfully deleted!");

                            }
                        } else {
                            if (player_ask != null) {
                                file.delete();
                                audio_file = "";
                                flag1 = 0;
                                play_btn.setVisibility(View.GONE);
                                timer.setVisibility(View.GONE);
                                timer2.setVisibility(View.GONE);
                                seek_bar.setVisibility(View.GONE);
                                delete_audio.setVisibility(View.GONE);
                                getdialog_showResponse(getApplicationContext(), "Audio successfully deleted!");
                            } else {
                                file.delete();
                                audio_file = "";
                                flag1 = 0;
                                play_btn.setVisibility(View.GONE);
                                timer.setVisibility(View.GONE);
                                timer2.setVisibility(View.GONE);
                                seek_bar.setVisibility(View.GONE);
                                delete_audio.setVisibility(View.GONE);
                                getdialog_showResponse(getApplicationContext(), "Audio successfully deleted!");

                            }
                        }

                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event


                    }
                });
                alertDialog.show();


            }
        });


    }

    private void bindComponentData() {
    }

    private void requestForDocument() {

        IISERApp.log(LOG_TAG, "requestForDocument");

        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                IISERApp.log(LOG_TAG, "Package Name->" + packageName);
                Intent intent1 = new Intent();
                intent1.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                targetShareIntents.add(intent1);
            }
        }
        if (!targetShareIntents.isEmpty()) {
            System.out.println("Have Intent");
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to select document");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
            startActivityForResult(chooserIntent, PERMISSION_REQUEST_DOCUMENT);
        } else {
            System.out.println("Do not Have Intent");
        }
    }

    public void select_date() {

        // TODO Auto-generated method stub
        // To show current date in the datepicker

        Calendar mcurrentDate = Calendar.getInstance();




       // Calendar mcurrentDate = Calendar.getInstance();
        System.out.println("Current time => " + mcurrentDate.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
        final String formattedDate = df.format(mcurrentDate.getTime());
        System.out.println("formated date => " + formattedDate);








        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        if (selected_date != "") {
            Date yourDate = null;
            try {
                yourDate = dFyyyy_mm_dd.parse(selected_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mcurrentDate.setTime(yourDate);
            mYear = mcurrentDate.get(Calendar.YEAR);
            mMonth = mcurrentDate.get(Calendar.MONTH);
            mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker,
                                          int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /* Your code to get date and time */
                        selectedmonth = selectedmonth + 1;
                        if (selectedday < 10) {
                            /*selected_date = selectedmonth + "/" + "0"+ selectedday + "/" + selectedyear;*/
                            selected_date = selectedyear + "-" + selectedmonth + "-" + "0" + selectedday;
                            display_date = "0" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                        }
                        if (selectedmonth < 10) {
                            /*selected_date = "0" + selectedmonth + "/"+ selectedday + "/" + selectedyear;*/
                            selected_date = selectedyear + "-" + "0" + selectedmonth + "-" + selectedday;
                            display_date = selectedday + "/" + "0" + selectedmonth + "/"
                                    + selectedyear;
                        }
                        if (selectedmonth < 10 && selectedday < 10) {
                            /*selected_date = "0" + selectedmonth + "/0"+ selectedday + "/" + selectedyear;*/
                            selected_date = selectedyear + "-0" + selectedmonth + "-0" + selectedday;
                            display_date = "0" + selectedday + "/0"
                                    + selectedmonth + "/" + selectedyear;
                        }
                        if (selectedmonth > 9 && selectedday > 9) {
                            selected_date = selectedyear + "-" + selectedmonth + "-" + selectedday;
                            display_date = "" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                        }

                        txtvw_assign_date.setText(selected_date);

                        //RVFApp.getDiffYears(selected_date);

                    }
                }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();

    }

   /* private void set_student_data(){
        List<LinkedHashMap<String, String>> menuItems =TABLE_TIMETABLE.get_courses_spnr();
        ArrayAdapter semester_adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        semester_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_course.setAdapter(menuItems);

       // int spinnerPosition = semester_adapter.getPosition(sem_name);
        //set the default according to value
        //spnr_course.setSelection(spinnerPosition);
    }*/

    class HttpAudio extends AsyncTask<Void, String, String> {

        protected String doInBackground(Void... params) {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName = audio_file_name;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String responseFromServer = "";
            String urlString = "http://pkclasses.co.in/swami/Test.php";
            // String urlString = "http://192.168.1.110/Tej_backup/Test.php";
            Log.e("Debug", "File is written" + existingFileName);
            try {

                //------------------ CLIENT REQUEST
                FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                while (bytesRead > 0) {
                    Log.e("Debug", "File is writtenbuffer" + buffer);
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.e("Debug", "File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }

            //------------------ read the SERVER RESPONSE
            try {

                inStream = new DataInputStream(conn.getInputStream());
                String str;

                while ((str = inStream.readLine()) != null) {

                    Log.e("Debug", "Server Response " + str);

                }

                inStream.close();

            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }


            return str;

        }

        protected void onProgressUpdate(String... progress) {


        }

        protected void onPostExecute(Long result) {

        }

    }// HTTP

    class HttpDocument extends AsyncTask<Void, String, String> {

        protected String doInBackground(Void... params) {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName = str_file_path;
            IISERApp.log(LOG_TAG, "existingFileName:" + existingFileName);
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String responseFromServer = "";
            String urlString = "http://pkclasses.co.in/swami/Test.php";
            // String urlString = "http://192.168.1.110/Tej_backup/Test.php";
            Log.e("Debug", "File is written" + existingFileName);
            try {

                //------------------ CLIENT REQUESTw
                FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                while (bytesRead > 0) {
                    Log.e("Debug", "File is writtenbuffer" + buffer);
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.e("Debug", "File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }

            //------------------ read the SERVER RESPONSE
            try {

                inStream = new DataInputStream(conn.getInputStream());
                String str;

                while ((str = inStream.readLine()) != null) {

                    Log.e("Debug", "Server Response " + str);

                }

                inStream.close();

            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }


            return str;

        }

        protected void onProgressUpdate(String... progress) {


        }

        protected void onPostExecute(Long result) {

        }

    }// HTTP

    final void cmdSeekTo(int msec) {
        if (player_ask != null) {
            if (player_ask.isPlaying()) {
                player_ask.seekTo(msec);
            } else {
                Toast.makeText(ActivityAddAssignment.this,
                        "Invalid State@cmdSeekTo() - skip",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getdialog_showResponse(Context context, String msg) {
//         Toast.makeText(AskQuestion.this,
//         "Internet Is Not Available..",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddAssignment.this);
        builder.setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Yes button clicked, do something

						/*
                         * Intent intent = getIntent(); finish();
						 * startActivity(intent);
						 */
                    }
                }).show();
        return null;

    }

    //set path audio fun
    public String get_audioFilePath() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "IISERPUNE AUDIO");

        if (!file.exists())
            file.mkdirs();
        audio_file = String.valueOf(System.currentTimeMillis()) + ".mp3";
        IISERApp.log(LOG_TAG, "file-->" + file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
        audio_file_name = (file.getAbsolutePath() + "/" + audio_file);
        IISERApp.log(LOG_TAG, "AudioName" + audio_file_name);

        return audio_file_name;
    }

    private String formatMilliSecondsToTime(long milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return "" + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public class Updater extends TimerTask {
        SeekBar seekBar;
        TextView timer;
        int value;
        Button audio;

        public Updater(SeekBar seekBar, TextView timer, int value, Button audio) {

            this.seekBar = seekBar;
            this.timer = timer;
            this.value = value;
            this.audio = audio;
            Log.e("tag", "Trying to connect");

        }

        @Override
        public void run() {
            try {

                /*if (flag3 == 3) {*/
                newTime1 = newTime1 + 1000;


                if (newTime1 > value) {
                    recording_timer.cancel();
                    seek_bar.setProgress(0);
                    audio.setBackgroundResource(R.drawable.audiogreenbutten);
                    //flag2 = 0;

                } else {
                    //  timer.setText(String.valueOf(newTime1));
                    Log.e("Timer ", "Time" + newTime1 + "->" + audio_duration2);
                    seek_bar.setProgress(newTime1);
                    timer.setText(formatMilliSecondsToTime(newTime1) + " / ");
                }


               /* } else {

                    int mediaPosition1 = player_ask.getCurrentPosition();

                    if (player_ask.isPlaying()) {
                        seek_bar.setProgress(mediaPosition1);
                        timer.setText(formatMilliSecondsToTime(mediaPosition1) + " / ");
                    } else {
                        //  timer.setText(String.valueOf(newTime1));
                        recording_timer.cancel();
                        seek_bar.setProgress(0);
                       // flag2 = 0;
                        audio.setBackgroundResource(R.drawable.audiogreenbutten);
                        audio.setVisibility(View.VISIBLE);
                    }


                }
*/

            } catch (Exception e) {
                System.out.println(e);
            }
        }


        //convert int to time format
        private String formatMilliSecondsToTime(long milliseconds) {

            int seconds = (int) (milliseconds / 1000) % 60;
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
            return "" + twoDigitString(minutes) + " : "
                    + twoDigitString(seconds);
        }

        private String twoDigitString(long number) {

            if (number == 0) {
                return "00";
            }

            if (number / 10 == 0) {
                return "0" + number;
            }

            return String.valueOf(number);
        }
    }

    private void generate_image_byte_string() {
        img_path = new_str_path;
        int len_path = img_path.length();
        if (len_path != 0) {

            try {

                Log.i("data", "----=" + img_path);
                filename_image = String.valueOf(System.currentTimeMillis()) + ".jpg";
                System.out.println("filename photo----------------------->" + filename_image);
                File photofile = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTO_ALBUM, filename_image); //or any other format supported
                Log.i("data", "2data=" + photofile);
                compressphotobytearrray = new ByteArrayOutputStream();

                Bitmap photobitmap = BitmapFactory.decodeFile("/" + img_path);
                Log.i("data", "2data=" + img_path.toString());
                Log.i("data", "file:/" + Environment.getExternalStorageDirectory() + img_path);
                photobitmap.compress(Bitmap.CompressFormat.JPEG, 70, compressphotobytearrray);
                byte[] imagebytearray = compressphotobytearrray.toByteArray();


                compressphotobytearrray.close();
                Log.i("data", "compressphotobytearrrayclose");


                photobitmap.recycle();
                photoString = Base64.encodeBytes(imagebytearray);
                IISERApp.log(LOG_TAG, "photoString:" + photoString);

            } catch (Exception e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IISERApp.log(LOG_TAG, "onActivityResult");
        IISERApp.log(LOG_TAG, "RequestCode->" + requestCode);
        IISERApp.log(LOG_TAG, "ResultCode->" + resultCode);
        if (requestCode == PERMISSION_REQUEST_DOCUMENT) {
            if (requestCode != -1) {
                if (requestCode == 2) {
                    if (data != null) {
                        IISERApp.log(LOG_TAG, "URL->" + data.getData());
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        IISERApp.log(LOG_TAG, "Uri = " + uri.toString());
                     /*
     Get the file's content URI from the incoming Intent, then
      get the file's MIME type
     */

                        try {
                            String mimeType = getContentResolver().getType(uri);
                            IISERApp.log(LOG_TAG, "MimeType->" + mimeType);
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(context, uri);
                            IISERApp.log(LOG_TAG, "path->" + path);
                            if (!TextUtils.isEmpty(path)) {
                                String str[] = path.split("\\.");
                                IISERApp.log(LOG_TAG, "strArr->" + str.length);
                                if (str.length > 0) {
                                    mimeType = str[(str.length - 1)];
                                }
                                if (DocumentType.check_valid_docuement(mimeType)) {

                                    String str_file[] = path.split("\\/");
                                    IISERApp.log(LOG_TAG, "str_file->" + str_file.length);
                                    if (str_file.length > 0) {
                                        IISERApp.log(LOG_TAG, "str_fileName->" + str_file[(str_file.length - 1)]);
                                        str_file_name = str_file[(str_file.length - 1)];
                                        IISERApp.log(LOG_TAG, "mrunal" + mimeType);
                                        IISERApp.set_session(IISERApp.SESSION_MIME_TYPE, mimeType);
                                    } else {
                                        str_file_name = path;
                                    }
                                    IISERApp.log(LOG_TAG, "DocType->" + mimeType);
                                    IISERApp.log(LOG_TAG, "path->" + path);
                                    IISERApp.log(LOG_TAG, "IsValidDoc->" + DocumentType.check_valid_docuement(mimeType));
                                    IISERApp.log(LOG_TAG, "MimeType->" + DocumentType.get_document_mimeType(mimeType));

                                    File file = new File(path);
                                    str_file_path = path;

                                    IISERApp.log(LOG_TAG, "FileExists->" + file.exists());
                                    Toast.makeText(context, "File Selected: " + path, Toast.LENGTH_LONG).show();
                                    txt_file_name.setText(str_file_path);

                                    str_doc_string=pdfToByte(str_file_path);
                                    IISERApp.log(LOG_TAG, " str_doc_string " + str_doc_string);

                                } else {

                                    Snackbar.make(txt_document, "Inavlid document type", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            IISERApp.log(LOG_TAG, "File select error" + e);
                        }
                    }
                }
            }
        } else if (requestCode == PERMISSION_REQUEST_CAMERA) {
            //File f = new File(Environment.getExternalStorageDirectory().toString());
            // IISERApp.log(LOG_TAG, "IsFileExists->" + img_file.exists());


            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                IISERApp.log(LOG_TAG, "onActivityResult if 1");
                IISERApp.log("Rasika", "In onActivityResult 1");
                try {

                    flag_isCamera = true;
                    compressImage(str_image_path);

                    str_byte_string = ConvertString(new_str_path);


                }
                catch (Exception e) {
                    IISERApp.log(LOG_TAG, "In catch");
                    Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);

                }


            }
            /*if (img_file.exists()) {
                f = img_file;

            }
            else
            {
                IISERApp.log(LOG_TAG, "nooo IsFileExists->" + img_file.exists());
            }
            try {

                // l1.setVisibility(View.VISIBLE);
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmap = compressImage(str_image_path);
                display_camera_image.setVisibility(View.VISIBLE);
                display_camera_image.setImageBitmap(bitmap);
                //img_photo_1.setImageBitmap(bitmap);
                IISERApp.log(LOG_TAG, "Image url is " + f.getAbsolutePath() + ", " + f.getAbsoluteFile());


                String path = Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = img_file;//new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    public static Bitmap compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        IISERApp.log(LOG_TAG, "BactualWidth->" + actualWidth);
        Log.i(LOG_TAG, "BactualHeight->" + actualHeight);
//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 768.0f;
        float maxWidth = 1024.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        Log.i(LOG_TAG, "AactualWidth->" + actualWidth);
        Log.i(LOG_TAG, "AactualHeight->" + actualHeight);
//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            IISERApp.log(LOG_TAG, "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.i(LOG_TAG, "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.i(LOG_TAG, "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.i(LOG_TAG, "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        new_str_path = getFilename();
        try {
            out = new FileOutputStream(new_str_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//      write the compressed bitmap at the destination specified by filename.
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        return scaledBitmap;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
        {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getFilename() {
        File imageStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstant.PHOTO_ALBUM);
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        String new_str_path = imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        return new_str_path;
    }




    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();

            IISERApp.log_bundle(bundle);

           /* if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
                *//*Intent intent1 = new Intent(context, ActivityCourseSelection.class);
                startActivity(intent1);
                finish();*//*
                initComponents();
                initComponentsListeners();
                bindComponentData();
                prgresbar.setVisibility(View.GONE);
               Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();

            } else {
                prgresbar.setVisibility(View.GONE);
                Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }*/

            if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
                dialog.dismiss();
                // prgresbar.setVisibility(View.GONE);
                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("1")) {
                    IISERApp.log(LOG_TAG, "in if condition of response stastus 1");
                    if (dialog!= null && dialog.isShowing()) {
                        //dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                        dialog.dismiss();
                        // Toast.makeText(ActivityCourseSelection.this, "Courses applied sucessfully...", Toast.LENGTH_SHORT).show();

                       /* if (IISERApp.selected_course_lhm.size() > 0) {

                            Set<String> keys = IISERApp.selected_course_lhm.keySet();
                            for (String key : keys) {
                                IISERApp.selected_course_lhm.get(key);
                                IISERApp.log(LOG_TAG, "select" + key + "-" + IISERApp.selected_course_lhm.get(key));
                                if (IISERApp.selected_course_lhm.get(key).equalsIgnoreCase("Y")) {

                                }
                            }
                        }*/
                        //TABLE_COURSE.updateSelectedCourse();
                        /*IISERApp.log(LOG_TAG, "in if conditiense stastus 1 if dialog showing..");
                        // Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
                        initComponents();
                        initComponentsListeners();
                        bindComponentData();*/
                    }
                } else {
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                        dialog.dismiss();
                        //Toast.makeText(ActivityCourseSelection.this, "Courses applied sucessfully...", Toast.LENGTH_SHORT).show();
                    }
                }

                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("0")) {
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            } else {
                // prgresbar.setVisibility(View.GONE);
                if (dialog!= null && dialog.isShowing())
                    dialog.dismiss();
                //Toast.makeText(ActivityCourseSelection.this, "Courses applied sucessfully...", Toast.LENGTH_SHORT).show();
                // Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }
        }
    };


    public static String ConvertString(String path) {
        IISERApp.log(LOG_TAG, "In ConvertString ");
        String phototostring = "";
        try {
            IISERApp.log(LOG_TAG, "ConvertString path->" + path);

            ByteArrayOutputStream compressphotobytearrray = new ByteArrayOutputStream();
            Bitmap photobitmap = BitmapFactory.decodeFile(path);

            photobitmap.compress(Bitmap.CompressFormat.PNG, 70, compressphotobytearrray);
            byte[] imagebytearray = compressphotobytearrray.toByteArray();

            compressphotobytearrray.close();
            Log.i("data", "compressphotobytearrrayclose");


            photobitmap.recycle();
            phototostring = Base64.encodeBytes(imagebytearray);
//            App.log(TAG,"byte->"+phototostring);

        } catch (Exception e) {
            IISERApp.log(LOG_TAG, "ConvertString Error->" + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return phototostring;
    }




    public String pdfToByte(String filePath)throws Exception {

        File file = new File(filePath);
        FileInputStream fileInputStream;
        byte[] data = null;
        byte[] finalData = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        String pdf="";

        try {
            fileInputStream = new FileInputStream(file);
            data = new byte[(int)file.length()];
            finalData = new byte[(int)file.length()];
            byteArrayOutputStream = new ByteArrayOutputStream();

            fileInputStream.read(data);
            byteArrayOutputStream.write(data);
            finalData = byteArrayOutputStream.toByteArray();

            fileInputStream.close();
            pdf = Base64.encodeBytes(finalData);

        } catch (FileNotFoundException e) {
            /// LOGGER.info("File not found" + e);
        } catch (IOException e) {
            // LOGGER.info("IO exception" + e);
        }

        return pdf;

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
      //  IISERApp.log(LOG_TAG, "onDestroy>");
        IISERApp.log(LOG_TAG, "ondestroy");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        context.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public  void onResume()
    {
        super.onResume();
        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
    }
}
