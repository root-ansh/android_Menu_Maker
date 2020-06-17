/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:07 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package in.curioustools.menu_maker.screens.menu_list;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import in.curioustools.menu_maker.R;
import in.curioustools.menu_maker.modal.MenuEntry;
import in.curioustools.menu_maker.modal.MenuEntryType;

@SuppressLint("SetTextI18n")
public class BottomSheetManager {


    private Button btItem, btCategory, btSave;
    private AutoCompleteTextView etItem, etCategory;
    private TextInputEditText etPrHalf, etPrFull, etPrSingle;
    private TextInputLayout tilHalf, tilFull, tilSingle;
    private RadioGroup rgRates;
    private LinearLayout llItemName;
    private LinearLayout llBottomSheetRoot;

    private BottomSheetBehavior sheetBehavior;

    private boolean isTypeItem, isRateMulti;
    private String editEntryid;

    public static final String TAG = "BottomSheet >>";

    @Nullable
    MenuEntryListActivityVM menuEntryListActivityVM = null;

    public BottomSheetManager(@Nullable View bottomSheetView, MenuEntryListActivityVM mvm) {
        if (bottomSheetView != null) {
            initUi(bottomSheetView);
            attachListeners();
        }
        this.menuEntryListActivityVM=mvm;

    }

    private void initUi(View v) {
        llBottomSheetRoot = v.findViewById(R.id.ll_bottom_sheet_root);


        btItem = v.findViewById(R.id.bt_item);
        btCategory = v.findViewById(R.id.bt_category);

        llItemName = v.findViewById(R.id.ll_item_name);
        etItem = v.findViewById(R.id.et_item);
        etCategory = v.findViewById(R.id.et_category);
        etItem.setDropDownVerticalOffset(0);
        etCategory.setDropDownVerticalOffset(0);

        rgRates = v.findViewById(R.id.rg_rates);

        etPrFull = v.findViewById(R.id.et_p_full);
        etPrHalf = v.findViewById(R.id.et_p_half);
        etPrSingle = v.findViewById(R.id.et_p_singleItem);

        tilHalf = v.findViewById(R.id.til_half);
        tilFull = v.findViewById(R.id.til_full);
        tilSingle = v.findViewById(R.id.til_single);

        btSave = v.findViewById(R.id.bt_save);

        //initialvalues
        isTypeItem = false;

        isRateMulti = false;

        toggleSingleOrMultiRates();

        toggleItemOrCategory();

    }

    private void attachListeners() {
        sheetBehavior = BottomSheetBehavior.from(llBottomSheetRoot);

        //bt dismiss


        // fab behaviour
        sheetBehavior.addBottomSheetCallback(getBottomSheetCallback());


        rgRates.setOnCheckedChangeListener((group, checkedId) -> {
            isRateMulti = checkedId == R.id.rbt_multiple;
            toggleSingleOrMultiRates();
        });

        btCategory.setOnClickListener(v -> {
            isTypeItem = false;
            toggleItemOrCategory();
        });
        btItem.setOnClickListener(v -> {
            isTypeItem = true;
            toggleItemOrCategory();
        });

        btSave.setOnClickListener(v -> getDataAndSave());

    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private BottomSheetBehavior.BottomSheetCallback getBottomSheetCallback() {

        return new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    editEntryid = "";
                }
            }

            @Override
            public void onSlide(@NonNull View view, float drag) {


            }
        };
    }

    private void toggleItemOrCategory() {
        Resources r = btItem.getContext().getResources();

        if (isTypeItem) {
            btItem.setBackgroundResource(R.drawable.bg_curved_accent);
            btItem.setTextColor(r.getColor(R.color.white_fff));
            btCategory.setBackgroundResource(R.drawable.bg_curved_white_outline_accent_dotted);
            btCategory.setTextColor(r.getColor(R.color.colorAccent));


            llItemName.setVisibility(View.VISIBLE);
            rgRates.setVisibility(View.VISIBLE);
            toggleSingleOrMultiRates();

        } else {
            btCategory.setBackgroundResource(R.drawable.bg_curved_accent);
            btCategory.setTextColor(r.getColor(R.color.white_fff));
            btItem.setBackgroundResource(R.drawable.bg_curved_white_outline_accent_dotted);
            btItem.setTextColor(r.getColor(R.color.colorAccent));


            llItemName.setVisibility(View.INVISIBLE);


        }
    }

    private void toggleSingleOrMultiRates() {

        if (isRateMulti) {

            ((RadioButton) rgRates.findViewById(R.id.rbt_multiple)).setChecked(true);
            ((RadioButton) rgRates.findViewById(R.id.rbt_single)).setChecked(false);

            tilHalf.setVisibility(View.VISIBLE);
            tilFull.setVisibility(View.VISIBLE);

            tilSingle.setVisibility(View.GONE);

        } else {
            ((RadioButton) rgRates.findViewById(R.id.rbt_multiple)).setChecked(false);
            ((RadioButton) rgRates.findViewById(R.id.rbt_single)).setChecked(true);
            tilHalf.setVisibility(View.GONE);
            tilFull.setVisibility(View.GONE);

            tilSingle.setVisibility(View.VISIBLE);
        }
    }

    private void getDataAndSave() {

        String categoryName = etCategory.getText().toString();
        MenuEntry entry;
        if (!isTypeItem)
            entry = new MenuEntry(categoryName);
        else {
            String itemName = etItem.getText().toString();
            if (isRateMulti) {
                String half = etPrHalf.getText().toString().equals("") ? "0" : etPrHalf.getText().toString();
                int rateH = Integer.parseInt(half);

                String full = etPrFull.getText().toString().equals("") ? "0" : etPrFull.getText().toString();
                int rateF = Integer.parseInt(full);

                entry = new MenuEntry(categoryName, itemName, rateH, rateF);
            } else {
                String rateS = etPrSingle.getText().toString().equals("") ? "0" : etPrSingle.getText().toString();
                int rate = Integer.parseInt(rateS);
                entry = new MenuEntry(categoryName, itemName, rate);
            }
        }

        if (editEntryid != null && !editEntryid.equals("")) {
            entry.setId(editEntryid);
        }

        if(menuEntryListActivityVM!=null){
            menuEntryListActivityVM.insertOrUpdateMenuItems(entry);

        }
        else {
            Log.e(TAG, "getDataAndSave: could not update db, view model is null" );
        }

        toggleBottomSheet();


    }

    public void openForEditing(@Nullable MenuEntry editThisEntry) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (editThisEntry != null) {
            this.editEntryid = editThisEntry.getId();

            etCategory.setText(editThisEntry.getCategoryName());

            int type = editThisEntry.getType();
            isTypeItem = type == MenuEntryType.ITEM.ordinal();
            toggleItemOrCategory();

            if (isTypeItem) {
                etItem.setText(editThisEntry.getItemName());


                isRateMulti = editThisEntry.getPriceHalf() != 0;
                toggleSingleOrMultiRates();

                if (isRateMulti) {
                    etPrHalf.setText("" + editThisEntry.getPriceHalf());
                    etPrFull.setText("" + editThisEntry.getPriceFull());
                } else {
                    etPrSingle.setText("" + editThisEntry.getPriceFull());
                }
            }
        }
    }


    public void addHintsAdapterForItemName(Context ctx, List<String> items) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (ctx, android.R.layout.select_dialog_item, items);
        if (etItem != null) {
            etItem.setThreshold(1);
            etItem.setAdapter(adapter);
        }

    }

    public void addHintsAdapterForCategory(Context ctx, List<String> categories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (ctx, R.layout.layout_autocomplete, categories);
        if (etCategory != null) {
            etCategory.setThreshold(0);
            etCategory.setAdapter(adapter);
        }

    }


    //----------------not useful now-------------------------------------


    public void downLoad(Context ctx, String filename, String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadQ(ctx, filename, data);
        } else {
            downLoadCompat(ctx, filename, data);
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private void downloadQ(Context ctx, String fileName, String data) {
        Log.e(TAG, "downloadQ: Android Q version ran");
        ContentValues myContentValues = new ContentValues();
        myContentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        String myFolder = "Download/downloadQ2";
        myContentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, myFolder);
        myContentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
        myContentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);

        Uri extVolumeUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);

        ContentResolver contentResolver = ctx.getContentResolver();
        Uri uri = contentResolver.insert(extVolumeUri, myContentValues);

        if (uri == null) {
            Log.e(TAG, "downloadQ: uri is null, quitting operation and returning");
            return;
        }
        Log.e(TAG, "downloadQ: uri=" + uri);

        try {
            FileOutputStream fos = new FileOutputStream(new File(uri.toString()));
//                    ctx.openFileOutput(uri.toString(), Context.MODE_PRIVATE);//try with uri.getPath()
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "downloadQ: error occurred" + e.getMessage());
            e.printStackTrace();
        } finally {
            myContentValues.clear();
            myContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0);
            contentResolver.update(uri, myContentValues, null, null);
        }

    }

    private void downLoadCompat(Context ctx, String filename, String data) {
        Log.e(TAG, "downLoadCompat: legacy version ran");
    }


}

/*

    private fun checkIfLegacy() {

        val  isQAndUsesLegacyStorage =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Environment.isExternalStorageLegacy()

        val isLegacyMessage =
            if (isQAndUsesLegacyStorage) { "This app has legacy external storage" }
            else { "This app will use new Scoped storage" }

        Log.e(TAG, "checkIfLegacy: $isLegacyMessage")
    }

private val collection =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
    MediaStore.Video.Media.getContentUri( MediaStore.VOLUME_EXTERNAL ) else MediaStore.Video.Media.EXTERNAL_CONTENT_URI


  suspend fun download(filename: String): Uri =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) downloadQ(filename)
    else downloadLegacy(filename)

  private suspend fun downloadQ(filename: String): Uri =
    withContext(Dispatchers.IO) {
      val url = URL_BASE + filename
      val response = ok.newCall(Request.Builder().url(url).build()).execute()

      if (response.isSuccessful) {
        val values = ContentValues().apply {
          put(MediaStore.Video.Media.DISPLAY_NAME, filename)
          put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/ConferenceVideos")
          put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
          put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(collection, values)

        uri?.let {
          resolver.openOutputStream(uri)?.use { outputStream ->
            val sink = Okio.buffer(Okio.sink(outputStream))

            response.body()?.source()?.let { sink.writeAll(it) }
            sink.close()
          }

          values.clear()
          values.put(MediaStore.Video.Media.IS_PENDING, 0)
          resolver.update(uri, values, null, null)
        } ?: throw RuntimeException("MediaStore failed for some reason")

        uri
      } else {
        throw RuntimeException("OkHttp failed for some reason")
      }
    }

  private suspend fun downloadLegacy(filename: String): Uri =
    withContext(Dispatchers.IO) {
      val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
        filename
      )
      val url = URL_BASE + filename
      val response = ok.newCall(Request.Builder().url(url).build()).execute()

      if (response.isSuccessful) {
        val sink = Okio.buffer(Okio.sink(file))

        response.body()?.source()?.let { sink.writeAll(it) }
        sink.close()

        MediaScannerConnection.scanFile(
          context,
          arrayOf(file.absolutePath),
          arrayOf("video/mp4"),
          null
        )

        FileProvider.getUriForFile(context, AUTHORITY, file)
      } else {
        throw RuntimeException("OkHttp failed for some reason")
      }
    }
}



 */
/*

In Android API 29 and above you can use the below code to store files, images and videos to external storage.

//First, if your picking your file using "android.media.action.IMAGE_CAPTURE" then store that file in applications private Path(getExternalFilesDir()) as below.

File destination = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
//Then use content provider to get access to Media-store as below.

 ContentValues values = new ContentValues();
 values.put(MediaStore.Images.Media.TITLE, fileName);
 values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
//if you want specific mime type, specify your mime type here. otherwise leave it blank, it will take default file mime type

values.put(MediaStore.Images.Media.MIME_TYPE, "MimeType");
values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "path"); // specify
 storage path
// insert to media-store using content provider, it will return URI.

Uri uri = cxt.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
//Use that URI to open the file.

ParcelFileDescriptor descriptor = context.getContentResolver().openFileDescriptor(uri,"w"); //"w" specify's write mode
FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
// read file from private path.

InputStream dataInputStream = cxt.openFileInput(privatePath_file_path);
//write file into out file-stream.

 OutputStream output = new FileOutputStream(fileDescriptor);
 byte[] buf = new byte[1024];
 int bytesRead;
 while ((bytesRead = dataInputStream.read(buf)) > 0)
 {
  output.write(buf, 0, bytesRead);
 }
 datanputStream.close();
 output.close();
}

* */
/*

    public void stringtopdf(String data) {
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        File fol = new File(extstoragedir, "pdf");
        File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdir();
        }
        try {
            final File file = new File(folder, "sample.pdf");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText(data, 10, 10, paint);
            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

        } catch (IOException e) {
            Log.e(TAG, "stringtopdf:" + e.getMessage());
        }

    }
    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File textFile = new File(root, sFileName);
            FileWriter writer = new FileWriter(textFile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(btSave.getContext(), "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "generateNoteOnSD: errpor happenned" + e.getMessage());
        }
    }
    private void saveText(Context context, String data, String fileName) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String relativeLocation = Environment.DIRECTORY_DOWNLOADS + File.separator + "YourSubfolderName";
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
            }

            Uri contentUri = MediaStore.Files.getContentUri("external");
            uri = resolver.insert(contentUri, contentValues);
            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            FileOutputStream stream = new FileOutputStream(uri.toString());
            stream.write(data.getBytes());
            stream.close();
            Toast.makeText(context, "saveText:Success", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e(TAG, "saveTextError: " + e.getMessage());

            if (uri != null) {
                resolver.delete(uri, null, null);
            }

        }
    }
    public void createMyPDF(String data) {

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = data;
        int x = 10, y = 25;

        for (String line : myString.split("\n")) {
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y += myPaint.descent() - myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/createMyPDF.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "createMyPDF: " + e.getMessage());
        } finally {
            myPdfDocument.close();
        }

    }
    private void saveText2(Context context, String data, String fileName) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String myFolder = Environment.DIRECTORY_DOWNLOADS + File.separator + "YourSubfolderName";
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, myFolder);
            }

            Uri contentUri = MediaStore.Files.getContentUri("external");
            uri = contentResolver.insert(contentUri, contentValues);
            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            InputStream inputStream = contentResolver.openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(uri.toString());

            byte[] buffer = data.getBytes();

            int length;
            if (inputStream != null) {
                while ((length = inputStream.read(buffer)) > 0) {

                    outputStream.write(buffer, 0, length);

                }
                inputStream.close();

            }
            outputStream.close();
            Toast.makeText(context, "saveText:Success", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e(TAG, "saveText2 Error: " + e.getMessage());

            if (uri != null) {
                contentResolver.delete(uri, null, null);
            }

        }
    }

* */
