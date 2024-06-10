package at.fhooe.sail.project.semesterproject1

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import at.fhooe.sail.project.semesterproject1.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val db: FirebaseFirestore = Firebase.firestore

    // Late initialization of shared preferences to store session and user information
    private lateinit var sharedPref: SharedPreferences

    private lateinit var sessionID: String
    private lateinit var userID: String

    // HashMap to store survey data, if any
    private var surveyHashMap: HashMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Retrieve session ID and user ID from shared preferences
        sessionID = sharedPref.getString("SessionID", null)!!
        userID = sharedPref.getString("UserID", null)!!

        // Replace the current fragment with the status fragment
        replaceFragment(Fragment_status())

        // Set up the navigation view
        setNavigationView()

        // Retrieve the session name text view and set up a Firestore listener for session data
        val sessionName = findViewById<TextView>(R.id.toolbar_session_name)
        if (sessionID != null) {
            db.collection("Session").document(sessionID)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("Firestore", "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("Firestore", "Current data: ${snapshot.data}")

                        surveyHashMap = snapshot.data?.get("survey") as HashMap<String, Any>?

                        // Update the UI based on whether survey data exists or not
                        if (surveyHashMap == null) {
                            db.collection("Session").document(sessionID).collection("User").document(userID)
                                .update("surveyOption", -1)

                            if (supportFragmentManager.findFragmentById(R.id.activity_main_frameLayout)!!::class == Fragment_survey::class) {
                                replaceFragment(Fragment_nosurvey())
                            }

                        } else {
                            if (supportFragmentManager.findFragmentById(R.id.activity_main_frameLayout)!!::class == Fragment_nosurvey::class) {
                                replaceFragment(Fragment_survey())
                            }
                        }

                        setNavigationView()

                        sessionName.text = snapshot.data?.get("name").toString()
                    } else {
                        Log.d("Firestore", "Document has been deleted!")
                        deleteUser()
                    }
                }
        }

        // Set up the leave button to delete the user and finish the activity when clicked
        val leaveButton = findViewById<ImageView>(R.id.toolbar_leave_session)
        leaveButton.setOnClickListener {
            deleteUser()
        }
    }

    private fun setNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.status_item -> {
                    replaceFragment(Fragment_status())
                }
                R.id.question_item -> replaceFragment(Fragment_question())
                R.id.survey_item -> {
                    if (surveyHashMap == null) {
                        replaceFragment(Fragment_nosurvey())
                    } else {
                        replaceFragment(Fragment_survey())
                    }
                }
                else -> {}
            }
            true
        }
    }

    // Method to replace the current fragment with the specified fragment
    private fun replaceFragment(fragment: Fragment) {
        val bundle = bundleOf("sessionId" to sessionID, "userId" to userID)
        fragment.arguments = bundle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.activity_main_frameLayout, fragment)
        }
    }

    // Method to delete the user from the Firestore database and clear shared preferences
    private fun deleteUser() {
        if (sessionID != null && userID != null) {
            db.collection("Session").document(sessionID).collection("User").document(userID)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "User successfully deleted!")
                    with(sharedPref.edit()) {
                        remove("SessionID")
                        remove("UserID")
                        apply()
                    }
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error deleting user", e)
                }
        }
    }
}
