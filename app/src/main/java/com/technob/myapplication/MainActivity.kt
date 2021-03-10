package com.technob.myapplication

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawer:DrawerLayout
    lateinit var tool:Toolbar
    lateinit var navi:NavigationView
    lateinit var co:CoordinatorLayout
    lateinit var fr:FrameLayout
    var previous:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        opendash()

        drawer=findViewById(R.id.draw)
        tool=findViewById(R.id.tool)
        navi=findViewById(R.id.navi)
        co=findViewById(R.id.co)
        fr=findViewById(R.id.fr)
        setupToolbar()

        val actionbar=ActionBarDrawerToggle(this@MainActivity,drawer,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer.addDrawerListener(actionbar)
        actionbar.syncState()
        navi.setNavigationItemSelectedListener {
            if (previous!=null){
                previous?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previous=it
            when(it.itemId){
                R.id.a1 ->{
                    supportActionBar?.title="Dashboard"
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fr,
                            DashboardFragment()
                        )
                        .commit()
                    drawer.closeDrawers()
                }
                R.id.a2 ->{
                    supportActionBar?.title="Favorites"
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fr,
                            FavoriteFragment()
                        )
                        .commit()
                    drawer.closeDrawers()
                }
                R.id.a3 ->{
                    supportActionBar?.title="About"
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fr,
                            AboutFragment()
                        )
                        .commit()
                    drawer.closeDrawers()                }
                R.id.a4 ->{
                    supportActionBar?.title="Help"
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fr,
                            HelpFragment()
                        )
                        .commit()
                    drawer.closeDrawers()                }
            }
            return@setNavigationItemSelectedListener true
        }
        }


    fun setupToolbar(){
        setSupportActionBar(tool)
        supportActionBar?.title="Dashboard"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.fr)
        when(frag){
            !is DashboardFragment ->{
                supportActionBar?.title="Dashboard"
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fr,
                        DashboardFragment()

                    )
                    .commit()



            }
            else->{

                val dialog= AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("\uD83C\uDD74\uD83C\uDD87\uD83C\uDD78\uD83C\uDD83")
                dialog.setMessage("DO YOU WANT TO CLOSE THE APP")
                dialog.setPositiveButton("OK"){text,listener->finish()}
                dialog.setNegativeButton("CANCEL"){text,listener->}
                dialog.create()
                dialog.show()
            }

        }
    }
    fun opendash(){
        val fra= DashboardFragment()
        val tran=supportFragmentManager.beginTransaction()
         tran.replace(
             R.id.fr,
             DashboardFragment()
         )
            tran.commit()
        supportActionBar?.title="Dashboard"


    }
}