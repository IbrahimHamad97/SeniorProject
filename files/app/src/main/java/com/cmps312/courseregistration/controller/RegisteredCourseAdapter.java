package com.cmps312.courseregistration.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.SchedGroup;

import java.util.ArrayList;


/**
 * A lighter version of CourseAdapter for use with Course Management and Waiting List
 */
public class RegisteredCourseAdapter extends RecyclerView.Adapter<RegisteredCourseAdapter.CourseViewHolder> {
    private Context context;
    private ArrayList<CourseSchedule> courses;
    private RegisteredCourseAdapter.RegCourseInteractions mListener;
    private boolean isWaitingList;

    public RegisteredCourseAdapter(Context context, ArrayList<CourseSchedule> courses,
                                   RegisteredCourseAdapter.RegCourseInteractions listener,
                                   boolean isWaitingList) {
        this.context = context;
        this.courses = courses;
        mListener = listener;
        this.isWaitingList = isWaitingList;
    }

    @NonNull
    @Override
    public RegisteredCourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_constraint_new,
                parent, false);
        return new RegisteredCourseAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisteredCourseAdapter.CourseViewHolder holder, int position) {

        holder.tlCourseItemActions.setVisibility(View.GONE);
        holder.tvCode.setText(context.getString(R.string.code_ph, "" + courses.get(position).getCourseObj().getCode()));
        holder.tvSect.setText(context.getString(R.string.sect_ph, courses.get(position).getSection()));
        holder.tvTitle.setText(context.getString(R.string.course_ph, courses.get(position).getCourseObj().getCourseName()));
        holder.tvInstructor.setText(context.getString(R.string.instructor_ph, courses.get(position).getInstructor().get(0)));
        holder.tvCrn.setText(context.getString(R.string.crn_ph, courses.get(position).getCrn()));
        holder.tvCredit.setText(context.getString(R.string.credit_hours_ph, courses.get(position).getCourseObj().getCreditHours()));
        holder.tvGender.setText(context.getString(R.string.gender_ph, courses.get(position).getGender()));
        holder.tvActiveSeats.setText(context.getString(R.string.active_ph, courses.get(position).getActual()));
        holder.tvRemaining.setText(context.getString(R.string.cap_ph, courses.get(position).getCapacity()));
        holder.llDates.removeAllViews();
        for (SchedGroup courseDates : courses.get(position).getSchedGroups()) {
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(context);
            tv.setLayoutParams(lparams);
            tv.setText(courseDates.toString());
            holder.llDates.addView(tv);
        }


    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCode, tvSect, tvTitle, tvInstructor, tvSched, tvCredit, tvCrn,
                tvActiveSeats, tvRemaining, tvGender;
        private ConstraintLayout tlCourseItem;
        private LinearLayout llDates;
        private TableLayout tlCourseItemActions;
        private TableRow trShowCourseInfo, trRegCourse, trDropCourse, trUnselect, trOverride, trWaitingList;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            //Associate non-used elements and set to GONE
            trUnselect = itemView.findViewById(R.id.tr_unselect_course);
            trOverride = itemView.findViewById(R.id.tr_override_course);
            trWaitingList = itemView.findViewById(R.id.tr_waitinglist_course);
            trUnselect.setVisibility(View.GONE);
            trOverride.setVisibility(View.GONE);
            trWaitingList.setVisibility(View.GONE);

            tlCourseItem = itemView.findViewById(R.id.tl_course_item);
            tlCourseItemActions = itemView.findViewById(R.id.tl_course_item_actions);
            trShowCourseInfo = itemView.findViewById(R.id.tr_show_course);
            trDropCourse = itemView.findViewById(R.id.tr_drop_course);
            trRegCourse = itemView.findViewById(R.id.tr_select_course);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvActiveSeats = itemView.findViewById(R.id.tv_active);
            tvRemaining = itemView.findViewById(R.id.tv_rem);
            tvCode = itemView.findViewById(R.id.tv_crs_code);
            tvSect = itemView.findViewById(R.id.tv_crs_section);
            tvTitle = itemView.findViewById(R.id.tv_crs_title);
            tvInstructor = itemView.findViewById(R.id.tv_crs_instructor);
            tvSched = itemView.findViewById(R.id.tv_crs_datetime);
            tvCredit = itemView.findViewById(R.id.tv_credit_hours);
            tvCrn = itemView.findViewById(R.id.tv_item_crn);
            llDates = itemView.findViewById(R.id.ll_dates);


            //buttonEffectListener
            final Interactions.ButtonEffectListener bfl = new Interactions.ButtonEffectListener();

            if (isWaitingList) {
                TextView trRegCourseLbl = itemView.findViewById(R.id.tv_lbl_select_course);
                trRegCourseLbl.setText(context.getString(R.string.reg_course));
                trRegCourse.setVisibility(View.VISIBLE);
            } else {
                trRegCourse.setVisibility(View.GONE);
            }

            tlCourseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickToggleActions(v, tlCourseItemActions);
                }
            });

            trRegCourse.setOnTouchListener(bfl);
            trRegCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    mListener.onAddCourse(tempSched);
                }
            });

            trShowCourseInfo.setOnTouchListener(bfl);
            trShowCourseInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    mListener.onClickInfo(trShowCourseInfo, tempSched);
                }
            });

            trDropCourse.setOnTouchListener(bfl);
            trDropCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courses.get(getAdapterPosition()).setActual(courses.get(getAdapterPosition()).getActual() - 1);
                    CourseSchedule tempSched = courses.get(getAdapterPosition());
                    mListener.onDropCourse(tempSched);
                }
            });

        }
    }

    //Button actions
    public interface RegCourseInteractions {

        void onClickToggleActions(View view, View actions);

        void onClickInfo(View view, CourseSchedule course);

        void onAddCourse(CourseSchedule course);

        void onDropCourse(CourseSchedule course);
    }
}
