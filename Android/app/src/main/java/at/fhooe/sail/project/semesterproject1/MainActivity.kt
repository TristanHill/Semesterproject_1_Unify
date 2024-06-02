package at.fhooe.sail.project.semesterproject1

import android.app.Activity
import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.content.SharedPreferences
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import at.fhooe.sail.project.semesterproject1.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

const val TAG: String = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val db: FirebaseFirestore = Firebase.firestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sessionID: String
    private lateinit var userID: String
    private var surveyHashMap: HashMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sessionID= sharedPref.getString("SessionID", null)!!
        userID = sharedPref.getString("UserID", null)!!

        replaceFragment(Fragment_status())
        setNavigationView()

        val sessionName =findViewById<TextView>(R.id.toolbar_session_name)
        if(sessionID != null){
            db.collection("Session").document(sessionID)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("Firestore", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Firestore", "Current data: ${snapshot.data}")

                    surveyHashMap = snapshot.data?.get("survey") as HashMap<String, Any>?

                    if(surveyHashMap == null){
                        db.collection("Session").document(sessionID!!).collection("User").document(userID!!)
                            .update("surveyOption",-1)

                        if(supportFragmentManager.findFragmentById(R.id.activity_main_frameLayout)!!::class==Fragment_survey::class){
                            replaceFragment(Fragment_nosurvey())
                        }

                    } else {
                        if(supportFragmentManager.findFragmentById(R.id.activity_main_frameLayout)!!::class==Fragment_nosurvey::class){
                            replaceFragment(Fragment_survey())
                        }
                    }

                    setNavigationView()

                    sessionName.setText(snapshot.data?.get("name").toString())
                } else {
                    Log.d("Firestore", "Document has been deleted!")
                    deleteUser()
                }
            }
        }

        val leaveButton = findViewById<ImageView>(R.id.toolbar_leave_session)
        leaveButton.setOnClickListener{
            deleteUser()
        }
    }


    private fun setNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.status_item ->{
                    replaceFragment(Fragment_status())
                }
                R.id.question_item -> replaceFragment(Fragment_question())
                R.id.survey_item ->{
                    if(surveyHashMap==null){
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
    private fun replaceFragment(fragment: Fragment){
        val bundle = bundleOf("sessionId" to sessionID, "userId" to userID)
        fragment.arguments = bundle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.activity_main_frameLayout, fragment)
        }

    }

    private fun deleteUser(){
        if(sessionID != null && userID != null) {
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
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting user", e) }
        }
    }
}
