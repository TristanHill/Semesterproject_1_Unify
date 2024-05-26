package at.fhooe.sail.project.semesterproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.fhooe.sail.project.semesterproject1.databinding.ActivityQrscannerBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class QRCodeScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding
    val db: FirebaseFirestore = Firebase.firestore

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

        val userData = hashMapOf(
            "status" to "Done"
        )

        db.collection("Session")
            .document(scannedData)
            .collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

                // Store SessionID and UserID in SharedPreferences
                // TODO Send SessionID, UserID and user Name in the QR code and seperate them to put into shared preferences
                val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("SessionID", scannedData)
                    // putString("UserID", scannedData)
                    // putString("SessionName, scannedData)
                    apply()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    companion object {
        private const val TAG = "QRCodeScannerActivity"
    }
}
