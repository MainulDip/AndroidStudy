/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.dagger.registration.enterdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.android.dagger.MyApplication
import com.example.android.dagger.R
import com.example.android.dagger.registration.RegistrationActivity
import com.example.android.dagger.registration.RegistrationViewModel
import javax.inject.Inject

class EnterDetailsFragment : Fragment() {

    /**
     * RegistrationViewModel is used to set the username and password information (attached to
     * Activity's lifecycle and shared between different fragments)
     * EnterDetailsViewModel is used to validate the user input (attached to this
     * Fragment's lifecycle)
     *
     * They could get combined but for the sake of the codelab, we're separating them so we have
     * different ViewModels with different lifecycles.
     */
//    private lateinit var registrationViewModel: RegistrationViewModel
//    private lateinit var enterDetailsViewModel: EnterDetailsViewModel

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel

    @Inject
    lateinit var enterDetailsViewModel: EnterDetailsViewModel

    private lateinit var errorTextView: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        (requireActivity().application as MyApplication).appComponent.inject(this)
        (activity as RegistrationActivity).registrationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enter_details, container, false)

        // Commenting as this 2 instantiation is now injected using dagger
//        registrationViewModel = (activity as RegistrationActivity).registrationViewModel
//
//        enterDetailsViewModel = EnterDetailsViewModel()

        /**
         * setting observable on enterDetailsState (which is a liveData)
         * so, if enterDetailsState value gets changed, the associated block
         * underneath will be called
         */
        enterDetailsViewModel.enterDetailsState.observe(
            viewLifecycleOwner
        ) { state ->
            when (state) {
                is EnterDetailsSuccess -> {

                    val username = usernameEditText.text.toString()
                    val password = passwordEditText.text.toString()
                    registrationViewModel.updateUserData(username, password)

                    (activity as RegistrationActivity).onDetailsEntered()
                }

                is EnterDetailsError -> {
                    errorTextView.text = state.error
                    errorTextView.visibility = View.VISIBLE
                }
            }
        }

        setupViews(view)
        return view
    }

    /**
     * User Registration Form
     * After Adding Credentials, Next Button press will Trigger EnterDetailsViewModel's validate fn
     * On validation success, fields will be stored, and as the property is a
     * observed live data, if it changes, it will trigger the observe callback declared
     * inside onCreateView of this (EnterDetailsFragment) class, which will again trigger
     * RegistrationViewModel's updateUserData fn and trigger
     * the RegistrationActivity's onDetailsEntered fn, from there, will call
     * TermsAndCondition Fragment, if accepted, will call again the RegistrationActivity's
     * onTermsAndConditionsAccepted() fn, which will call registrationViewModel.registerUser(),
     * and finally it will call UserManager's register fn, from there the SharedPreference Store
     * will be populated, and will instantiate UserDataRepository with current UserManager Object
     */

    private fun setupViews(view: View) {
        errorTextView = view.findViewById(R.id.error)

        usernameEditText = view.findViewById(R.id.username)
        usernameEditText.doOnTextChanged { _, _, _, _ -> errorTextView.visibility = View.INVISIBLE }

        passwordEditText = view.findViewById(R.id.password)
        passwordEditText.doOnTextChanged { _, _, _, _ -> errorTextView.visibility = View.INVISIBLE }

        view.findViewById<Button>(R.id.next).setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            enterDetailsViewModel.validateInput(username, password)
        }
    }
}

/**
 * Sealed class used here are for defining the livedata's type and restricting
 * livedata's observe callback's when case (so no need to use else block)
 */

sealed class EnterDetailsViewState
object EnterDetailsSuccess : EnterDetailsViewState()
data class EnterDetailsError(val error: String) : EnterDetailsViewState()
