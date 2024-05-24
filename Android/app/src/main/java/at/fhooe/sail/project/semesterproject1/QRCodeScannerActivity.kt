package at.fhooe.sail.project.semesterproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.fhooe.sail.project.semesterproject1.databinding.ActivityQrscannerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class QRCodeScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityScanQRButton.setOnClickListener {
            startScanning()
        }
    }

    private fun startScanning() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val scannedData = barcode.rawValue ?: ""
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                createUser(scannedData)
            }
            .addOnCanceledListener {
                Toast.makeText(this, "Scan abgebrochen", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Scan fehlgeschlagen", e)
            }
    }

    private fun createUser(scannedData: String) {
        val resultsTextView = binding.scanResults
        resultsTextView.text = scannedData

        val userData = hashMapOf(
            "status" to "done"
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("Session")
            .document(scannedData)
            .collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

                // Store SessionID and UserID in SharedPreferences
                val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("SessionID", scannedData)
                    putString("UserID", documentReference.id)
                    apply()
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    companion object {
        private const val TAG = "QRCodeScannerActivity"
    }
}