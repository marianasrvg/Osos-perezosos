package mx.iteso.focusfocus

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.transition.Fade
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fragment_work.*
import mx.iteso.focusfocus.Fragments.FragmentStats
import mx.iteso.focusfocus.Fragments.FragmentTasks
import mx.iteso.focusfocus.Fragments.FragmentWork

class ActivityMain : AppCompatActivity(),
        FragmentWork.OnFragmentInteractionListener,
        FragmentTasks.OnFragmentInteractionListener,
        FragmentStats.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var navigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUIElements()
        setSupportActionBar(toolbar_work)
        /*setSupportActionBar(toolbar_settings)*/
    }

    private fun initUIElements() {
        navigationBar = findViewById(R.id.bottomNav)
        initNavigationBar()
    }

    private fun initNavigationBar() {
        val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
        mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener(
                function = { item: MenuItem ->
                    when (item.itemId) {
                        R.id.navBAction -> {
                            val transaction = supportFragmentManager.beginTransaction()
                            Log.d("Fragment", "Next rides")

                            val nextFragment = FragmentWork.newInstance()
                            nextFragment.setEnterTransition(Fade(1))
                            nextFragment.setExitTransition(Fade(2))
                            transaction.replace(R.id.fragmentMain, nextFragment)
                            transaction.addToBackStack(null).commit()
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navBActivities -> {
                            val transaction = supportFragmentManager.beginTransaction()
                            Log.d("Fragment", "Next rides")

                            val nextFragment = FragmentTasks.newInstance()
                            nextFragment.setEnterTransition(Fade(1))
                            nextFragment.setExitTransition(Fade(2))
                            transaction.replace(R.id.fragmentMain, nextFragment)
                            transaction.addToBackStack(null).commit()
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navBStats -> {
                            val transaction = supportFragmentManager.beginTransaction()
                            Log.d("Fragment", "Next rides")

                            val nextFragment = FragmentStats()
                            nextFragment.setEnterTransition(Fade(1))
                            nextFragment.setExitTransition(Fade(2))
                            transaction.replace(R.id.fragmentMain, nextFragment)
                            transaction.addToBackStack(null).commit()
                            return@OnNavigationItemSelectedListener true
                        }
                        else -> {
                            return@OnNavigationItemSelectedListener false
                        }
                    }
                })
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigationBar.selectedItemId = R.id.navBAction
    }

}
