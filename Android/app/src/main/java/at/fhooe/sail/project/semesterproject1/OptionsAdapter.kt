package at.fhooe.sail.android.lists.a

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.project.semesterproject1.Option
import at.fhooe.sail.project.semesterproject1.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class OptionsAdapter(val mData: List<Option>,val listener: OnItemClickListener) : RecyclerView.Adapter<OptionsAdapter.OptionHolder>(){

    inner class OptionHolder(val root: View): RecyclerView.ViewHolder(root){
        var mContent: TextView
        val db: FirebaseFirestore = Firebase.firestore
        private lateinit var sharedPref: SharedPreferences
        private lateinit var sessionID: String
        private lateinit var userID: String
        init {
            mContent = root.findViewById(R.id.fragment_survey_option_tv) as TextView

            root.setOnClickListener {
                listener.setOnItemClick(adapterPosition)
            }

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.fragment_survey_option, null)
        return OptionHolder(view)
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.mContent.text = mData[position].content
    }

    override fun getItemCount(): Int = mData.size
}
