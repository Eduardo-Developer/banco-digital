package com.edudev.bancodigital.presenter.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.databinding.FragmentProfileBinding
import com.edudev.bancodigital.databinding.LayoutBottomSheetBinding
import com.edudev.bancodigital.databinding.LayoutBottomSheetImageProfileBinding
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private var imageProfile: String? = null
    private var currentPhotoPath: String? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, true, light = false)
        getProfile()
        initListener()
    }

    private fun initListener() {
        binding.btnSend.setOnClickListener {
            if (user != null)
                if (imageProfile != null) { //Usuário selecionou uma imagem
                    saveImageProfile()
                } else { //Usuário não selecionou uma imagem
                    validateData()
                }
        }

        binding.imageUser.setOnClickListener {
            showBottomSheetImage()
        }
    }

    private fun checkPermissionCamera() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Permission Concedida", Toast.LENGTH_SHORT).show()
                openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permissão Negada\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        showDialogPermissionDenied(
            permissionlistener = permissionlistener,
            permission = android.Manifest.permission.CAMERA,
            R.string.text_message_access_camera_denied_profile_fragment
        )
    }

    private fun checkPermissionGallery() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Permission Concedida", Toast.LENGTH_SHORT).show()
                openGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permissão Negada\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        showDialogPermissionDenied(
            permissionlistener = permissionlistener,
            permission = android.Manifest.permission.READ_EXTERNAL_STORAGE,
            R.string.text_message_access_gallery_denied_profile_fragment
        )
    }

    private fun showDialogPermissionDenied(
        permissionlistener: PermissionListener,
        permission: String,
        message: Int
    ) {
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedTitle("Permissão negada")
            .setDeniedMessage(message)
            .setDeniedCloseButtonText("Não")
            .setGotoSettingButtonText("Sim")
            .setPermissions(permission)
            .check();

    }

    private fun showBottomSheetImage() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetBinding: LayoutBottomSheetImageProfileBinding =
            LayoutBottomSheetImageProfileBinding.inflate(layoutInflater, null, false)

        bottomSheetBinding.btnCamera.setOnClickListener {
            checkPermissionCamera()
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnGallery.setOnClickListener {
            checkPermissionGallery()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun saveImageProfile() {
        imageProfile?.let { image ->
            viewModel.saveImageProfile(image).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        saveProfile(stateView.data)

                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = stateView.message)

                    }
                }
            }
        }
    }

    private fun getProfile() {
        viewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let { user = it }
                    configData()
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                    showBottomSheet(
                        message = getString(
                            FirebaseHelper.validError(
                                stateView.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun saveProfile(urlImage: String? = null) {
        user?.let {

            if (urlImage != null) {
                it.image = urlImage
            }

            viewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(
                            message = getString(R.string.text_message_save_success)
                        )
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                        showBottomSheet(
                            message = getString(
                                FirebaseHelper.validError(
                                    stateView.message ?: ""
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(
                requireContext(),
                "Não foi possível abrir a câmera do dispositivo",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.edudev.bancodigital.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraLauncher.launch(takePictureIntent)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale("pt", "BR")).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        currentPhotoPath = image.absolutePath
        return image
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath!!)
            binding.imageUser.setImageURI(Uri.fromFile(file))
            imageProfile = file.toURI().toString()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val imageSelected = result.data!!.data
            imageProfile = imageSelected.toString()

            if (imageSelected != null) {
                binding.imageUser.setImageBitmap(getBitmap(imageSelected))
            }

        }
    }

    private fun getBitmap(pathUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, pathUri)
            } else {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, pathUri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun configData() {
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        if (user?.image?.isNotEmpty() == true) {
            Picasso.get()
                .load(user?.image)
                .fit().centerCrop()
                .into(binding.imageUser, object : Callback {
                    override fun onSuccess() {
                        binding.progressImage.isVisible = false
                        binding.imageUser.startAnimation(fadeInAnimation)
                        binding.imageUser.isVisible = true
                    }

                    override fun onError(e: java.lang.Exception?) {
                    }
                })
        } else {
            binding.imageUser.setImageResource(R.drawable.ic_user_place_holder)
            binding.progressImage.isVisible = false
            binding.imageUser.startAnimation(fadeInAnimation)
            binding.imageUser.isVisible = true
        }

        binding.editName.setText(user?.name)
        binding.editPhone.setText(user?.phone)
        binding.editEmail.setText(user?.email)
    }

    private fun validateData() {
        val name = binding.editName.text.toString().trim()
        val phone = binding.editPhone.unMaskedText

        if (name.isNotEmpty()) {
            if (phone?.isNotEmpty() == true) {
                if (phone.length == 11) {
                    user?.name = name
                    user?.phone = phone

                    saveProfile()

                } else {
                    showBottomSheet(message = getString(R.string.text_phone_valid))
                }
            } else {
                showBottomSheet(message = getString(R.string.text_phone_valid))
            }
        } else {
            showBottomSheet(message = getString(R.string.text_name_empty))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}