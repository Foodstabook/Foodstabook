// Generated by view binder compiler. Do not edit!
package com.example.foodstabook.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.foodstabook.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button foodSuggestionButton;

  @NonNull
  public final Button loginButton;

  @NonNull
  public final ImageButton logo;

  @NonNull
  public final Button resetPasswordButton;

  @NonNull
  public final Button userAccountButton;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button foodSuggestionButton, @NonNull Button loginButton, @NonNull ImageButton logo,
      @NonNull Button resetPasswordButton, @NonNull Button userAccountButton) {
    this.rootView = rootView;
    this.foodSuggestionButton = foodSuggestionButton;
    this.loginButton = loginButton;
    this.logo = logo;
    this.resetPasswordButton = resetPasswordButton;
    this.userAccountButton = userAccountButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.food_suggestion_button;
      Button foodSuggestionButton = ViewBindings.findChildViewById(rootView, id);
      if (foodSuggestionButton == null) {
        break missingId;
      }

      id = R.id.login_button;
      Button loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.logo;
      ImageButton logo = ViewBindings.findChildViewById(rootView, id);
      if (logo == null) {
        break missingId;
      }

      id = R.id.reset_password_button;
      Button resetPasswordButton = ViewBindings.findChildViewById(rootView, id);
      if (resetPasswordButton == null) {
        break missingId;
      }

      id = R.id.user_account_button;
      Button userAccountButton = ViewBindings.findChildViewById(rootView, id);
      if (userAccountButton == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, foodSuggestionButton, loginButton,
          logo, resetPasswordButton, userAccountButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}