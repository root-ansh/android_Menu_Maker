/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:09 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.components_preview

import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.MenuEntry
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class PreviewAndConvertActivity : AppCompatActivity() {
    private var wwPrev: WebView? = null;
    private var fabPdf: FloatingActionButton? = null;
    private val TAG = "PrevAndConvert>>";

    private var viewmodel: PreviewAndConverActivityVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_and_convert)

        initDbVariables();
        initUi();

    }

    private fun initDbVariables() {
        viewmodel = ViewModelProvider.AndroidViewModelFactory(this.application).create(
            PreviewAndConverActivityVM::class.java
        )
    }

    private fun initUi() {
        wwPrev = findViewById(R.id.web_wiew_prev);
        fabPdf = findViewById(R.id.fab_pdf);
    }

    override fun onStart() {
        super.onStart()

        startListenersAndDbVars();

    }

    private fun startListenersAndDbVars() {
        fabPdf?.setOnClickListener { createPDF() }

        viewmodel?.allEntriesLivedata?.observe(
            this,
            androidx.lifecycle.Observer { generateAndLoadHtml(it) })

    }


    private fun generateAndLoadHtml(entrylist: List<MenuEntry>?) {
        var htmlString: String?
        if (entrylist == null) {
            htmlString = "<HTML><H1>Loading...</H1></HTML>";
        } else {
            htmlString = "";

            val start = "<html><head>\n" +
                    "<style type=\"text/css\" media=\"print\">@page{size:  auto; margin: 0mm;}" +
                    "html{background-color: #FFFFFF;margin: 0px;}body{padding: 1%;}</style>\n" +
                    "<style>body {background-color: #efffea; }table {margin: auto;width: calc(100% -" +
                    " 40px);}td {text-align: center;vertical-align: middle;}th {background-color: " +
                    "#8cba51;color: white;}tr{background-color: white;}.x{background-color:#efffea;}" +
                    ".c{background-color:#efffea; color:black}</style>\n" +
                    "</head><body><H1> Menu </H1><table cellspacing=\"0\">"
            var middle = "";
            for(i in entrylist){
                if(i.type==MenuEntry.Type.CATEGORY){
                    val category =""+
                            "<tr><th colspan=3 class=\"x\"><br></th></tr><tr><th colspan=3 " +
                            "class=\"c\">${i.categoryName}</th></tr><tr><th width=40%>ItemName</th><th>" +
                            "Rate(Half)</th><th>Rate(Full)</th></tr>";

                    middle+=category
                }
                else{
                    val pHalf =  if(i.priceHalf>0) i.priceHalf.toString()else "-"
                    val item = ""+
                            "<tr><td>${i.itemName}</td><td>${pHalf }</td><td>${i.priceFull}</td></tr>"
                    middle+=item;
                }
            }

            val end ="</table></body></html>\n"
            htmlString+=start+middle+end

            Log.e(TAG, "initUi: html=\n$htmlString")
            wwPrev?.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);

        }
    }


    private fun createPDF() {
        val dateFormat = SimpleDateFormat("dd.MM.yy,h.mm a", Locale.ROOT)
        val jobName = "Menu ${dateFormat.format(Calendar.getInstance().time)}.pdf";
        val printManager: PrintManager = getSystemService(Context.PRINT_SERVICE) as PrintManager;
        val printAdapter = wwPrev?.createPrintDocumentAdapter(jobName);

        val printAttributes =
            PrintAttributes.Builder().setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()

        if (printAdapter != null) {
            printManager.print(jobName, printAdapter, printAttributes);
        }

    }


}