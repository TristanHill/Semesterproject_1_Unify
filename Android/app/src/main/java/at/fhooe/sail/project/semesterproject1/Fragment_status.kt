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
import android.widget.Button
import android.widget.Toast
import at.fhooe.sail.project.semesterproject1.databinding.FragmentStatusBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_status.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_status : Fragment() {

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
        val binding = FragmentStatusBinding.inflate(inflater, container, false)

        //TODO Send String to firebase when button is clicked
        binding.fragmentStatusButtonWorking.setOnClickListener {
            Toast.makeText(context, "Status Changed To Working", Toast.LENGTH_SHORT).show()
        }

        binding.fragmentStatusButtonHelp.setOnClickListener {
            Toast.makeText(context, "Status Changed To Help", Toast.LENGTH_SHORT).show()
        }

        binding.fragmentStatusButtonDone.setOnClickListener {
            Toast.makeText(context, "Status Changed To Done", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUserStatus(status: String){
        if(sessionId != null && userId != null){
            db.collection("Session").document(sessionId!!).collection("User").document(userId!!)
                .update("status",status)
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
         * @return A new instance of fragment fragment_status.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_status().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
