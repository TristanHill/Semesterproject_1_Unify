package at.fhooe.sail.project.semesterproject1

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.project.semesterproject1.databinding.FragmentSurveyBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentSurvey : Fragment(), OnItemClickListener {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: OptionsAdapter
    private lateinit var recyclerView: RecyclerView

    private var optionList: MutableList<Option> = mutableListOf()
    private var _binding: FragmentSurveyBinding? = null
    private val binding get() = _binding!!

    private val db: FirebaseFirestore = Firebase.firestore
    private var sessionId: String? = null
    private var userId: String? = null
    private var survey: Survey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionId = it.getString("sessionId")
            userId = it.getString("userId")
        }

        if (sessionId != null) {
            db.collection("Session").document(sessionId!!)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("Firestore", "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val surveyMap = snapshot.data?.get("survey") as? HashMap<String, Any> // ✅ Fix 8
                        if (surveyMap != null) {
                            survey = Survey(
                                surveyMap["surveyTitle"].toString(),
                                ArrayList(surveyMap["surveyOptions"] as List<String>)
                            )
                            if (isAdded && _binding != null) {
                                dataInitialize()
                            }
                        }
                    } else {
                        Log.d("Firestore", "Document has been deleted!")
                    }
                }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        // ✅ Fix 7: Remove delayed callbacks to avoid leaks
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun onItemClick(position: Int) {
        updateUserSurveyOption(position)
        Toast.makeText(context, "${survey!!.surveyOptions[position]}", Toast.LENGTH_SHORT).show()

        handler.postDelayed({
            parentFragmentManager.beginTransaction()
                .replace(R.id.activity_main_frameLayout, Fragment_nosurvey())
                .addToBackStack(null)
                .commit()
        }, 200)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.fragmentSurveyRecyclerView
        adapter = OptionsAdapter(optionList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 1)
    }

    private fun updateUserSurveyOption(optionIndex: Int?) {
        if (sessionId != null && userId != null) {
            db.collection("Session").document(sessionId!!)
                .collection("User").document(userId!!)
                .update("surveyOption", optionIndex)
        }
    }

    private fun dataInitialize() {
        if (survey != null) {
            binding.fragmentSurveyTv.text = survey?.surveyTitle
            optionList = survey!!.surveyOptions.map { Option(it) }.toMutableList()

            adapter = OptionsAdapter(optionList, this)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.left = 10
                    outRect.right = 10
                    outRect.bottom = 10
                }
            })
        }
    }
}
