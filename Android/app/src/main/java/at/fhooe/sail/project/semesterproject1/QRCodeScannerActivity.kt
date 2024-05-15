package at.fhooe.sail.project.semesterproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QRCodeScannerActivity : AppCompatActivity() {
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Scan canceled", Toast.LENGTH_LONG).show()
        } else {
            val scannedData = result.contents
            Toast.makeText(this, "scanned: $scannedData", Toast.LENGTH_LONG).show()
            createUser(scannedData)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        val scanButton: Button = findViewById(R.id.scan_button)
        scanButton.setOnClickListener {
            startScanning()
        }
    }

    private fun startScanning() {
        val options = ScanOptions()
        options.setPrompt("Scan a QR-Code")
        options.setBeepEnabled(false)
        options.setOrientationLocked(false)
        barcodeLauncher.launch(options)
    }

    private fun createUser(scannedData: String) {
        val resultsTextView: TextView = findViewById(R.id.scan_results)
        resultsTextView.text = scannedData

        val userData = hashMapOf(
            "status" to "done",
        )


        val db = FirebaseFirestore.getInstance()

        db.collection("Session")
            .document(scannedData)
            .collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                val intent = Intent()
                intent.putExtra("SessionId", scannedData)
                intent.putExtra("UserId", documentReference.id)
                setResult(RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

}
