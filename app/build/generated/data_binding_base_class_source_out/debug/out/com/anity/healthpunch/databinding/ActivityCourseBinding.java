// Generated by view binder compiler. Do not edit!
package com.anity.healthpunch.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import com.anity.healthpunch.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCourseBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final PagerTabStrip pagerTabStrip;

  @NonNull
  public final Spinner selectSpinner;

  @NonNull
  public final ViewPager viewPager;

  private ActivityCourseBinding(@NonNull LinearLayout rootView,
      @NonNull PagerTabStrip pagerTabStrip, @NonNull Spinner selectSpinner,
      @NonNull ViewPager viewPager) {
    this.rootView = rootView;
    this.pagerTabStrip = pagerTabStrip;
    this.selectSpinner = selectSpinner;
    this.viewPager = viewPager;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCourseBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCourseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_course, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCourseBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.pagerTabStrip;
      PagerTabStrip pagerTabStrip = ViewBindings.findChildViewById(rootView, id);
      if (pagerTabStrip == null) {
        break missingId;
      }

      id = R.id.select_spinner;
      Spinner selectSpinner = ViewBindings.findChildViewById(rootView, id);
      if (selectSpinner == null) {
        break missingId;
      }

      id = R.id.viewPager;
      ViewPager viewPager = ViewBindings.findChildViewById(rootView, id);
      if (viewPager == null) {
        break missingId;
      }

      return new ActivityCourseBinding((LinearLayout) rootView, pagerTabStrip, selectSpinner,
          viewPager);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
