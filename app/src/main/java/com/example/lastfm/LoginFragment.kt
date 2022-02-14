package com.example.lastfm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.lastfm.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception
import java.security.MessageDigest

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val pref by lazy {
        CustomPreference(requireContext())
    }
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.login.setText(pref.login)
        binding.password.setText(pref.password)
        viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                pref.login = user.userName
                pref.password = user.password
            }
        }
        viewModel.isSuccessfullyEnter.observe(viewLifecycleOwner) {
            if (it) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, ResultFragment.newInstance())
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Wrong login or password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.signIn.setOnClickListener {
            val userName = binding.login.text.toString()
            val password = binding.password.text.toString()

            viewModel.onSignInClick(userName, password)

        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}