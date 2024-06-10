package at.fhooe.sail.project.semesterproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.fhooe.sail.project.semesterproject1.databinding.ActivityQrscannerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class QRCodeScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding

    // Firebase Firestore instance to interact with the Firestore database
    val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a click listener for the "Scan QR" button
        binding.activityScanQRButton.setOnClickListener { startScanning() }
    }

    private fun startScanning() {
        // Configure the scanner
        val options = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()

        // Get a scanner client with the specified options
        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Retrieve the scanned data from the barcode
                val scannedData = barcode.rawValue ?: ""
                // Call the createUser method with the scanned data
                createUser(scannedData)
            }
            .addOnCanceledListener {
                // Show a toast message if the scan is canceled
                Toast.makeText(this, "Scan abgebrochen", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                // Log an error message if the scan fails
                Log.e(TAG, "Scan fehlgeschlagen", e)
            }
    }

    // Method to create a user in the Firestore database using the scanned QR code data
    private fun createUser(scannedData: String) {
        // Create a hashmap with user data
        val userData = hashMapOf("status" to "In Progress")

        // Add the user data to the "User" collection under the "Session" document identified by the scanned data
        db.collection("Session")
            .document(scannedData)
            .collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

                // Save the session ID and user ID to shared preferences
                val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("SessionID", scannedData)
                    putString("UserID", documentReference.id)
                    apply()
                }

                // Create an intent to start the MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // Log a warning message if there is an error adding the document
                Log.w(TAG, "Error adding document", e)
            }
    }

    // Companion object to hold constants
    companion object {
        private const val TAG = "QRCodeScannerActivity"
    }
}
