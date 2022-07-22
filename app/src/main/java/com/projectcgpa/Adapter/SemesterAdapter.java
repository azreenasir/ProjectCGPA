package com.projectcgpa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.projectcgpa.CourseListActivity;
import com.projectcgpa.DialogActivity;
import com.projectcgpa.MainActivity;
import com.projectcgpa.R;
import com.projectcgpa.RegisterSemester;
import com.projectcgpa.entities.Semester;
import com.projectcgpa.entities.User;

import java.text.DecimalFormat;
import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.MyViewHolder> {
    private final Intent intent;
    private DecimalFormat precision = new DecimalFormat("0.00");
    private List<Semester> semesterList;
    private LayoutInflater inflater;
    private Context context;
    private DialogActivity dialogActivity;

    public SemesterAdapter(Context context, List<Semester> semesterList, Intent intent) {
        this.semesterList = semesterList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.dialogActivity = new DialogActivity(context);
        this.intent = intent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_semester, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mSemesterName.setText(semesterList.get(holder.getAdapterPosition()).getSemester_name());
        holder.mSemesterCgpa.setText(String.valueOf(precision.format(semesterList.get(holder.getAdapterPosition()).getTotal_gpa())));
        String credit = "Total Credit: " + String.valueOf(semesterList.get(holder.getAdapterPosition()).getTotal_credit());
        holder.mSemesterCredit.setText(credit);
        String gpa = "Total Course: " + String.valueOf(semesterList.get(holder.getAdapterPosition()).getTotal_course());
        holder.mSemesterCourse.setText(gpa);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Semester semester = semesterList.get(holder.getAdapterPosition());
                long mId = semester.getmId();
                Intent intent1 = ((Activity) context).getIntent();
                User user = (User) intent1.getSerializableExtra("user");
                Intent intent = new Intent(context, CourseListActivity.class);
                intent.putExtra("mId", mId);
                intent.putExtra("user",user);
                context.startActivity(intent);
            }
        });
        holder.btn_delete_semester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btn_delete_semester);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.rename) {
                            dialogActivity.showDialogUpdateForSemester(semesterList, holder.getAdapterPosition());
                            Toast.makeText(context,"Rename", Toast.LENGTH_SHORT).show();
                        }
                        else if (item.getItemId() == R.id.delete) {
                            Semester semester = semesterList.get(holder.getAdapterPosition());
                            dialogActivity.showDialogDeleteSemester(semester, holder.getAdapterPosition());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Semester semester = semesterList.get(holder.getAdapterPosition());
                dialogActivity.showDialogDeleteSemester(semester, holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return !semesterList.isEmpty() ? semesterList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mSemesterName, mSemesterCgpa, mSemesterCredit, mSemesterCourse;
        CardView cardView;
        ImageButton btn_delete_semester;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mSemesterName = itemView.findViewById(R.id.SemNameTV);
            mSemesterCgpa = itemView.findViewById(R.id.semCgpaTV);
            mSemesterCredit = itemView.findViewById(R.id.TotCreditTV);
            mSemesterCourse = itemView.findViewById(R.id.totCourseTV);
            cardView = itemView.findViewById(R.id.single_row_card_layout_semester);
            btn_delete_semester = itemView.findViewById(R.id.btn_delete_semester);
        }
    }

}
