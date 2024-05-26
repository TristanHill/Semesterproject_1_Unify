package at.fhooe.sail.project.semesterproject1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_question.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_question : Fragment() {
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    private fun addQuestion(questionText: String){
        if(sessionId != null) {
            db.collection("Session").document(sessionId!!)
                .update("questionList", FieldValue.arrayUnion(hashMapOf(
                    "text" to questionText
                ))).addOnSuccessListener {
                    Toast.makeText(activity,"Question submitted",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(activity,"Could not submit",Toast.LENGTH_SHORT).show()
                }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_question.
         */
        // TODO: Rename and change types and number of parameters
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