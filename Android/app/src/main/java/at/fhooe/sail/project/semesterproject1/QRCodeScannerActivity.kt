package at.fhooe.sail.project.semesterproject1

import android.os.Bundle
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
            updateResults(scannedData)
            sendToFirebase(scannedData)
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

    private fun updateResults(scannedData: String) {
        val resultsTextView: TextView = findViewById(R.id.scan_results)
        resultsTextView.text = scannedData
    }

    private fun sendToFirebase(scannedData: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("ScannedData").add(mapOf("data" to scannedData))
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Data Succesfully Transferred. ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error Ocurred when Transferred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
