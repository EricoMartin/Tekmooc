package com.example.tekmooc.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.tekmooc.R
import com.example.tekmooc.databinding.FragmentProfileBinding
import com.example.tekmooc.ui.auth.SignInActivity
import com.example.tekmooc.util.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var img: ImageView? = null
    private var toolbar: Toolbar? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        toolbar = binding.profileToolbar.toolbar
        auth = Firebase.auth
        Timber.d("onCreateView called: ", savedInstanceState)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.setNavigationIcon(R.drawable.ic_backspace)
        toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
        toolbar?.inflateMenu(R.menu.main_menu)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.action_signout -> {
                    auth.signOut()
                    requireContext().startActivity(Intent(context, SignInActivity::class.java))
                    Toast.makeText(context, "User Signed out!", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
        img = binding.userImage
        Glide.with(this).load(R.drawable.ic_baseline_account_circle_24)
            .transition(DrawableTransitionOptions.withCrossFade())
            .thumbnail(0.5f)
            .transform(CircleTransform(context))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(img!!)

        val username: TextView = binding.userName
        username.text = getString(R.string.erico_martin)
        val address: TextView = binding.userAddress
        address.text = getString(R.string.address)


    }

    companion object {
        private val TAG = "ProfileFragment"
    }
}