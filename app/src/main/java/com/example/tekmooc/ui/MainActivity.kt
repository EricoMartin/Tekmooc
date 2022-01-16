package com.example.tekmooc.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.tekmooc.R
import com.example.tekmooc.databinding.ActivityMainBinding
import com.example.tekmooc.domain.viewmodels.CourseViewModel
import com.example.tekmooc.ui.auth.SignInActivity
import com.example.tekmooc.util.CircleTransform
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mancj.materialsearchbar.MaterialSearchBar


class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {

    private var navigationView: NavigationView? = null 
    private var drawer: DrawerLayout? = null
    private var navHeader: View? = null
    private var imgNavHeaderBg: ImageView? = null
    private var imgProfile: android.widget.ImageView? = null
    private var txtName: TextView? = null
    private var txtWebsite: TextView? = null
    private lateinit var bottomNav: BottomNavigationView
    private var searchBar: MaterialSearchBar? = null
    private var lastSearches: MutableList<String>? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg"
    private val urlProfileImg =
        "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg"

    // index to identify current nav menu item
    var navItemIndex = 0

    // tags used to attach the fragments
    private val TAG_HOME = "home"
    private val TAG_PROFILE = "profile"
    private val TAG_COURSES = "courses"
    private val TAG_NOTIFICATIONS = "notifications"
    private val TAG_SETTINGS = "settings"
    var CURRENT_TAG = TAG_HOME
    private var navigController: NavController? = null
    private var arrayList = ArrayList<String>()

    //viewbinding declaration
    private lateinit var binding: ActivityMainBinding

    // toolbar titles respected to selected nav menu item
    private var activityTitles: Array<String>? = null

    // flag to load home fragment when user presses back key
    private val shouldLoadHomeFragOnBackPress = true
    private var mHandler: Handler? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mHandler = Handler()

        auth = Firebase.auth
        drawer = binding.drawerLayout
        navigationView = binding.navView
        bottomNav = binding.bottomNavigation
        searchBar = binding.searchBar
        searchBar!!.inflateMenu(R.menu.main_menu)
        searchBar!!.setOnSearchActionListener(this)
        searchBar!!.setCardViewElevation(10);
        lastSearches = java.util.ArrayList<String>()
        arrayList.add(searchBar!!.lastSuggestions.toString())

        navHeader = navigationView!!.getHeaderView(0)

        txtName = navHeader?.findViewById<View>(R.id.name) as TextView
        txtWebsite = navHeader?.findViewById<View>(R.id.website) as TextView
        imgNavHeaderBg = navHeader?.findViewById<View>(R.id.img_header_bg) as ImageView
        imgProfile = navHeader?.findViewById<View>(R.id.img_profile) as ImageView

//        val navController = this.findNavController(R.id.nav_host)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView!!, navController)
        NavigationUI.setupWithNavController(bottomNav, navController)



        // load toolbar titles from string resources
        activityTitles = resources.getStringArray(R.array.nav_item_activity_titles)

        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView()

        navigController = navController
        searchBar!!.menu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener() {
                when (it.getItemId()) {
                    R.id.action_signout -> {
                        auth.signOut()
                        this.startActivity(Intent(this, SignInActivity::class.java))
                        Toast.makeText(applicationContext, "User Signed out!", Toast.LENGTH_LONG)
                            .show()
                    }
                    R.id.action_settings -> {
                        navController.navigate(R.id.settingsFragment)
                    }
                }

            return@OnMenuItemClickListener true
        })

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    // Respond to navigation item 1 click
                    navController.navigate(R.id.homeFragment)
                    searchBar!!.visibility = View.VISIBLE
                    true
                }
                R.id.page_2 -> {
                    // Respond to navigation item 2 click
                    navController.navigate(R.id.profileFragment)
                    searchBar!!.visibility = View.GONE
                    true
                }
                R.id.page_3 -> {
                    // Respond to navigation item 2 click
                    navController.navigate(R.id.courseFragment)
                    searchBar!!.visibility = View.VISIBLE
                    true
                }
                R.id.page_4 -> {
                    // Respond to navigation item 2 click
                    navController.navigate(R.id.notificationsFragment)
                    searchBar!!.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
            true
        }


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host)
        return NavigationUI.navigateUp(navController, drawer)
    }

    private fun loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
//        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) != null) {
            drawer?.closeDrawers();
        }
    }

    override fun onStart() {
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        if (sharedPref.getString("username", "") != ""){
            txtName?.text = sharedPref.getString("username", "")
        } else {
            txtName?.text = "Eric Martins";
        }

        if (sharedPref.getString("username", "") != ""){
            txtWebsite?.text = sharedPref.getString("email", "")
        } else {
            txtWebsite?.text = "www.baseboxng.com";
        }
        super.onStart()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun loadNavHeader() {

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//            .transition(withCrossFade())
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(imgNavHeaderBg!!)

        // Loading profile image (urlProfileImg)
        Glide.with(this).load(R.drawable.tekmooc)
            .transition(withCrossFade())
            .thumbnail(0.5f)
            .transform(CircleTransform(this))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgProfile!!)
    }
//    private fun setToolbarTitle() {
//        supportActionBar!!.title = activityTitles?.get(navItemIndex)
//    }


    private fun selectNavMenu() {
        navigationView!!.menu.getItem(navItemIndex).isChecked = true
    }

    private fun setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu
            //Check to see which item was being clicked and perform appropriate action
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navItemIndex = 0
                    CURRENT_TAG = TAG_HOME
                    navigController?.navigate(R.id.homeFragment)
                    searchBar!!.visibility = View.VISIBLE
                }
                R.id.profileFragment -> {
                    navItemIndex = 1
                    CURRENT_TAG = TAG_PROFILE
                    navigController?.navigate(R.id.profileFragment)
                    searchBar!!.visibility = View.GONE
                }
                R.id.courseFragment -> {
                    navItemIndex = 2
                    CURRENT_TAG = TAG_COURSES
                    navigController?.navigate(R.id.courseFragment)
                    searchBar!!.visibility = View.VISIBLE
                }
                R.id.notificationsFragment -> {
                    navItemIndex = 3
                    CURRENT_TAG = TAG_NOTIFICATIONS
                    navigController?.navigate(R.id.notificationsFragment)
                    searchBar!!.visibility = View.VISIBLE
                }
                R.id.nav_settings -> {
                    navItemIndex = 4
                    CURRENT_TAG = TAG_SETTINGS
                    searchBar!!.visibility = View.GONE
                    navigController?.navigate(R.id.settingsFragment)

                }

                else -> navItemIndex = 0
            }

            //Checking if the item is in checked state or not, if not make it in checked state
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true
            loadHomeFragment()
            true
        }
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            R.string.openDrawer,
            R.string.closeDrawer
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                if (drawerView != null) {
                    super.onDrawerClosed(drawerView)
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                if (drawerView != null) {
                    super.onDrawerOpened(drawerView)
                }
            }
        }

        //Setting the actionbarToggle to drawer layout
        drawer!!.setDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawers()
            return
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0
                CURRENT_TAG = TAG_HOME
                loadHomeFragment()
                return
            }
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (navItemIndex == 1) {
            menuInflater.inflate(R.menu.main_menu, menu)
        }
        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            menuInflater.inflate(R.menu.notifications, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        if (id == R.id.action_signout) {
            auth.signOut()
            Toast.makeText(applicationContext, "User Signed out!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, SignInActivity::class.java))
            return true
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(
                applicationContext,
                "All notifications marked as read!",
                Toast.LENGTH_LONG
            ).show()
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(applicationContext, "Clear all notifications!", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        val s = if (enabled) "enabled" else "disabled"
        Toast.makeText(this@MainActivity, "Search $s", Toast.LENGTH_SHORT).show()
    }

    override fun onSearchConfirmed(text: CharSequence?) {
//        searchBar!!.showSuggestionsList()
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_NAVIGATION -> drawer!!.openDrawer(GravityCompat.START)
            MaterialSearchBar.BUTTON_SPEECH -> {
            }
            MaterialSearchBar.BUTTON_BACK -> searchBar!!.closeSearch()
        }
    }
}