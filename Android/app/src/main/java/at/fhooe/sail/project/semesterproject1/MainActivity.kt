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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

const val TAG: String = "Main"
class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        db = Firebase.firestore

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_join_session_main)) { v, insets ->
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
        }
    }
}
