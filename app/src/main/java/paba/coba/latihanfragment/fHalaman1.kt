package paba.coba.latihanfragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fHalaman1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_halaman1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _tvCurrentScore = view.findViewById<TextView>(R.id.tvCurrentScore)
        _tvCurrentScore.text = MainActivity.playerScore.toString()

        gameLogic(_tvCurrentScore)

        view.findViewById<Button>(R.id.btnGiveUp).setOnClickListener {
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNav).selectedItemId =
                R.id.score_menu
        }
    }

    private fun gameLogic(_tvCurrentScore: TextView) {
//        Set semua button text dengan angka random
        val fragmentView = view ?: return

        val buttonIds = listOf(
            R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE,
            R.id.btnF, R.id.btnG, R.id.btnH, R.id.btnI, R.id.btnJ
        )

        val btnArray =
            buttonIds.map { id -> fragmentView.findViewById<Button>(id) }.toTypedArray()
        btnArray.shuffle()

        var cardNumber = MainActivity.startNumber
        for (i in btnArray.indices step 2) {
            btnArray[i]?.text = cardNumber.toString()
            btnArray[i + 1]?.text = cardNumber.toString()
            cardNumber++
        }
//        End of Set semua button text dengan angka random

        MainActivity.playerScore = 50
        _tvCurrentScore.text = MainActivity.playerScore.toString()

//        Game Logic
        var opened1: Button? = null
        var opened2: Button? = null
        var matchedPairs = 0
        val totalPairs = btnArray.size / 2

//        Set semua button ada setOnClickListener
        btnArray.forEach { button ->
            button?.setOnClickListener {
                // Jika di click akan menampilkan angka nya dan disable button tersebut
                button.setTextColor(Color.BLACK)
                button.isEnabled = false

                if (opened1 == null) {
                    opened1 = button
                } else {
                    opened2 = button

                    // Pengecekan apakah angkanya sama (true) atau tidak (false)
                    val isMatch = opened1?.text == opened2?.text
                    updateScoreAndColor(isMatch, opened1, opened2, _tvCurrentScore)

                    if (isMatch) { // Cek kalau benar dan jumlah benar sudah memenuhi total benar maka game berakhir
                        matchedPairs++
                        if (matchedPairs == totalPairs) navigateToScoreScreen()
                    } else if (MainActivity.playerScore <= 0) { // Cek kalau score sudah habis maka game berakhir
                        navigateToScoreScreen()
                    }

                    resetOpenedButtonsDelayed(isMatch, opened1, opened2)
                    opened1 = null
                    opened2 = null
                }
            }
        }
    }

    private fun updateScoreAndColor(isMatch: Boolean, button1: Button?, button2: Button?, scoreView: TextView) {
        // Cek kalau match +10 poin, kalau not match -5 poin
        MainActivity.playerScore += if (isMatch) 10 else -5

        // Update tv score view dan warna textnya
        scoreView.text = MainActivity.playerScore.toString()
        scoreView.setTextColor(if (isMatch) Color.parseColor("#008000") else Color.RED)

        // Delay warna text menjadi hitam setelah 1 detik
        scoreView.postDelayed({
            scoreView.setTextColor(Color.BLACK)
        }, 1000)
        // Update warna text button1 dan button2, kalau match warna hijau, kalau not match warna merah
        button1?.setTextColor(if (isMatch) Color.parseColor("#008000") else Color.RED)
        button2?.setTextColor(if (isMatch) Color.parseColor("#008000") else Color.RED)
    }

    private fun resetOpenedButtonsDelayed(isMatch: Boolean, button1: Button?, button2: Button?) {
        // Kalau not match button text kembali hidden dan enabled, delay 1 detik
        if (!isMatch) {
            button1?.postDelayed({
                button1?.setTextColor(Color.TRANSPARENT)
                button2?.setTextColor(Color.TRANSPARENT)
                button1?.isEnabled = true
                button2?.isEnabled = true
            }, 1000)
        }
    }

    private fun navigateToScoreScreen() {
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNav).selectedItemId = R.id.score_menu
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = fHalaman1().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}
