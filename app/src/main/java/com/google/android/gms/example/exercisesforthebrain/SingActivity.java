package com.google.android.gms.example.exercisesforthebrain;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingActivity extends BaseActivity implements
        View.OnClickListener {
    //private static final String TAG = "EmailPassword";

    @BindView(R.id.status)
    TextView mStatusTextView;
    @BindView(R.id.detail)
    TextView mDetailTextView;
    @BindView(R.id.email_sign_in_button)
    Button singInButton;
    @BindView(R.id.email_create_account_button)
    Button createButton;
    @BindView(R.id.sign_out_button)
    Button singOutButton;
    @BindView(R.id.verify_email_button)
    Button verifyButton;
    @BindView(R.id.forgot_password_botton)
    Button forgotButton;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        ButterKnife.bind(this);
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);

        // Buttons

        singInButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        singOutButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        forgotButton.setOnClickListener(this);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        //Log.d(TAG, "createAccount: " + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SingActivity.this, getString(R.string.singCreateAcWhEmailSus),
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "Create User With Email:failure", task.getException());
                            Toast.makeText(SingActivity.this, getString(R.string.singCreateAcWhEmailFai),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        //Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (!user.isEmailVerified()) {
                                    //Log.d(TAG, "Sign In With Email:success");
                                    Toast.makeText(SingActivity.this, getString(R.string.singActivateAcountByEmail),
                                            Toast.LENGTH_LONG).show();
                                }
                                updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SingActivity.this, getString(R.string.singAutFai),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                            mStatusTextView.setContentDescription(getResources().getString(R.string.auth_failed));
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(SingActivity.this,
                                        getString(R.string.singVerifiEmailSentTo) + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                                singInButton.setVisibility(View.VISIBLE);
                                singInButton.setEnabled(true);
                                singOutButton.setVisibility(View.INVISIBLE);
                                singOutButton.setEnabled(false);
                                createButton.setVisibility(View.VISIBLE);
                                createButton.setEnabled(true);
                                verifyButton.setVisibility(View.INVISIBLE);
                                verifyButton.setEnabled(false);
                                mEmailField.setVisibility(View.VISIBLE);
                                mEmailField.setEnabled(true);
                                mPasswordField.setEnabled(true);
                                mPasswordField.setVisibility(View.VISIBLE);
                            } else {
                                //Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(SingActivity.this,
                                        getString(R.string.singVerifiEmailSentFailure),
                                        Toast.LENGTH_SHORT).show();
                            }
                            // [END_EXCLUDE]
                        }
                    });
            // [END send_email_verification]
        }
    }

    private boolean forgotPaswword() {
        boolean forgot = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.singEmailDataRequiered));
            forgot = false;
        } else {
            mEmailField.setError(null);
        }
        return forgot;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.singEmailDataRequiered));
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError(getString(R.string.singPasswordDataRequiered));
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            if (user.isEmailVerified()) {

                Intent intent = new Intent(SingActivity.this, MainActivity.class);
                intent.putExtra("singin", "true");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //Close "Day activity" and the current Activity.  | Intent.FLAG_ACTIVITY_SINGLE_TOP
            } else {
                singInButton.setVisibility(View.VISIBLE);
                singInButton.setEnabled(true);
                singOutButton.setVisibility(View.INVISIBLE);
                singOutButton.setEnabled(false);
                createButton.setVisibility(View.INVISIBLE);
                createButton.setEnabled(false);
                verifyButton.setVisibility(View.VISIBLE);
                verifyButton.setEnabled(true);
            }
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mStatusTextView.setContentDescription(getResources().getString(R.string.signed_out));
            mDetailTextView.setText(null);

            mEmailField.setVisibility(View.VISIBLE);
            mEmailField.setEnabled(true);
            mPasswordField.setEnabled(true);
            mPasswordField.setVisibility(View.VISIBLE);

            singInButton.setVisibility(View.VISIBLE);
            singInButton.setEnabled(true);
            singOutButton.setVisibility(View.INVISIBLE);
            singOutButton.setEnabled(false);
            createButton.setVisibility(View.VISIBLE);
            createButton.setEnabled(true);
            verifyButton.setVisibility(View.INVISIBLE);
            verifyButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        } else if (i == R.id.forgot_password_botton) {
            resetPassword();
        }
    }

    private void resetPassword() {

        if (!forgotPaswword()) {
            return;
        }
        String email = mEmailField.getText().toString().trim();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SingActivity.this, getString(R.string.singInstructionRestPass), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SingActivity.this, getString(R.string.singInstructionRestPassFailed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
