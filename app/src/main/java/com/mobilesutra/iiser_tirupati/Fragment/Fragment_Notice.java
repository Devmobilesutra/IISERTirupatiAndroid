package com.mobilesutra.iiser_tirupati.Fragment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Activities.ActivityEvents;
import com.mobilesutra.iiser_tirupati.Activities.ActivityPDFReader;
import com.mobilesutra.iiser_tirupati.Activities.EventPdf;
import com.mobilesutra.iiser_tirupati.Activities.Notice_pdf;
import com.mobilesutra.iiser_tirupati.Activities.PDFDownloader;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_NOTICE;
import com.mobilesutra.iiser_tirupati.Model.DTO_Notice;
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


public class Fragment_Notice extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    Context context = null;
    RecyclerView recycler_view = null;
    public List<LinkedHashMap<String, String>> DTO_NoticeList = new ArrayList<>();

    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    SliderLayout mDemoSlider=null;
    View view=null;
   public List<DTO_Notice> DTONoticeList = new ArrayList<>();

    int notice_list_count=0;
    ProgressDialog dialog=null;

    FloatingActionButton fab_alarm=null;

    List<LinkedHashMap<String, String>> notice_list=new ArrayList<>();

    private static final String ARG_PARAM_TITLE = "title";
    private static final String LOG_TAG="Fragment_Notice";
    private static String filename="";
 //   private List<LinkedHashMap<String, String>> DTONotice;

    public static Fragment_Notice newInstance(String str_title) {
        Log.d("Fragment", "In newInstance");
        Fragment_Notice fragment = new Fragment_Notice();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        fragment.setArguments(bundle);
        return fragment;
    }
    public Fragment_Notice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.activity_fragment_profile, container, false);
        if(view==null) {
            view = inflater.inflate(R.layout.activity_fragment_notice, container, false);

            initComponentData(view);
            initComponentListeners();
            //displayBanner(view);
            bindComponentData();
        }
        return view;
    }


    private void getIntentData() {
    }

    private void
    initComponentData(View view) {
       /* fab_alarm=(FloatingActionButton)findViewById(R.id.fab_alarm);
        fab_alarm.setVisibility(View.GONE);*/
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);
        DTO_NoticeList = new ArrayList<>();
        DTONoticeList  = new ArrayList<DTO_Notice>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
    }

    private void initComponentListeners()
    {
       /* IISERApp.log(LOG_TAG,"in componntlisteners");

        DTO_NoticeList= TABLE_NOTICE.get_notice_list();
        IISERApp.log(LOG_TAG,"notices are ...:"+DTO_NoticeList);
        recyclerAdapter = new EventAdapter(DTONoticeList);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();
        Date date_curr = null,result_date=null;

        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        IISERApp.log(LOG_TAG, "IN FOR" + DTONoticeList.size());
        for(int i=0 ; i<DTONoticeList.size() ; i++) {
            IISERApp.log(LOG_TAG, "IN FOR LOOP: " + DTO_NoticeList.size());
            DTO_Notice notice = DTONoticeList.get(i);
            IISERApp.log(LOG_TAG, "IN for value  of I: ");
            String notice_date = notice.getStr_expiry_date();

            try
            {

                result_date = targetFormat.parse(notice_date);

//                        System.out.println(date);
                date_curr = new Date();
                IISERApp.log(LOG_TAG, "CURRENT DATE :" + date_curr);
                IISERApp.log(LOG_TAG, "EVENT DATE :" + result_date);
                int diff = date_curr.compareTo(result_date);
                IISERApp.log(LOG_TAG, "date_curr.compareTo(result_date)" + diff);
                int diffrence2 = result_date.compareTo(date_curr);
                IISERApp.log(LOG_TAG, "result_date.compareTo(date_curr) :" + diffrence2);

                if (date_curr.compareTo(result_date) == 0) {
                    IISERApp.log(LOG_TAG, "IN IF: ");

                    //recycler_view.getLayoutManager().scrollToPosition(i);
                    recycler_view.scrollToPosition(i);
                    break;
                } else if (date_curr.compareTo(result_date) < 0) {
                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOP: ");
                    recycler_view.scrollToPosition(i);
                    //recycler_view.getLayoutManager().scrollToPosition(i);

                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOPvalue  of I: ");
                    break;


                }
            }
            catch (ParseException e) {
                // TODO Auto-generated catch block
                // Toast.makeText(MainActivity.this,"in exception1",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
*/
    }

    private void bindComponentData() {

        List<DTO_Notice> DTONoticeList = new ArrayList<>();
        if (view != null) {
            notice_list = TABLE_NOTICE.get_notice_list();
            notice_list_count = notice_list.size();
            for (int i = 0; i < notice_list_count; i++) {
                String notice_id = notice_list.get(i).get("notice_id").toString();
                String notice_title = notice_list.get(i).get("notice_title").toString();
                String notice_description = notice_list.get(i).get("notice_description").toString();
                String expiry_date = notice_list.get(i).get("expiry_date").toString();
                String notice_pdf_link = notice_list.get(i).get("pdf_link").toString();

                String day = IISERApp.get_day_from_date(expiry_date);
                String month = IISERApp.get_month_from_date(expiry_date);

                DTO_Notice DTONotice = new DTO_Notice(notice_id, notice_title, notice_description, notice_pdf_link
                        , expiry_date, day, month);
                DTONoticeList.add(DTONotice);
            }
        }
        /*DTO_Notice DTONotice = new DTO_Notice("","Notice 1","Description",""
                ,"","21","APR");
        DTONoticeList.add(DTONotice);

        DTONotice = new DTO_Notice("","Notice 2","Description",""," ","4","JUL");
        DTONoticeList.add(DTONotice);

        DTONotice = new DTO_Notice("", "Notice 3","Description","","","10", "JUL");
        DTONoticeList.add(DTONotice);

        DTONotice = new DTO_Notice("",  "Notice 4", "Description","","","15", "DES");
        DTONoticeList.add(DTONotice);*/


        IISERApp.log(LOG_TAG, "view:" + view);
        if (view != null) {
            if (recyclerAdapter == null) {
                IISERApp.log(LOG_TAG,"recyclerAdapter:null"+recyclerAdapter);
                recyclerAdapter = new EventAdapter(DTONoticeList);
                recycler_view.setAdapter(recyclerAdapter);
            } else {
                IISERApp.log(LOG_TAG,"recyclerAdapter:not null"+recyclerAdapter);
                IISERApp.log(LOG_TAG,"DTONoticeList size clear:"+DTONoticeList.size());
                recyclerAdapter.DTOEventList.clear();
                IISERApp.log(LOG_TAG, "DTONoticeList size after clear" +DTONoticeList.size());
                recyclerAdapter.DTOEventList.addAll(DTONoticeList);
                IISERApp.log(LOG_TAG, "DTONoticeList size after addition" + DTONoticeList.size());
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshData()
    {
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
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1,  R.drawable.banner_3};

        for (int i = 0; i < 3; i++) {

            TextSliderView textSliderView = new TextSliderView(context);
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
       /* } else {*/

          /*  Set<String> keys = lhm.keySet();
            for (String key : keys) {
                TextSliderView textSliderView = new TextSliderView(getContext());
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get(key))
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", key);

                mDemoSlider.addSlider(textSliderView);
            }*/
          /*  for (int i = 0; i < length; i++) {

                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get("" + i))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", lhm.get("" + i));

                mDemoSlider.addSlider(textSliderView);
            }*/
        // }


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

        private List<DTO_Notice> DTOEventList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView notice_title, notice_description, event_venue, event_month, event_day,pdf_link;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                event_month = (TextView) view.findViewById(R.id.txt_month);
                event_day = (TextView) view.findViewById(R.id.txt_day);
                notice_title = (TextView) view.findViewById(R.id.txt_event_header);
                notice_description = (TextView) view.findViewById(R.id.txt_event_organiser);
                 event_venue = (TextView) view.findViewById(R.id.txt_event_venue);
                pdf_link= (TextView) view.findViewById(R.id.txt_event_pdf_link);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);
                fab_alarm=(FloatingActionButton)view.findViewById(R.id.fab_alarm);
                fab_alarm.setVisibility(View.GONE);

            }
        }


        public EventAdapter(List<DTO_Notice> DTOEventList) {
            this.DTOEventList = DTOEventList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_row_event, parent, false);

            itemView.setOnClickListener(new View.OnClickListener() {
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
                    TextView txt_pdf_link = (TextView) view.findViewById(R.id.txt_event_pdf_link);
                    IISERApp.log(LOG_TAG, "pdf_link:" + txt_pdf_link.getText().toString());
                    String pdfUrl = txt_pdf_link.getText().toString();
                    //  String pdfUrl = "http://192.168.0.242/ms-projects/mobilesutra.com/isser/uploads/event/Android.pdf";

/*
                    Notice_pdf pd= (Notice_pdf) getActivity();
                    pd.fi(pdfUrl);*/



                    Intent i = new Intent(getActivity().getBaseContext(),
                            Notice_pdf.class);

                    //PACK DATA
                    i.putExtra("SENDER_KEY", "MyFragment");
                    i.putExtra("NAME_KEY", pdfUrl);
                    getActivity().startActivity(i);



                 /*   Intent  i = new Intent(Fragment_Notice.this,EventPdf.class);
                    i.putExtra("pdfurl", pdfUrl);
                    startActivity(i);*/



                  //  Loadpdf(pdfUrl);


                  /*  Intent intent1 = new Intent(getActivity().getApplication(), ActivityPDFReader.class);
                    intent1.putExtra("PdfUrl", pdfUrl);

                    startActivity(intent1);*/






                  /*  File targetFile = new File(pdfUrl);
                    Uri targetUri = Uri.fromFile(targetFile);

                    Intent intent;
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(targetUri, "application/pdf");
                    startActivity(intent);
*/

                    IISERApp.log(LOG_TAG,"in load pdf");
                    /*Intent intent = new Intent(context, ActivityPDFReader.class);
                    startActivity(intent);*/

                }
            });
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_Notice DTONotice = DTOEventList.get(position);
            holder.event_month.setText(DTONotice.getStr_month());
            holder.event_day.setText(DTONotice.getStr_day());
            holder.notice_title.setText(DTONotice.getStr_notice_title());
            holder.notice_description.setText(DTONotice.getStr_notice_description());
            holder.pdf_link.setText(DTONotice.getStr_notice_pdf_link());
            //          holder.event_venue.setText(DTOEvent.getStr_event_venue());

        }

        @Override
        public int getItemCount() {
            return DTOEventList.size();
        }


    }
    public void Loadpdf(String pdfurl) {
        IISERApp.log(LOG_TAG,"IN LOAD PDF");
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

    }

    public void showPdf() {

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
                 /*   Intent intent1 = new Intent(getActivity().getApplication(), ActivityPDFReader.class);
                    intent1.putExtra("PdfUrl", filepath);

                    startActivity(intent1);*/
                    if (pdfFile.exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(pdfFile);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "No pdf file to view", Toast.LENGTH_LONG).show();
                    }
                }
            } else {

                Toast.makeText(getContext(), "File NotFound",

                        Toast.LENGTH_SHORT).show();

            }

        } catch (ActivityNotFoundException e) {

            Toast.makeText(
                    getContext(),

                    "No Pdf Viewer Application Found.Please Download Any Pdf Viewer Application From Google Play Store",
                    Toast.LENGTH_SHORT)

                    .show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

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
            IISERApp.set_session("pdfFile", pdfFile.toString());
            if (pdfFile.exists()) {
                IISERApp.log(LOG_TAG, "EXISTS PDF" + pdfFile);
            } else {
                IISERApp.log(LOG_TAG, "PDF DOES NOT EXISTS " + pdfFile);
            }

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
            //dialog.dismiss();
            if (progress[0].equals("0")) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Log.d("HolidayList", "Res: 0");

                        //((IISERApp) getContext()).getdialog_internetslow(context);

                        // // Toast.makeText(Holiday_List.this,
                        // // "Your internet connection is slow..",
                        // Toast.LENGTH_LONG)
                        // // .show();

                    }
                });
                //((IISERApp) getActivity().getApplication()).getdialog_internetslow(context);
            } else {
                Log.d("HolidayList", "Enter in else part");
                //showPdf();
            }

        }

        protected void onPostExecute(Long result) {

        }

    }
}
