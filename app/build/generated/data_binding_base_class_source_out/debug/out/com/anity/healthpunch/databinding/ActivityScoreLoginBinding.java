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

public final class ActivityScoreLoginBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnScoreLogin;

  @NonNull
  public final CheckBox cbScoreRemember;

  @NonNull
  public final EditText etScorePassword;

  @NonNull
  public final EditText etScoreUsername;

  private ActivityScoreLoginBinding(@NonNull LinearLayout rootView, @NonNull Button btnScoreLogin,
      @NonNull CheckBox cbScoreRemember, @NonNull EditText etScorePassword,
      @NonNull EditText etScoreUsername) {
    this.rootView = rootView;
    this.btnScoreLogin = btnScoreLogin;
    this.cbScoreRemember = cbScoreRemember;
    this.etScorePassword = etScorePassword;
    this.etScoreUsername = etScoreUsername;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityScoreLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityScoreLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_score_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityScoreLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_scoreLogin;
      Button btnScoreLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnScoreLogin == null) {
        break missingId;
      }

      id = R.id.cb_scoreRemember;
      CheckBox cbScoreRemember = ViewBindings.findChildViewById(rootView, id);
      if (cbScoreRemember == null) {
        break missingId;
      }

      id = R.id.et_scorePassword;
      EditText etScorePassword = ViewBindings.findChildViewById(rootView, id);
      if (etScorePassword == null) {
        break missingId;
      }

      id = R.id.et_scoreUsername;
      EditText etScoreUsername = ViewBindings.findChildViewById(rootView, id);
      if (etScoreUsername == null) {
        break missingId;
      }

      return new ActivityScoreLoginBinding((LinearLayout) rootView, btnScoreLogin, cbScoreRemember,
          etScorePassword, etScoreUsername);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
