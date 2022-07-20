package com.projectcgpa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.projectcgpa.DialogActivity;
import com.projectcgpa.R;
import com.projectcgpa.entities.Course;

import java.text.DecimalFormat;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private DecimalFormat precision = new DecimalFormat("0.00");
    private List<Course> courseList;
    private LayoutInflater inflater;
    private DialogActivity dialogActivity;
    Context context;

    public CourseAdapter(Context context, List<Course> course_List) {
        this.inflater = LayoutInflater.from(context);
        this.courseList = course_List;
        this.dialogActivity = new DialogActivity(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_course, parent, false);
        return new CourseAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Course course = courseList.get(position);
        String strCourseCode = "Course Code : " + course.getCourse_name();
        holder.mCourse.setText(strCourseCode);
        String str = "Credit Hours : " + String.valueOf(course.getCourse_credit());
        holder.mCredit.setText(str);
        String grade = "Score : " + String.valueOf(precision.format(course.getObtain_gpa()));
        holder.mGpa.setText(grade);
        holder.mGrade.setText(course.getGrade());
    }

    @Override
    public int getItemCount() {
        return !courseList.isEmpty() ? courseList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mCourse, mCredit, mGpa, mGrade;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCourse = itemView.findViewById(R.id.courseNameTV);
            mCredit = itemView.findViewById(R.id.creditHoursTV);
            mGpa = itemView.findViewById(R.id.gradeTV);
            mGrade = itemView.findViewById(R.id.courseCGPATV);
            cardView = itemView.findViewById(R.id.single_row_card_layout_course);

        }
        @Override
        public void onClick(View view) {
            dialogActivity.showDialogUpdateDeleteForCourse(courseList, getAdapterPosition());
        }
    }
}
