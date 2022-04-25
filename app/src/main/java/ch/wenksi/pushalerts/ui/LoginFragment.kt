package ch.wenksi.pushalerts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.FragmentLoginBinding
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel
import ch.wenksi.pushalerts.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasActiveSession()) {
            navigateToHomeScreen()
        }
        observeLogin()
        binding.btnLogin.setOnClickListener { onLoginTapped() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasActiveSession(): Boolean {
        return false
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(R.id.action_LoginFragment_to_MainFragment)
    }

    private fun onLoginTapped() {
        val email = binding.tilEmailAddress.editText?.text.toString().trim()
        val password = binding.tilPassword.editText?.text.toString().trim()
        userViewModel.login(email, password)
    }

    private fun observeLogin() {
        userViewModel.token.observe(viewLifecycleOwner) {
            // Store token
            Snackbar.make(binding.root, "Logged in user", Snackbar.LENGTH_SHORT).show()
            navigateToHomeScreen()
        }
        userViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }
}