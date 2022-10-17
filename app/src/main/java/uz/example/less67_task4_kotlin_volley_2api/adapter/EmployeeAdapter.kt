package uz.example.less67_task4_kotlin_volley_2api.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import uz.example.less67_task4_kotlin_volley_2api.MainActivity
import uz.example.less67_task4_kotlin_volley_2api.R
import uz.example.less67_task4_kotlin_volley_2api.activity.EditActivity
import uz.example.less67_task4_kotlin_volley_2api.model.Employee

class EmployeeAdapter(val activity: MainActivity,var items:ArrayList<Employee>):RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poster_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val sl_swipe = holder.sl_swipe

        sl_swipe.showMode = SwipeLayout.ShowMode.PullOut
        sl_swipe.addDrag(SwipeLayout.DragEdge.Left, holder.linear_left)
        sl_swipe.addDrag(SwipeLayout.DragEdge.Right, holder.linear_right)

        holder.name.text = "Employee Name: " + item.employee_name.toUpperCase()
        holder.salary.text = "Employee Salary: : " + item.employee_salary
        holder.age.text = "Employee Age: " + item.employee_age
        val id: Int = item.id

        holder.tv_delete.setOnClickListener { activity.dialogEmployee(item) }
        holder.tv_edit.setOnClickListener {
            val intent = Intent(activity.baseContext, EditActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", item.employee_name)
            intent.putExtra("salary", item.employee_salary)
            intent.putExtra("age", item.employee_age)
            //activity.setResult(88,intent);
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        var name: TextView
        var salary: TextView
        var age: TextView
        var linear_left: LinearLayout
        var linear_right: LinearLayout
        var sl_swipe: SwipeLayout
        var tv_delete: TextView
        var tv_edit: TextView
        init {
            name = itemView.findViewById(R.id.tv_name)
            salary = itemView.findViewById(R.id.tv_salary)
            age = itemView.findViewById(R.id.tv_age)
            linear_right = itemView.findViewById(R.id.ll_linear_right)
            linear_left = itemView.findViewById(R.id.ll_linear_left)
            sl_swipe = itemView.findViewById(R.id.sl_swipe)
            tv_delete = itemView.findViewById(R.id.tv_delete)
            tv_edit = itemView.findViewById(R.id.tv_edit)
        }
    }
}