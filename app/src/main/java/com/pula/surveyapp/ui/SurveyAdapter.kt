package com.pula.surveyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pula.surveyapp.utils.OnNextClickListener

class SurveyAdapter(activity: AppCompatActivity, val onNextClickListener:OnNextClickListener, val itemsCount:Int):FragmentStateAdapter(activity) {

    lateinit var currentFragment:Fragment
    override fun getItemCount(): Int {


        return  itemsCount


    }

    override fun createFragment(position: Int): Fragment {


        val questionsFragment=QuestionsFragment()

        val bundle=Bundle()

        bundle.putInt(QuestionsFragment.ARG_POSITION,position)

        questionsFragment.arguments=bundle

        questionsFragment.onNextClickListener=onNextClickListener

        return questionsFragment
    }

    fun verifyInput():Boolean{
        return (currentFragment as QuestionsFragment).verifyInput()


    }



}