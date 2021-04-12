package com.mobilesutra.iiser_tirupati.Fragment;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Activities.ActivityAssignImage;
import com.mobilesutra.iiser_tirupati.Activities.PDFDownloader;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ASSIGNMENT;
import com.mobilesutra.iiser_tirupati.Model.DTO_Assign;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Assign extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;

    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    List<LinkedHashMap<String, String>> assignment_list = new ArrayList<>();
    int assignment_list_count = 0;
    String LOG_TAG = "Fragment_Asssign", tab_title = "", filename = "";
    ProgressDialog dialog = null;

    SliderLayout mDemoSlider = null;
    View view = null;

    private static final String ARG_PARAM_TITLE = "title";

    public static Fragment_Assign newInstance(String str_title) {
        Log.d("Fragment", "In newInstance");
        Fragment_Assign fragment = new Fragment_Assign();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment_Assign() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_five, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_assign, container, false);

            Bundle args = getArguments();
            IISERApp.log(LOG_TAG, "tab title next:" + args.getCharSequence(ARG_PARAM_TITLE).toString());
            tab_title = args.getCharSequence(ARG_PARAM_TITLE).toString();
            LOG_TAG = LOG_TAG + tab_title;
            initComponentData(view);
            initComponentListeners();
            bindComponentData();

            // displayBanner(view);

        }

        return view;
    }


    private void initComponentData(View view) {

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);

        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

    }

    private void initComponentListeners()
    {

    }

    private void bindComponentData()
    {
        List<DTO_Assign> assign_List = new ArrayList<>();
        if (view != null) {
            assignment_list = TABLE_ASSIGNMENT.get_assignment_details(tab_title);
            IISERApp.log(LOG_TAG, "assignment_list: " + assignment_list);
            assignment_list_count = assignment_list.size();
            for (int i = 0; i < assignment_list_count; i++)
            {

                String assignment_title = assignment_list.get(i).get("assignment_title").toString();
                String description = assignment_list.get(i).get("description").toString();
                String course_id = assignment_list.get(i).get("course_id").toString();
           /* String teacher_name = assignment_list.get(i).get("cec_member").toString();*/
                String pdf_link = assignment_list.get(i).get("pdf_link").toString();
                String submission_date = assignment_list.get(i).get("submission_date").toString();
                String img_url = assignment_list.get(i).get("img_url").toString();

                String day = IISERApp.get_day_from_date(submission_date);
                String month = IISERApp.get_month_from_date(submission_date);

                DTO_Assign assignment = new DTO_Assign(assignment_title, description, month, day, pdf_link);
                assignment.setImg_url(img_url);
                assign_List.add(assignment);
            }
        }
        /*for (int i = 0; i < 10; i++) {
            DTO_Assign assignment = new DTO_Assign("Assignment 1", "Description", "APR", "18", "Dr.Aloke Das");
            assign_List.add(assignment);
        }*/
        IISERApp.log(LOG_TAG, "view:" + view);
        if (view != null)
        {
            if (recyclerAdapter == null) {
                IISERApp.log(LOG_TAG, "recyclerAdapter:null" + recyclerAdapter);
                recyclerAdapter = new EventAdapter(assign_List);
                recycler_view.setAdapter(recyclerAdapter);
            } else
            {
                IISERApp.log(LOG_TAG, "recyclerAdapter:not null" + recyclerAdapter);
                IISERApp.log(LOG_TAG, "assign_List size clear:" + assign_List.size());
                recyclerAdapter.assign_List.clear();
                IISERApp.log(LOG_TAG, "assign_List size after clear" + assign_List.size());
                recyclerAdapter.assign_List.addAll(assign_List);
                IISERApp.log(LOG_TAG, "assign_List size after addition" + assign_List.size());
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshData() {
        bindComponentData();
    }

    public void displayBanner(View view) {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


        mDemoSlider = (SliderLayout) view.findViewById(R.id.sample_output);
        mDemoSlider.setFocusable(true);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);
       /* LinkedHashMap<String, String> lhm = ShopApp.db.getFrontBanner();
        int length = lhm.size();
        if (length == 0) {*/
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1, R.drawable.banner_3};

        for (int i = 0; i < 4; i++) {

            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description("")
                    .image(bannerArray[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");

            mDemoSlider.addSlider(textSliderView);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<DTO_Assign> assign_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView assign_title, sub_name, submission_date, submission_month, teacher_name, pdf_link;
            public LinearLayout lnrLayout;
            public Button view_photo;


            public MyViewHolder(View view) {
                super(view);
               // if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student"))
                {
                    assign_title = (TextView) view.findViewById(R.id.txt_assign_title);
                    view_photo =(Button) view.findViewById(R.id.view_photo);

/*

                    if(IISERApp.get_session(IISERApp.SESSION_IMG_PATH).isEmpty())
                    {
                        view_photo.setVisibility(View.GONE);
                    }
*/





                    sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
                    submission_date = (TextView) view.findViewById(R.id.txt_exam_date);
                    submission_month = (TextView) view.findViewById(R.id.txt_exam_month);
                   // teacher_name = (TextView) view.findViewById(R.id.txt_teacher_name);
                    pdf_link = (TextView) view.findViewById(R.id.txt_pdf_link);
                    lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);
                }
             /*   view_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);

                    }
                });*/
            }
        }


        public EventAdapter(List<DTO_Assign> assign_List) {
            this.assign_List = assign_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_assignment_row, parent, false);




            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {


                    Log.d("FragmentFive", "clicked on item");
                    /*if (IISERApp.hasLollipop()) {
                        Intent intent = new Intent(context, ActivityEventDetails.class);
                        startActivity(intent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tutorialspoint.com/android/android_tutorial.pdf"));
                        startActivity(browserIntent);
                    }

*/

                    filename = "";
                    TextView txt_pdf_link = (TextView) view.findViewById(R.id.txt_pdf_link);
                    IISERApp.log(LOG_TAG, "pdf_link:" + txt_pdf_link.getText().toString());
                    String pdfUrl = txt_pdf_link.getText().toString();
                    //  String pdfUrl = "http://192.168.0.242/ms-projects/mobilesutra.com/isser/uploads/event/Android.pdf";
                    Loadpdf(pdfUrl);
                    /*Intent intent = new Intent(context, ActivityPDFReader.class);
                    startActivity(intent);*/


                }
            });

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {


           holder.view_photo.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {
                   IISERApp.log(LOG_TAG, "clciked viewbutton");
                   //Toast.makeText(context, "view photo is cliced!", Toast.LENGTH_SHORT).show();
                  // IISERApp.log(LOG_TAG,"image is : "+);
                   DTO_Assign assign = assign_List.get(position);

                   String img_url = assign.getImg_url();
                   IISERApp.set_session(IISERApp.SESSION_IMG_PATH, img_url);
                   IISERApp.log(LOG_TAG, "str_image_path is" + IISERApp.get_session(IISERApp.SESSION_IMG_PATH));
                   //IISERApp.log(LOG_TAG, "str_image_path is" + IISERApp.get_session(IISERApp.SESSION_IMG_PATH));

                  Intent intent = new Intent(getActivity(), ActivityAssignImage.class);
                   startActivity(intent);


               }

           });
            DTO_Assign assign = assign_List.get(position);
            holder.assign_title.setText(assign.getStr_assign_title());

/*

            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
            {
                DTO_Assign assign1 = assign_List.get(position);
                holder.assign_title.setText(assign1.getStr_assign_title());
            }*/
            //holder.view_photo.setText(assign.getStr_sub_name());
            holder.sub_name.setText(assign.getStr_sub_name());
            holder.submission_date.setText(assign.getStr_submission_date());
            holder.submission_month.setText(assign.getStr_submission_month());
            // holder.teacher_name.setText(assign.getStr_teacher_name());
            holder.pdf_link.setText(assign.getStr_teacher_name());
            //holder.view_photo.setText(assign.getImg_url());


        }

        @Override
        public int getItemCount() {
            return assign_List.size();
        }


    }

    public void Loadpdf(String pdfurl)
    {
        {
            // TODO Auto-generated method stub

            // boolean deleted = file.delete();
            filename = pdfurl.substring(pdfurl.lastIndexOf('/') + 1);
            File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/pdf/" + filename);
            if (file.exists()) {
                IISERApp.log(LOG_TAG, "filename in loadpdf if:" + filename);
            /*if(dialog!=null && dialog.isShowing())
                dialog.dismiss();*/


                showPdf();
            } else {
                IISERApp.log(LOG_TAG, "filename in loadpdf else :" + filename);

                if (((IISERApp) getActivity().getApplication()).isInternetAvailable() == true) {

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(dialog!=null && dialog.isShowing())
                        dialog.dismiss();
                    if (getPermissionCount() > 0)
                        check_app_persmission();
                    else
                        new DownloadFile().execute(pdfurl, filename);

                } else {*/
                    new DownloadFile().execute(pdfurl, filename);
                    //}

                } else {
                    ((IISERApp) getActivity().getApplication()).getdialog_checkinternet(context);

                }
            }

        }

    }
    /*{
        // TODO Auto-generated method stub

        // boolean deleted = file.delete();
        filename = pdfurl.substring(pdfurl.lastIndexOf('/') + 1);
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pdf/" + filename);
        if (file.exists()) {
            IISERApp.log(LOG_TAG, "filename in loadpdf if:" + filename);
            showPdf();
        } else {
            IISERApp.log(LOG_TAG, "filename in loadpdf else :" + filename);

            if (((IISERApp) getActivity().getApplication()).isInternetAvailable() == true) {

                new DownloadFile().execute(pdfurl, filename);

            } else {
                ((IISERApp) getActivity().getApplication()).getdialog_checkinternet(context);

            }
        }

    }*/

    public void showPdf()
    {
        {

            File pdfFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/pdf/" + filename);

            String filepath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/pdf/" + filename;
            try {

                if (pdfFile.exists()) {

                    if(dialog!=null && dialog.isShowing())
                        dialog.dismiss();

                    Uri path = Uri.fromFile(pdfFile);
                    IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);

                    if (IISERApp.hasMarshMallow()) {
                        Intent objIntent = new Intent(Intent.ACTION_VIEW);

                        objIntent.setDataAndType(path, "application/pdf");

                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(objIntent);

                    } else {

                    /*IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);
                    Intent intent = new Intent(context, ActivityPDFReader.class);
                    intent.putExtra("PdfUrl", filepath);
                    IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);
                    startActivity(intent);
                    IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);*/


                        if (pdfFile.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.fromFile(pdfFile);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "No pdf file to view", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {

                    Toast.makeText(context, "File NotFound",

                            Toast.LENGTH_SHORT).show();

                }

            } catch (ActivityNotFoundException e) {

                Toast.makeText(
                        context,

                        "No Pdf Viewer Application Found.Please Download Any Pdf Viewer Application From Google Play Store",
                        Toast.LENGTH_SHORT)

                        .show();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }
   /* {

        File pdfFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pdf/" + filename);

        String filepath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pdf/" + filename;
        try {

            if (pdfFile.exists()) {

                Uri path = Uri.fromFile(pdfFile);
                IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);

                if (IISERApp.hasMarshMallow()) {
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);

                    objIntent.setDataAndType(path, "application/pdf");

                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(objIntent);

                } else {
                    Intent intent = new Intent(getActivity().getApplication(), ActivityPDFReader.class);
                    intent.putExtra("PdfUrl", filepath);
                    startActivity(intent);
                }
            } else {

               *//* Toast.makeText(ActivityEvents.this, "File NotFound",

                        Toast.LENGTH_SHORT).show();*//*

            }

        } catch (ActivityNotFoundException e) {

           *//* Toast.makeText(
                    ActivityEvents.this,

                    "No Pdf Viewer Application Found.Please Download Any Pdf Viewer Application From Google Play Store",
                    Toast.LENGTH_SHORT)

                    .show();*//*

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
*/
    public class DownloadFile extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {

            // dialog = ProgressDialog.show(Holiday_List.this,
            // "Downloading....",
            // "downloading file", true, false);
            // progressBar.setVisibility(View.VISIBLE);
            //  webView.setWebViewClient(new myWebClient());
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0]; // ->
            // http://maven.apache.org/maven-1.x/maven.pdf
            String fileName1 = strings[1]; // -> maven.pdf
            Log.d("HolidayList", "fileUrl:" + fileUrl);
            Log.d("HolidayList", "fileName1:" + fileName1);
            String extStorageDirectory = Environment
                    .getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "pdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName1);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                Log.d("HolidayList", "IOException");
                e.printStackTrace();
            }

            int res = 0;
            try {
                if (((IISERApp) getActivity().getApplication()).isInternetAvailable()) {

                    String pdfurl = fileUrl.replaceAll(" ", "%20");
                    Log.d("HolidayList", "URL without space:" + pdfurl);
                    //res = PDFDownloader.downloadFile(fileUrl.replaceAll(" ", "%20"), pdfFile);
                    res = PDFDownloader.downloadFile(pdfurl, pdfFile);

                    // Log.d("HolidayList", "Res:" + res);
                    // onProgressUpdate("" + res);
                } else {
                    ((IISERApp) getActivity().getApplication())
                            .getdialog_checkinternet(context);

                }
            } catch (Exception e) {
                res = 0;
                // getdialog_internetslow();
                Log.d("HolidayList", " catch Res:" + res);
            }
            Log.d("HolidayList", "Res:" + res);

            onProgressUpdate("" + res);
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // dialog.dismiss();
            if (progress[0].equals("0")) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Log.d("HolidayList", "Res: 0");

                        ((IISERApp) getContext()).getdialog_internetslow(context);

                        // // Toast.makeText(Holiday_List.this,
                        // // "Your internet connection is slow..",
                        // Toast.LENGTH_LONG)
                        // // .show();

                    }
                });
               // ((IISERApp) getActivity().getApplication()).getdialog_internetslow(context);
            } else {
                Log.d("HolidayList", "Enter in else part");
                //showPdf();
            }

        }

        protected void onPostExecute(Long result) {

        }

    }
}