package com.vicpin.cabifychallenge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.vicpin.cabifychallenge.R
import com.vicpin.cabifychallenge.fragment.CheckoutFragment
import com.vicpin.cabifychallenge.fragment.ItemListFragment

class MainActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.container, ItemListFragment()).commit()
        supportFragmentManager.beginTransaction().add(R.id.bottomSheet, CheckoutFragment()).commit()
    }
}
