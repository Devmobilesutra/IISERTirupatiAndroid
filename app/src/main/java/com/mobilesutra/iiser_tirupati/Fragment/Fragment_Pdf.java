package com.mobilesutra.iiser_tirupati.Fragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.SearchTask;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.R;

import java.io.File;

public class Fragment_Pdf extends Fragment {
    private RelativeLayout mainLayout;
    private MuPDFCore core;
    private MuPDFReaderView mDocView;
    private Context mContext;
    private String mFilePath;
    Bundle args = new Bundle();
    private static final String TAG = "PdfFragment";
    private SearchTask mSearchTask;
    String LOG_TAG = "Fragment_Pdf", filename = "";

    public Fragment_Pdf() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        IISERApp.log(LOG_TAG, "in onCreateView");
        mContext = getActivity();
        args = this.getArguments();
        mFilePath = args.getString("filepath");

        View rootView = inflater.inflate(R.layout.pdf, container, false);
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.pdflayout);
        IISERApp.log(TAG,"iN openFile->"+mFilePath);
       // core = openFile(Uri.decode(mFilePath));
        openPdf(mFilePath);
//        if (core != null && core.countPages() == 0) {
//            core = null;
//        }
//        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
//            Log.e(TAG, "Document Not Opening");
//        }
//        if (core != null) {
//            mDocView = new MuPDFReaderView(getActivity()) {
//                @Override
//                protected void onMoveToChild(int i) {
//                    if (core == null)
//                        return;
//                    super.onMoveToChild(i);
//                }
//
//            };
//
//            mDocView.setAdapter(new MuPDFPageAdapter(mContext, core));
//            mainLayout.addView(mDocView);
//        }
//
//        mSearchTask = new SearchTask(mContext, core) {
//
//            @Override
//            protected void onTextFound(SearchTaskResult result) {
//                SearchTaskResult.set(result);
//                mDocView.setDisplayedViewIndex(result.pageNumber);
//                mDocView.resetupChildren();
//            }
//        };

        return rootView;
    }

    public void search(int direction, String text) {
        int displayPage = mDocView.getDisplayedViewIndex();
        SearchTaskResult r = SearchTaskResult.get();
        int searchPage = r != null ? r.pageNumber : -1;
        mSearchTask.go(text, direction, displayPage, searchPage);
    }


    private MuPDFCore openBuffer(byte[] buffer) {
        System.out.println("Trying to open byte buffer");
        try {
            core = new MuPDFCore(mContext, buffer);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }

    private MuPDFCore openFile(String path) {
        IISERApp.log(TAG,"iN openFile->"+path);
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(mContext, path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }

    public  void openPdf(String str_path)
    {
        IISERApp.log(LOG_TAG, "in openPdf AND PATH IS "+str_path);

        File file = new File(str_path);
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        IISERApp.log(LOG_TAG,"in pdfopenintent");
        try {
            startActivity(pdfOpenintent);
        }
        catch (ActivityNotFoundException e) {

        }
    }
    public void onDestroy() {
        IISERApp.log(LOG_TAG, "in onDestroy");
        if (core != null)
            core.onDestroy();

        core = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        IISERApp.log(LOG_TAG, "in onPause");
        if (mSearchTask != null)
            mSearchTask.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        IISERApp.log(LOG_TAG, "in onDestroyView");
    }

}


