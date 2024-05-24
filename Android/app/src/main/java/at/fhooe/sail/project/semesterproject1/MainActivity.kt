package at.fhooe.sail.project.semesterproject1

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import at.fhooe.sail.project.semesterproject1.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

const val TAG: String = "Main"
class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val sessionID = sharedPref.getString("SessionID", null)
        val userID = sharedPref.getString("UserID", null)

        db = Firebase.firestore

        replaceFragment(Fragment_question())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.status_item -> replaceFragment(Fragment_status())
                R.id.question_item -> replaceFragment(Fragment_question())
                R.id.survey_item -> replaceFragment(Fragment_survey())
                else -> {}
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment)
        fragmentTransaction.commit()
    }

       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_join_session_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Button zum Starten der QRCodeScannerActivity
        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val sessionId = result.data?.getStringExtra("SessionId")
                val userId = result.data?.getStringExtra("UserId")
                Log.w(TAG,result.data?.getStringExtra("SessionId").toString())
                Log.w(TAG,result.data?.getStringExtra("UserId").toString())

                if (sessionId != null && userId != null) {
                    db.collection("Session").document(sessionId)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }

                            if (snapshot != null && snapshot.exists()) {
                                Log.d(TAG, "Current data: ${snapshot.data}")
                            } else {
                                db.collection("Session")
                                    .document(sessionId)
                                    .collection("User")
                                    .document(userId).delete()
                            }
                        }
                }
            }
        }
        val scannerButton: Button = findViewById(R.id.activity_main_button_scanner)
        scannerButton.setOnClickListener {
            startForResult.launch(Intent(this, QRCodeScannerActivity::class.java))
        } */
}
