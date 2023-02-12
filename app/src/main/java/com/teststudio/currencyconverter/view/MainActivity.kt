package com.teststudio.currencyconverter.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.teststudio.currencyconverter.R
import com.teststudio.currencyconverter.util.FragmentListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = HomeFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
        }
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
        }
    }
}