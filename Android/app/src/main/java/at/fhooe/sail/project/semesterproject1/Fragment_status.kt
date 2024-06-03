package at.fhooe.sail.project.semesterproject1

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var db: FirebaseFirestore
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

        db = FirebaseFirestore.getInstance()

        val fragmentStatusButtonWorking: AppCompatButton = binding.fragmentStatusButtonWorking
        val fragmentStatusButtonHelp: AppCompatButton = binding.fragmentStatusButtonHelp
        val fragmentStatusButtonDone: AppCompatButton = binding.fragmentStatusButtonDone

        // Drawable resource IDs for the buttons' pressed state
        val drawableWorkingPressed = R.drawable.fragment_status_button_pressed_working
        val drawableHelpPressed = R.drawable.fragment_status_button_pressed_help
        val drawableDonePressed = R.drawable.fragment_status_button_pressed_done

        changeButtonColor(this@Fragment_status, fragmentStatusButtonWorking, drawableWorkingPressed)


        fragmentStatusButtonWorking.setOnClickListener {
            changeButtonColor(this@Fragment_status, fragmentStatusButtonWorking, drawableWorkingPressed)
            resetOtherButtons(fragmentStatusButtonWorking, fragmentStatusButtonHelp, fragmentStatusButtonDone)
            updateUserStatus("In Progress")
        }

        fragmentStatusButtonHelp.setOnClickListener {
            changeButtonColor(this@Fragment_status, fragmentStatusButtonHelp, drawableHelpPressed)
            resetOtherButtons(fragmentStatusButtonHelp, fragmentStatusButtonWorking, fragmentStatusButtonDone)
            updateUserStatus("Need Help")
        }

        fragmentStatusButtonDone.setOnClickListener {
            changeButtonColor(this@Fragment_status, fragmentStatusButtonDone, drawableDonePressed)
            resetOtherButtons(fragmentStatusButtonDone, fragmentStatusButtonWorking, fragmentStatusButtonHelp)
            updateUserStatus("Done")
        }

        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun resetOtherButtons(selectedButton: AppCompatButton, vararg otherButtons: AppCompatButton) {
        for (button in otherButtons) {
            val drawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.fragment_status_button_normal)
            button.background = drawable
        }
    }

    private fun updateUserStatus(status: String){
        if(sessionId != null && userId != null){
            db.collection("Session").document(sessionId!!).collection("User").document(userId!!)
                .update("status",status)
                .addOnSuccessListener {
                    // Handle success
                }.addOnFailureListener{
                    // Handle failure
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_status().apply {
                arguments = Bundle().apply {
                    putString("sessionId", param1)
                    putString("userId", param2)
                }
            }

        @JvmStatic
        fun changeButtonColor(fragmentStatus: Fragment_status, button: AppCompatButton, drawableId: Int) {
            val drawable: Drawable? = ContextCompat.getDrawable(fragmentStatus.requireContext(), drawableId)
            button.background = drawable
        }
    }
}
