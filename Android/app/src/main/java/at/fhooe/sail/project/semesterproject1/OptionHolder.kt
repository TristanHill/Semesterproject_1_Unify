package at.fhooe.sail.android.lists.a

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.project.semesterproject1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var selected = -1

/**
 * ViewHolder class for the options in the survey.
 * Handles the click events and updates the Firestore database with the selected option.
 */
class OptionHolder(val root: View) : RecyclerView.ViewHolder(root) {
    var mContent: TextView
    val db: FirebaseFirestore = Firebase.firestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sessionID: String
    private lateinit var userID: String

    /**
     * Initialize the ViewHolder and set up the click listener for the root view.
     */
    init {
        // Initialize the TextView from the layout
        mContent = root.findViewById(R.id.fragment_survey_option_tv) as TextView

        root.setOnClickListener {
            // Send the selected answer to Firestore
            db.collection("Session").document(sessionID!!).collection("User").document(userID!!)
                .update("surveyOption", adapterPosition)
            Toast.makeText(root.context, "Option (${mContent.text}) selected", Toast.LENGTH_SHORT).show()
        }
    }
}
