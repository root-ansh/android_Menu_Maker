/*
 * Copyright (c) 2020.
 * created on 17/6/20 8:38 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens

import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.screens.menu_list.MenuEntryListActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//Splash And Permission Check Activity
// I thought this project would need more permissions, so this was the activity where user
// could provide permissions
class StartActivity : AppCompatActivity() {
    private val TAG = "Splash & PermAct >>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.hide()
        Thread(
            Runnable {
                Thread.sleep(1000)
                runOnUiThread {startComponentActivity()}
            }
        ).start()
    }

    private fun startComponentActivity() {
        startActivity(Intent(this,MenuEntryListActivity::class.java))
        finish()
    }


}