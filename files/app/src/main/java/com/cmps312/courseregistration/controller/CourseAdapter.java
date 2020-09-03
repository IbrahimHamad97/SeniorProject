package com.cmps312.courseregistration.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.SchedGroup;
import com.cmps312.courseregistration.model.Student;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private ArrayList<CourseSchedule> courses;
    private CourseInteractions mListener;
    private Boolean isCourseList;
    private Student currStudent;
    private Button confirmRegBtn;
    private ArrayList<Integer> choice;


    public CourseAdapter(Context context, ArrayList<CourseSchedule> courses, CourseInteractions listener, Student currStudent, Button confirmRegBtn,ArrayList<Integer> choice) {
        this.context = context;
        this.courses = courses;
        mListener = listener;
        this.currStudent = currStudent;
        this.confirmRegBtn = confirmRegBtn;
        this.isCourseList = false;
        this.choice = choice;
    }

    public CourseAdapter(Context context, ArrayList<CourseSchedule> courses, CourseInteractions listener,
                         Boolean isCourseList) {
        this.context = context;
        this.courses = courses;
        mListener = listener;
        this.isCourseList = isCourseList;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_constraint_new,
                parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {

        holder.tlCourseItemActions.setVisibility(View.GONE);
        holder.tvCode.setText(context.getString(R.string.code_ph, ""+courses.get(position).getCourseObj().getCode()));
        holder.tvSect.setText(context.getString(R.string.sect_ph, courses.get(position).getSection()));
        holder.tvTitle.setText(context.getString(R.string.course_ph, courses.get(position).getCourseObj().getCourseName()));
        holder.tvInstructor.setText(context.getString(R.string.instructor_ph, courses.get(position).getInstructor().get(0)));
        holder.tvCrn.setText(context.getString(R.string.crn_ph, courses.get(position).getCrn()));
        holder.tvCredit.setText(context.getString(R.string.credit_hours_ph, courses.get(position).getCourseObj().getCreditHours()));
        holder.tvGender.setText(context.getString(R.string.gender_ph, courses.get(position).getGender()));
        holder.tvActiveSeats.setText(context.getString(R.string.active_ph, courses.get(position).getActual()));
        holder.tvCap.setText(context.getString(R.string.cap_ph, courses.get(position).getCapacity()));
        holder.llDates.removeAllViews();
        for(SchedGroup courseDates: courses.get(position).getSchedGroups()){
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(context);
            tv.setLayoutParams(lparams);
            tv.setText(courseDates.toString());
            holder.llDates.addView(tv);
        }



        String conflictCrn = currStudent.checkTimeConflict(courses.get(position));
        if(!currStudent.checkPrequisite(courses.get(position)) || currStudent.checkRegistered(courses.get(position)) || !conflictCrn.equals("-1") || courses.get(position).isFull() || currStudent.checkWaitingList(courses.get(position))){
            if(currStudent.checkRegistered(courses.get(position))){
                holder.tlCourseItem.setBackgroundResource(R.drawable.green_bg);
                holder.tvError.setText("Registered");
                holder.trDropCourse.setVisibility(View.VISIBLE);
                holder.trOverrideCourse.setVisibility(View.GONE);
                holder.trSelectCourse.setVisibility(View.GONE);
                holder.trUnselectCourse.setVisibility(View.GONE);
                holder.trWaitingList.setVisibility(View.GONE);
            }
            else if(currStudent.checkWaitingList(courses.get(position))){
                holder.tlCourseItem.setBackgroundResource(R.drawable.yellow_bg);
                holder.tvError.setText("On Waiting List");
                holder.trDropCourse.setVisibility(View.GONE);
                holder.trOverrideCourse.setVisibility(View.GONE);
                holder.trSelectCourse.setVisibility(View.GONE);
                holder.trUnselectCourse.setVisibility(View.GONE);
                holder.trWaitingList.setVisibility(View.GONE);
            }
            else if(courses.get(position).isFull()){
                holder.tlCourseItem.setBackgroundResource(R.drawable.red_bg);
                holder.tvError.setText("Course is Full");
                holder.trSelectCourse.setVisibility(View.GONE);
                holder.trOverrideCourse.setVisibility(View.VISIBLE);
                holder.trDropCourse.setVisibility(View.GONE);
                holder.trUnselectCourse.setVisibility(View.GONE);
                holder.trWaitingList.setVisibility(View.VISIBLE);
            }
            else if(!currStudent.checkPrequisite(courses.get(position))) {
                Log.d("test15", "onBindViewHolder: " + courses.get(position).getCrn());
                holder.tvError.setText("Issue: Missing Pre-Requisites");
                holder.tlCourseItem.setBackgroundResource(R.drawable.red_bg);
                holder.trSelectCourse.setVisibility(View.GONE);
                holder.trOverrideCourse.setVisibility(View.VISIBLE);
                holder.trDropCourse.setVisibility(View.GONE);
                holder.trUnselectCourse.setVisibility(View.GONE);
                holder.trWaitingList.setVisibility(View.GONE);
            }

            else if(!conflictCrn.equals("-1")){
                holder.tlCourseItem.setBackgroundResource(R.drawable.red_bg);
                holder.tvError.setText("Issue: Time conflict with "+conflictCrn);
                holder.trSelectCourse.setVisibility(View.GONE);
                holder.trOverrideCourse.setVisibility(View.GONE);
                holder.trDropCourse.setVisibility(View.GONE);
                holder.trUnselectCourse.setVisibility(View.GONE);
                holder.trWaitingList.setVisibility(View.GONE);
            }

        }
        else if(choice.contains(Integer.valueOf(position))){
            holder.tlCourseItem.setBackgroundResource(R.drawable.white_bg_selection);

            holder.tvError.setText("Selected");
            holder.trDropCourse.setVisibility(View.GONE);
            holder.trOverrideCourse.setVisibility(View.GONE);
            holder.trSelectCourse.setVisibility(View.GONE);
            holder.trUnselectCourse.setVisibility(View.VISIBLE);
            holder.trWaitingList.setVisibility(View.GONE);
        }
        else {
            holder.tvError.setText("");
            holder.tlCourseItem.setBackgroundResource(R.drawable.white_bg);
            holder.trDropCourse.setVisibility(View.GONE);
            holder.trOverrideCourse.setVisibility(View.GONE);
            holder.trSelectCourse.setVisibility(View.VISIBLE);
            holder.trUnselectCourse.setVisibility(View.GONE);
            holder.trWaitingList.setVisibility(View.GONE);
        }





        confirmRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CourseSchedule> selections = new ArrayList<>();
                if(!choice.isEmpty()){
                    for(int i=0;i<choice.size();i++){
                        CourseSchedule tempcs = courses.get(i);
                        if(checkLabRequirementsMet(tempcs)) {
                            selections.add(courses.get(i));
                        }
                        else{
                            Toast.makeText(context, "You must select both a lab and lecture together", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    mListener.onAddCourse(selections);
                    choice.clear();
                    notifyDataSetChanged();

                }else{
                    Toast.makeText(context, "No courses selected. Please select at least 1 course.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        if(courses!=null)
            return courses.size();
        else
            return 1;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCode, tvSect, tvTitle, tvInstructor, tvSched, tvCredit, tvCrn,
                tvActiveSeats, tvCap, tvGender, tvCourseAction, tvError;
        private ConstraintLayout tlCourseItem;
        private LinearLayout llDates;
        private TableLayout tlCourseItemActions;
        private TableRow trShowCourseInfo, trSelectCourse, trDropCourse, trOverrideCourse, trUnselectCourse, trWaitingList;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tlCourseItem = itemView.findViewById(R.id.tl_course_item);
            tlCourseItemActions = itemView.findViewById(R.id.tl_course_item_actions);
            trShowCourseInfo = itemView.findViewById(R.id.tr_show_course);
            trDropCourse = itemView.findViewById(R.id.tr_drop_course);
            trOverrideCourse = itemView.findViewById(R.id.tr_override_course);

            trUnselectCourse = itemView.findViewById(R.id.tr_unselect_course);

            trWaitingList = itemView.findViewById(R.id.tr_waitinglist_course);
            tvError = itemView.findViewById(R.id.tv_issues);
            trSelectCourse = itemView.findViewById(R.id.tr_select_course);
            trDropCourse.setVisibility(View.INVISIBLE);
            trOverrideCourse.setVisibility(View.INVISIBLE);
            trUnselectCourse.setVisibility(View.INVISIBLE);
            trSelectCourse.setVisibility(View.INVISIBLE);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvActiveSeats = itemView.findViewById(R.id.tv_active);
            tvCap = itemView.findViewById(R.id.tv_rem);
            tvCode = itemView.findViewById(R.id.tv_crs_code);
            tvSect = itemView.findViewById(R.id.tv_crs_section);
            tvTitle = itemView.findViewById(R.id.tv_crs_title);
            tvInstructor = itemView.findViewById(R.id.tv_crs_instructor);
            tvSched = itemView.findViewById(R.id.tv_crs_datetime);
            tvCredit = itemView.findViewById(R.id.tv_credit_hours);
            tvCrn = itemView.findViewById(R.id.tv_item_crn);
            llDates = itemView.findViewById(R.id.ll_dates);

            final Interactions.ButtonEffectListener bfl = new Interactions.ButtonEffectListener();

            tlCourseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickToggleActions(v, tlCourseItemActions);
                }
            });


            trShowCourseInfo.setOnTouchListener(bfl);
            trShowCourseInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    mListener.onClickInfo(trShowCourseInfo,tempSched);
                }
            });

            trSelectCourse.setOnTouchListener(bfl);
            trSelectCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice.add(getAdapterPosition());
                    notifyDataSetChanged();

                }
            });

            trDropCourse.setOnTouchListener(bfl);
            trDropCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courses.get(getAdapterPosition()).setActual(courses.get(getAdapterPosition()).getActual()-1);
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    mListener.onDropCourse(tempSched);
                }
            });

            trUnselectCourse.setOnTouchListener(bfl);
            trUnselectCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choice.remove(Integer.valueOf(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });

            trWaitingList.setOnTouchListener(bfl);
            trWaitingList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    mListener.onWaitingListCourse(tempSched);
                }
            });

            trOverrideCourse.setOnTouchListener(bfl);
            trOverrideCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String overrideReason="";
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    if(!currStudent.checkPrequisite(tempSched))
                        overrideReason="Pre-requisite Override";
                    else if(courses.get(getAdapterPosition()).isFull())
                        overrideReason="Course Capacity Override";
                    mListener.onOverrideCourse(tempSched,overrideReason);


                }
            });


            if (isCourseList)
                tvCourseAction.setText("Register course");

        }


    }

    //Item actions
    public interface CourseInteractions {

        void onClickToggleActions(View view, View actions);

        void onClickInfo(View view, CourseSchedule course);

        void onAddCourse(ArrayList<CourseSchedule> course);

        void onDropCourse(CourseSchedule course);

        void onWaitingListCourse(CourseSchedule course);

        void onOverrideCourse(CourseSchedule course, String reason);
    }

    public boolean checkLabRequirementsMet(CourseSchedule courseSchedule){

        Boolean labRequirement = courseSchedule.getCourseObj().getLab();
        if(labRequirement) {
            Character coursetype = courseSchedule.getSection().charAt(0);
            for (int i = 0; i < choice.size(); i++) {
                Character coursetype2 = courses.get(i).getSection().charAt(0);
                if (!coursetype.equals(coursetype2))
                    return true;
            }
            return false;
        }
        return true;


    }

}
