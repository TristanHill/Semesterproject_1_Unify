package at.fhooe.sail.project.semesterproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import at.fhooe.sail.project.semesterproject1.databinding.ActivityQrscannerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QRCodeScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Scan canceled", Toast.LENGTH_LONG).show()
        } else {
            val scannedData = result.contents
            Toast.makeText(this, "scanned: $scannedData", Toast.LENGTH_LONG).show()
            val i:Intent= Intent(this, MainActivity::class.java)
            startActivity(i)
            createUser(scannedData)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityScanQRButton.setOnClickListener {
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
        val resultsTextView = binding.scanResults
        resultsTextView.text = scannedData

        val userData = hashMapOf(
            "status" to "Done"
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("Session")
            .document(scannedData)
            .collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                val intent = Intent().apply {
                    putExtra("SessionId", scannedData)
                    putExtra("UserId", documentReference.id)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}
