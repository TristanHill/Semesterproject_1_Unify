package at.fhooe.sail.android.lists.a

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.project.semesterproject1.Option
import at.fhooe.sail.project.semesterproject1.R

class OptionsAdapter(val mData: MutableList<Option>) : RecyclerView.Adapter<OptionHolder>() {
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
