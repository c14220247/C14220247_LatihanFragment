package paba.coba.latihanfragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frameContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        Fragment Manager
        val mFragmentManager = supportFragmentManager

//        Panggil Fragment 1 ke Fragment Container
        val mfHalaman1 = fHalaman1()
        mFragmentManager.findFragmentByTag(fHalaman1::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, mfHalaman1, fHalaman1::class.java.simpleName)
            .commit()

//        Membuat Routing Navigation ke setiap fragment menggunakan Bottom Navigation View
        val _botNavView = findViewById<BottomNavigationView>(R.id.bottomNav)
        _botNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.game_menu -> {
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer, fHalaman1(), fHalaman1::class.java.simpleName)
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.score_menu -> {
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer, fHalaman2(), fHalaman2::class.java.simpleName)
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.setting_menu -> {
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer, fHalaman3(), fHalaman3::class.java.simpleName)
                        .commit()
                    return@setOnItemSelectedListener true
                }

                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

//    Membuat variable untuk menyimpan Player Score
    companion object {
        var startNumber = 1
        var playerScore = 50
    }
}