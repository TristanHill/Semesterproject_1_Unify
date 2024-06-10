package at.fhooe.sail.project.semesterproject1

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.android.play.integrity.internal.i
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

class Fragment_survey : Fragment(),     OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var adapter: OptionsAdapter
    private  lateinit var recyclerView: RecyclerView
    private var optionList: MutableList<Option> = mutableListOf<Option>()

    var _binding: FragmentSurveyBinding? = null
    val binding: FragmentSurveyBinding
        get() = _binding!!
    val db: FirebaseFirestore = Firebase.firestore
    private var sessionId: String? = null
    private var userId: String? = null
    private var survey: Survey? = null

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
                        val surveyHashMap = snapshot.data?.get("survey") as HashMap<String, Any>?
                        if(surveyHashMap != null){
                            survey =  Survey(surveyHashMap["surveyTitle"].toString(), surveyHashMap["surveyOptions"] as ArrayList<String>)
                            dataInitialize()
                            Log.d(TAG, survey.toString())
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

        this
        _binding = FragmentSurveyBinding.inflate(inflater)
        return binding.root
    }

    override fun onItemClick(position: Int) {
        // Handle the click event and position here
        updateUserSurveyOption(position)
        Toast.makeText(context, "${survey!!.surveyOptions[position]}", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            // Replace the current fragment with a new one
            val newFragment = Fragment_nosurvey() // Replace with your new fragment class
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Optionally add this transaction to the back stack
            fragmentTransaction.replace(R.id.activity_main_frameLayout, newFragment)
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.commit()
        }, 200)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.fragment_survey_recycler_view)
        val recycler: RecyclerView = binding.fragmentSurveyRecyclerView
        adapter = OptionsAdapter(optionList,  this)
        recycler.adapter = adapter
            recycler.layoutManager = GridLayoutManager(context,1)
        recycler.addItemDecoration(object: RecyclerView.ItemDecoration() {
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

    public fun updateUserSurveyOption(optionIndex: Int?){
        if(sessionId != null && userId != null){
            db.collection("Session").document(sessionId!!).collection("User").document(userId!!)
                .update("surveyOption",optionIndex)
                .addOnSuccessListener {

                }.addOnFailureListener{

                }
        }
    }

    private fun dataInitialize() {
        if(survey != null) {
            binding.fragmentSurveyTv.setText(survey?.surveyTitle)
            optionList = mutableListOf<Option>()

            //needs to be modified to go from 0.. amount of options, Option(surveyString, i)
            for(i in 0 .. survey?.surveyOptions!!.size-1){
                optionList.add(Option(survey?.surveyOptions!![i]))
            }

            Log.d("Kathi", optionList.toString())

            val layoutManager = LinearLayoutManager(context)
            recyclerView = binding.fragmentSurveyRecyclerView
            val recycler: RecyclerView = binding.fragmentSurveyRecyclerView
            recycler.adapter = OptionsAdapter(optionList, this)
            recycler.layoutManager = GridLayoutManager(context,1)
            recycler.addItemDecoration(object: RecyclerView.ItemDecoration() {
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