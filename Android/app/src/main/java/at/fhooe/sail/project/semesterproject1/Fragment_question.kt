package at.fhooe.sail.project.semesterproject1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment_question : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    // TODO: Rename and change types of parameters
    val db: FirebaseFirestore = Firebase.firestore
    private var sessionId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionId = it.getString("sessionId")
            userId = it.getString("userId")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    private fun addQuestion(questionText: String) {
        if (sessionId != null) {
            db.collection("Session")
                    .document(sessionId!!)
                    .update(
                            "questionList",
                            FieldValue.arrayUnion(hashMapOf("text" to questionText))
                    )
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Question submitted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "Could not submit", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Fragment_question().apply {
                    arguments =
                            Bundle().apply {
                                putString(ARG_PARAM1, param1)
                                putString(ARG_PARAM2, param2)
                            }
                }
    }
}
