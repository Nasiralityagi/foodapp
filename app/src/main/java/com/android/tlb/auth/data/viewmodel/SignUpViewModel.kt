package com.android.tlb.auth.data.viewmodel

import android.text.TextUtils
import android.view.View
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.android.tlb.R
import com.android.tlb.Tlb
import com.android.tlb.auth.data.listener.AuthListener
import com.android.tlb.auth.data.model.AuthResponse
import com.android.tlb.auth.data.repository.AuthRepository
import com.android.tlb.liveobserver.LiveMessageEvent
import com.android.tlb.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class SignUpViewModel(private val repository: AuthRepository) : ViewModel() {

    private var disposable: Disposable? = null
    private var navigationEvent: LiveMessageEvent<AuthListener> = LiveMessageEvent()
    var firstname: ObservableField<String> = ObservableField("")
    var mobileno: ObservableField<String> = ObservableField("")
    var emailid: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var age: ObservableField<String> = ObservableField("Birth date")
    var zip: ObservableField<String> = ObservableField("")
    var gender: ObservableField<String> = ObservableField("Male")
    var profileImg: ObservableField<String> = ObservableField("")
    var progress: ObservableInt = ObservableInt(View.GONE)

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }


    fun getNavigationEvent(): LiveMessageEvent<AuthListener> {
        return navigationEvent
    }

   /* fun onUserNameChanged(text: CharSequence, start: Int, before: Int, count: Int) {

    }*/

    fun onEmailTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if (text.isNotEmpty()) {
            if (isValidEmail(text) /*|| isNotValidMobile(text)*/) {
                setError(false, INPUT_EMAIL)
            } else {
                setError(true, INPUT_EMAIL)
            }
        }
    }

   /* fun onMobileNoChanged(text: CharSequence, start: Int, before: Int, count: Int) {

    }*/

    fun onPasswordTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if (text.isNotEmpty()) {
            if (isPassValid(text)) {
                setError(false, INPUT_PASS)
            } else {
                setError(true, INPUT_PASS)
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        /*Min a@b.cc*/
        var isValid = false
        if (email.isEmpty()) {
            navigationEvent.sendEvent { showToast("Enter an email!") }
        } else if (Utils.isEmailValid(email)) {
            isValid = true
        }
        return isValid
    }

    private fun isPassValid(password: CharSequence): Boolean {
        var isValid = false
        if (password.isEmpty()) {
            navigationEvent.sendEvent { showToast("Enter a password!") }
        } else if (password.length > 7) {
            isValid = true
        }
        return isValid
    }

    private fun setError(isError: Boolean, inputType: String) {
        navigationEvent.sendEvent { setError(isError, inputType) }
    }

    fun openDatePicker() {
        navigationEvent.sendEvent { openDatePicker() }
    }

    fun onCheckedChanged(radioGroup: RadioGroup, checkedId: Int) {
        if (checkedId == R.id.male) {
            gender.set("Male")
        } else if (checkedId == R.id.female) {
            gender.set("Female")
        } else if (checkedId == R.id.other) {
            gender.set("Other")
        }
    }

    private fun notifyProgressVisibility(visibility: Int) {
        navigationEvent.sendEvent { pgVisibility(visibility) }
    }

    private fun sendSignUpData(data: AuthResponse) {
       // navigationEvent.sendEvent { loginData(data) }
    }

    fun onImageSelection() {
        navigationEvent.sendEvent { onImageSelection() }
    }

    fun enterSignUp() {

        notifyProgressVisibility(View.VISIBLE)

        if (Tlb.getInstance().isConnected()) {
            val name = firstname.get()!!
            val emailId = emailid.get()!!
            val mobileno = mobileno.get()!!
            val password = password.get()!!
            val age = age.get()!!
            val zip = zip.get()!!
            val gender = gender.get()!!
            val profileImg = profileImg.get()!!

            if (TextUtils.isEmpty(firstname.get()!!)) {
                navigationEvent.sendEvent { showToast("Name filed is invalid!") }
                return
            } else if (TextUtils.isEmpty(emailId) || !isValidEmail(emailId)) {
                navigationEvent.sendEvent { showToast("Email-Id filed is invalid!") }
                return
            } else if (TextUtils.isEmpty(mobileno)) {
                navigationEvent.sendEvent { showToast("Mobile number fileInd is invalid!") }
                return
            } else if (TextUtils.isEmpty(password) || !isPassValid(password)) {
                navigationEvent.sendEvent { showToast("Password filed is invalid, min length 8!") }
                return
            } else if (TextUtils.isEmpty(age) || age == "Birth date") {
                navigationEvent.sendEvent { showToast("Age filed is invalid!") }
                return
            } else if (TextUtils.isEmpty(zip)) {
                navigationEvent.sendEvent { showToast("Zip code filed is invalid!") }
                return
            } else if (TextUtils.isEmpty(gender)) {
                navigationEvent.sendEvent { showToast("Select your gender!") }
                return
            } else if (TextUtils.isEmpty(profileImg)) {
                navigationEvent.sendEvent { showToast("Choose profile image!") }
                return
            }

            disposable?.dispose()//dispose  pre call

            try {
                val file = File(profileImg)
                val requestFile: RequestBody = RequestBody.create(MediaType.parse("*/*"), file)
                val fileBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("user_profile", file.name, requestFile)

                val name: RequestBody = createRequestBody(name)
                val emailId: RequestBody = createRequestBody(emailId)
                val phone: RequestBody = createRequestBody(mobileno)
                val password: RequestBody = createRequestBody(password)
                val age: RequestBody = createRequestBody(age)
                val zip: RequestBody = createRequestBody(zip)
                val gender: RequestBody = createRequestBody(gender)

                val params: HashMap<String, RequestBody> = HashMap()
                params["name"] = name
                params["email"] = emailId
                params["phone"] = phone
                params["password"] = password
                params["birthday"] = age
                params["zip"] = zip
                params["gender"] = gender

                disposable = repository.signUp(params, fileBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        sendSignUpData(result)
                        repository.setLoggedInUser(result)
                        notifyProgressVisibility(View.GONE)
                    }, { error ->
                        notifyProgressVisibility(View.GONE)
                        navigationEvent.sendEvent { showToast(error.message!!) }
                    })

            } catch (e: Exception) {
                progress.set(View.GONE)
                navigationEvent.sendEvent { showToast(e.message!!) }
            }
        } else {
            notifyProgressVisibility(View.GONE)
            navigationEvent.sendEvent { showToast("Something went wrong internet or data!") }
        }
    }

    fun createRequestBody(s: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), s)
    }

}