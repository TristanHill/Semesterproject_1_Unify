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
import android.widget.Toast
import at.fhooe.sail.project.semesterproject1.databinding.FragmentStatusBinding

// Constants for fragment initialization parameters
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
        // Retrieve session and user IDs from arguments
        arguments?.let {
            sessionId = it.getString("sessionId")
            userId = it.getString("userId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatusBinding.inflate(inflater, container, false)

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance()

        val fragmentStatusButtonWorking: AppCompatButton = binding.fragmentStatusButtonWorking
        val fragmentStatusButtonHelp: AppCompatButton = binding.fragmentStatusButtonHelp
        val fragmentStatusButtonDone: AppCompatButton = binding.fragmentStatusButtonDone

        val drawableWorkingPressed = R.drawable.fragment_status_button_pressed_working
        val drawableHelpPressed = R.drawable.fragment_status_button_pressed_help
        val drawableDonePressed = R.drawable.fragment_status_button_pressed_done

        // Initially change the color of the "Working" button to indicate it is selected
        changeButtonColor(this@Fragment_status, fragmentStatusButtonWorking, drawableWorkingPressed)

        // Set click listeners for each button
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

    // Helper method to reset the color of other buttons to their normal state
    private fun resetOtherButtons(selectedButton: AppCompatButton, vararg otherButtons: AppCompatButton) {
        for (button in otherButtons) {
            val drawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.fragment_status_button_normal)
            button.background = drawable
        }
    }

    // Method to update the user's status in the Firestore database
    private fun updateUserStatus(status: String){
        if(sessionId != null && userId != null){
            db.collection("Session").document(sessionId!!).collection("User").document(userId!!)
                .update("status", status)
                .addOnSuccessListener {
                    // Handle success (optional: show a toast or log success)
                }.addOnFailureListener{
                    // Handle failure (optional: show a toast or log error)
                }
        }
    }

    companion object {
        // Factory method to create a new instance of this fragment using the provided parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_status().apply {
                arguments = Bundle().apply {
                    putString("sessionId", param1)
                    putString("userId", param2)
                }
            }

        // Static method to change the button color
        @JvmStatic
        fun changeButtonColor(fragmentStatus: Fragment_status, button: AppCompatButton, drawableId: Int) {
            val drawable: Drawable? = ContextCompat.getDrawable(fragmentStatus.requireContext(), drawableId)
            button.background = drawable
        }
    }
}
