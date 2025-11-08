package at.fhooe.sail.project.semesterproject1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import at.fhooe.sail.project.semesterproject1.databinding.FragmentQuestionBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Constants for fragment initialization parameters
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Fragment_question : Fragment() {

    // Variables to hold initialization parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db: FirebaseFirestore = Firebase.firestore

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
        val binding = FragmentQuestionBinding.inflate(inflater, container, false)

        binding.fragmentQuestionButtonSend.setOnClickListener {
            val questionInput = binding.fragmentQuestionTextInput.editText
            if (questionInput != null) {
                // Add the question to Firestore and clear the input field
                addQuestion(questionInput.text.toString())
                questionInput.setText("")
            }
        }

        return binding.root
    }

    // Method to add a question to the Firestore database
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
        // Factory method to create a new instance of this fragment using the provided parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_question().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
