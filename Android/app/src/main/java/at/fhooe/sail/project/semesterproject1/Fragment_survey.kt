package at.fhooe.sail.project.semesterproject1

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.android.lists.a.OptionsAdapter
import at.fhooe.sail.project.semesterproject1.databinding.FragmentSurveyBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_survey.newInstance] factory method to
 * create an instance of this fragment.
 */

class Fragment_survey : Fragment(), OnItemClickListener {

    private lateinit var adapter: OptionsAdapter
    private lateinit var recyclerView: RecyclerView
    // List to hold survey options
    private var optionList: MutableList<Option> = mutableListOf<Option>()

    private var _binding: FragmentSurveyBinding? = null
    private val binding: FragmentSurveyBinding
        get() = _binding!!

    private val db: FirebaseFirestore = Firebase.firestore

    // Session ID and User ID passed as arguments
    private var sessionId: String? = null
    private var userId: String? = null

    // Survey data object
    private var survey: Survey? = null

    /**
     * Called when the fragment is created.
     * Retrieves session and user IDs from the arguments and sets up a listener for Firestore changes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionId = it.getString("sessionId")
            userId = it.getString("userId")
        }

        // Listen for changes in the session document
        if (sessionId != null) {
            db.collection("Session").document(sessionId!!)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("Firestore", "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    // Update the survey data if the document exists
                    if (snapshot != null && snapshot.exists()) {
                        val surveyHashMap = snapshot.data?.get("survey") as HashMap<String, Any>?
                        if (surveyHashMap != null) {
                            survey = Survey(
                                surveyHashMap["surveyTitle"].toString(),
                                surveyHashMap["surveyOptions"] as ArrayList<String>
                            )
                            dataInitialize()
                            Log.d(TAG, survey.toString())
                        }
                    } else {
                        Log.d("Firestore", "Document has been deleted!")
                    }
                }
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        _binding = FragmentSurveyBinding.inflate(inflater)
        return binding.root
    }

    /**
     * Handles item click events from the RecyclerView.
     */
    override fun onItemClick(position: Int) {
        // Update the user's selected survey option
        updateUserSurveyOption(position)
        Toast.makeText(context, "Clicked item position: $position", Toast.LENGTH_SHORT).show()
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes data and sets up the RecyclerView.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        // Set up the RecyclerView with a layout manager and adapter
        recyclerView = view.findViewById(R.id.fragment_survey_recycler_view)
        adapter = OptionsAdapter(optionList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = 10
                outRect.right = 10
                outRect.bottom = 10
            }
        })
    }

    /**
     * Updates the user's selected survey option in Firestore.
     */
    public fun updateUserSurveyOption(optionIndex: Int?) {
        if (sessionId != null && userId != null) {
            db.collection("Session").document(sessionId!!).collection("User").document(userId!!)
                .update("surveyOption", optionIndex)
                .addOnSuccessListener {
                    // Successfully updated the survey option
                }.addOnFailureListener {
                    // Failed to update the survey option
                }
        }
    }

    /**
     * Initializes the data for the survey options and sets up the RecyclerView adapter.
     */
    private fun dataInitialize() {
        if (survey != null) {
            binding.fragmentSurveyTv.text = survey?.surveyTitle
            optionList = mutableListOf<Option>()

            // Populate the option list with survey options
            for (i in 0 until survey?.surveyOptions!!.size) {
                optionList.add(Option(survey?.surveyOptions!![i]))
            }

            val layoutManager = LinearLayoutManager(context)
            recyclerView = binding.fragmentSurveyRecyclerView
            recyclerView.adapter = OptionsAdapter(optionList, this)
            recyclerView.layoutManager = GridLayoutManager(context, 1)
            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.left = 10
                    outRect.right = 10
                    outRect.bottom = 10
                }
            })
        }
    }

    companion object {
        /**
         * Factory method to create a new instance of this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_survey.
         */
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
