package com.kotlinflowsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.kotlinflowsample.databinding.ActivityMainBinding
import com.kotlinflowsample.factory.CommonViewModelFactory
import com.kotlinflowsample.viewmodel.CommonViewModel
import com.mi.aiattorney.flow.resource.Status
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CommonViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//jaskirat
        viewModel=ViewModelProvider(this, CommonViewModelFactory(this))[CommonViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.dogBreadResponse.collect {
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.pb.visibility= View.GONE
                            binding.tvData.visibility= View.VISIBLE
                            binding.tvData.text=it.toString()
                            Log.e("dataReceived",it.toString())
                        }
                        Status.LOADING->{
                            binding.pb.visibility= View.VISIBLE

                        }
                        else -> {
                            binding.pb.visibility= View.GONE

                        }
                    }
                }
            }

        }

        viewModel.fetchBread()
    }
}