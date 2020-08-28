package com.myth.mama

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.func_item.view.*

class MainAdapter(private val funcList: List<FuncItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.func_item, parent, false)
        ) {}
    }

    override fun getItemCount(): Int {
        return funcList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val funcItem = funcList[position]
        holder.itemView.btn_func.text = funcItem.name
        holder.itemView.btn_func.setOnClickListener {
            funcItem.func()
        }
    }
}