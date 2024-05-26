package at.fhooe.sail.project.semesterproject1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_survey.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_survey : Fragment() {
    // TODO: Rename and change types of parameters
    val db: FirebaseFirestore = Firebase.firestore
    private var sessionId: String? = null
    private var userId: String? = null

    private var survey: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionId = it.getString("sessionId")
            userId = it.getString("userId")
        }

        if(sessionId != null){
            db.collection("Session").document(sessionId!!)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("Firestore", "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        survey = snapshot.data?.get("survey")
                        Log.d("Firestore", survey.toString())
                        
                        if(survey == null){
                            updateUserSurveyOption(-1)
                        }
                    } else {
                        Log.d("Firestore", "Document has been deleted!")
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey, container, false)
    }

    private fun updateUserSurveyOption(optionIndex: Int?){
        if(sessionId != null && userId != null){
            db.collection("Session").document(sessionId!!).collection("User").document(userId!!)
                .update("surveyOption",optionIndex)
                .addOnSuccessListener {

                }.addOnFailureListener{

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
         * @return A new instance of fragment fragment_survey.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_survey().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}