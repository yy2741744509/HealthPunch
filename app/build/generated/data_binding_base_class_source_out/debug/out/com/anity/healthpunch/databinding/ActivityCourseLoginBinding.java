// Generated by view binder compiler. Do not edit!
package com.anity.healthpunch.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.anity.healthpunch.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCourseLoginBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnCourseLogin;

  @NonNull
  public final CheckBox cbCourseRemember;

  @NonNull
  public final EditText etCoursePassword;

  @NonNull
  public final EditText etCourseUsername;

  private ActivityCourseLoginBinding(@NonNull LinearLayout rootView, @NonNull Button btnCourseLogin,
      @NonNull CheckBox cbCourseRemember, @NonNull EditText etCoursePassword,
      @NonNull EditText etCourseUsername) {
    this.rootView = rootView;
    this.btnCourseLogin = btnCourseLogin;
    this.cbCourseRemember = cbCourseRemember;
    this.etCoursePassword = etCoursePassword;
    this.etCourseUsername = etCourseUsername;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCourseLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCourseLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_course_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCourseLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_courseLogin;
      Button btnCourseLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnCourseLogin == null) {
        break missingId;
      }

      id = R.id.cb_courseRemember;
      CheckBox cbCourseRemember = ViewBindings.findChildViewById(rootView, id);
      if (cbCourseRemember == null) {
        break missingId;
      }

      id = R.id.et_coursePassword;
      EditText etCoursePassword = ViewBindings.findChildViewById(rootView, id);
      if (etCoursePassword == null) {
        break missingId;
      }

      id = R.id.et_courseUsername;
      EditText etCourseUsername = ViewBindings.findChildViewById(rootView, id);
      if (etCourseUsername == null) {
        break missingId;
      }

      return new ActivityCourseLoginBinding((LinearLayout) rootView, btnCourseLogin,
          cbCourseRemember, etCoursePassword, etCourseUsername);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
