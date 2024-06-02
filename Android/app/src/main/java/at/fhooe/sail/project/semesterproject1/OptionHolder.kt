package at.fhooe.sail.android.lists.a

import android.content.SharedPreferences
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.project.semesterproject1.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class OptionHolder(val root: View): RecyclerView.ViewHolder(root) {
    var mContent: TextView
    val db: FirebaseFirestore = Firebase.firestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sessionID: String
    private lateinit var userID: String
    init {
        mContent = root.findViewById(R.id.fragment_survey_option_tv) as TextView

        root.setOnClickListener {
            //send answer to firebase
         /*   db.collection("Session").document(sessionID!!).collection("User").document(userID!!)
                .update("surveyOption", adapterPosition) */
            Toast.makeText(root.context, "Option (${mContent.text.toString()}) ausgewählt", Toast.LENGTH_SHORT).show()
        }

    }

}