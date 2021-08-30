package com.example.crazybottle2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.crazybottle2.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment() {

    val TAG = "AnimationFragment"

    private var rotationFling: FlingAnimation? = null

    lateinit var binding: FragmentAnimationBinding
    lateinit var viewModel: AnimationViewModel

    lateinit var sharedViewModel: MainActivity.SharedViewModel


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Binding and Fragment View Model
        binding = FragmentAnimationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AnimationViewModel::class.java]

        // Set Observers (to use data from dataModel automatically when changes)
        //viewModel.velocity.observe(viewLifecycleOwner, Observer { newVel -> binding.textVelocity.text = "%.2f".format(newVel)})
        viewModel.game.stateId.observe(viewLifecycleOwner, Observer { newState -> binding.textVelocity.text = "$newState"})

        // Animation
        binding.imageBottle.setOnTouchListener { view, m -> handleTouch(m); true }

        // Button navigation
        binding.buttonOptions.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_animationFragment_to_optionsFragment) }
            setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Shared View Model and move data to local viewModel
        // TODO: 28/08/2021 Can be directly passed to local viewModel (with out using the fragment)?
        sharedViewModel = ViewModelProvider(requireActivity())[MainActivity.SharedViewModel::class.java]
        viewModel.listOfSegments = sharedViewModel.listOfSegments
        viewModel.listOfActions = sharedViewModel.listOfActions

        setupFlingAnimations()
    }

    // Manage lifecycle when we lose the focus

    override fun onPause() {
        super.onPause()

        if (viewModel.rotating) {
            rotationFling?.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            viewModel.firstTime -> {
                binding.root.setBackgroundColor(Color.WHITE)
                binding.textScreen.text = "Toca a..."
                binding.actionTextScreen.text = " "
                binding.toPersonTextScreen.text = " "
            }
            viewModel.rotating -> {
                drawBottleEndAnimation(true)
                drawBottleStartingAnimation()
                drawBottleDuringAnimation(viewModel.rotation)
                rotationFling?.setStartValue(viewModel.rotation)
                rotationFling?.setStartVelocity(viewModel.velocity.value ?: 0.0f)
                rotationFling?.start()
            }
            else -> { // Stopped
                binding.imageBottle.rotation = viewModel.rotation
                drawBottleEndAnimation()
            }
        }
    }


    // Menu creation
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnValue = false

        if (item.title == "Share") {
            startActivity(getShareIntent())
            returnValue = true
        } else {
            returnValue = NavigationUI.onNavDestinationSelected(item, binding.root.findNavController())
        }

        return returnValue|| super.onOptionsItemSelected(item)
    }

    private fun getShareIntent() : Intent {
       val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(
            Intent.EXTRA_TEXT,
            binding.textScreen.text)
        return shareIntent
    }

    private fun setupFlingAnimations() {

        rotationFling = FlingAnimation(binding.imageBottle, DynamicAnimation.ROTATION).apply {
            friction = 0.2f

        }.addUpdateListener { animation, value, velocity ->
            viewModel.rotation = value
            viewModel.velocity.value = velocity
            drawBottleDuringAnimation(value)

        }.addEndListener { animation, canceled, value, velocity ->
            if (!canceled) {
                viewModel.onEndAnimation(sharedViewModel.listOfActions.size)
                drawBottleEndAnimation()
            }
        }
    }

    private fun drawBottleStartingAnimation() {

        binding.root.setBackgroundColor(Color.WHITE)

        when (viewModel.game.stateId.value) {
            0 -> {
                binding.actionTextScreen.text = " "
            }
            1 -> {
                binding.textScreen.setTextColor(viewModel.getColor(viewModel.game.fromPerson + 7))
                binding.textScreen.setTextColor(viewModel.getColor(viewModel.game.fromPerson + 7))

                binding.actionTextScreen.setTextColor(viewModel.getColor(viewModel.game.fromPerson + 7))
                binding.actionTextScreen.setTextColor(viewModel.getColor(viewModel.game.fromPerson + 7))
            }
        }
    }

    private fun drawBottleDuringAnimation(value: Float) {

        val segmentNum = viewModel.getNumAngle(value, viewModel.listOfSegments.size)
        val textName = viewModel.listOfSegments.elementAt(segmentNum)
        val color = viewModel.getColor(segmentNum + 7)

        when (viewModel.game.stateId.value) {
            0 -> {
                binding.textScreen.text = textName
                binding.textScreen.setTextColor(color)
            }
            1 -> {
                binding.toPersonTextScreen.text = textName
                binding.toPersonTextScreen.setTextColor(color)
            }
        }
    }

    private fun drawBottleEndAnimation(resumeState: Boolean = false) {

        when (viewModel.game.stateId.value) {
            1 -> { // Ended the state 0
                binding.textScreen.setTextColor(Color.WHITE)
                binding.textScreen.text = sharedViewModel.listOfSegments.elementAt(viewModel.game.fromPerson)

                binding.actionTextScreen.setTextColor(Color.WHITE)
                binding.actionTextScreen.text = sharedViewModel.listOfActions.elementAt(viewModel.actionId)

                binding.toPersonTextScreen.text = ""

                binding.root.setBackgroundColor(viewModel.getColor(viewModel.game.fromPerson+7))

            }
            0 -> { // Ended the state 1
                if (! resumeState) {
                    binding.textScreen.setTextColor(Color.WHITE)
                    binding.textScreen.text =
                        sharedViewModel.listOfSegments.elementAt(viewModel.game.fromPerson)

                    binding.actionTextScreen.setTextColor(Color.WHITE)
                    binding.actionTextScreen.text =
                        sharedViewModel.listOfActions.elementAt(viewModel.actionId)

                    binding.toPersonTextScreen.setTextColor(Color.WHITE)
                    binding.toPersonTextScreen.text =
                        sharedViewModel.listOfSegments.elementAt(viewModel.game.toPerson)

                    binding.root.setBackgroundColor(viewModel.getColor(viewModel.game.toPerson + 7))
                }

            }
        }

    }


    private fun handleTouch(m: MotionEvent)
    {
        val pointerCount = m.pointerCount
        for (i in 0 until pointerCount)
        {
            val pos : Point = Point (m.getX(i), m.getY(i))

            when (m.actionMasked)
            {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.onActionDown(pos)
                    drawBottleStartingAnimation()
                }
                MotionEvent.ACTION_MOVE ->  {
                    // TODO: 26/08/2021 Why can't be initialized earlier 
                    viewModel.rotCenter = Point(
                        binding.imageBottle.pivotX,
                        binding.imageBottle.pivotY)

                    viewModel.onActionMove(pos)
                    rotationFling?.setStartVelocity(viewModel.velocity.value ?: 0.0f)
                    rotationFling?.start()
                }
                MotionEvent.ACTION_UP -> viewModel.onActionUp()
            }
        }
    }


}