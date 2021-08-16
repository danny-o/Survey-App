package com.pula.surveyapp.ui


import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pula.surveyapp.R
import com.pula.surveyapp.databinding.FragmentQuestionsBinding
import com.pula.surveyapp.models.Question
import com.pula.surveyapp.utils.OnNextClickListener
import java.lang.NumberFormatException

class QuestionsFragment:Fragment() {

    lateinit var binding: FragmentQuestionsBinding

    lateinit var mainActivityViewModel: MainActivityViewModel

    lateinit var onNextClickListener: OnNextClickListener

    var questionText:String?=null


    var question: Question?=null


    var answer:String?=null

    lateinit var questionType:String

    lateinit var answerType:String

    var position:Int=0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentQuestionsBinding.inflate(inflater,container,false)

        val view=binding.root

        mainActivityViewModel=ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        position=arguments?.getInt(ARG_POSITION)!!





        questionText = if(position==0){

            mainActivityViewModel.startQuestion

        }
        else{

            mainActivityViewModel.nextQuestion
        }

        if(questionText==null){
            binding.ivSurveyComplete.isVisible=true

            binding.tvSurveyComplete.isVisible=true

            binding.radioGroup.isVisible=false

            binding.btnNext.isVisible=false
            binding.tvQuestion.isVisible=false

            binding.etAnswer.isVisible=false
        }
        else{
            populateQuestion()
        }









        return view
    }

    fun populateQuestion(){
        mainActivityViewModel.getQuestionsWithOptions(questionText.toString()).observe(viewLifecycleOwner,{ questionWithOptions->




            Log.d(QuestionsFragment::class.simpleName,questionText.toString())


            question=questionWithOptions[position!!].question

            mainActivityViewModel.nextQuestion=question?.next

            binding.tvQuestion.text=question?.question_text

            questionText=question?.question_text

            questionType=question?.question_type.toString()

            answerType=question?.answer_type.toString()




            val questionString=mainActivityViewModel.strings?.getString(questionText.toString())


            binding.tvQuestion.text=questionString

            Log.d(QuestionsFragment::class.simpleName,questionWithOptions.size.toString())




            binding.tvQuestion.text=questionString





            questionType=questionWithOptions[position].question.question_type

            answerType= questionWithOptions[position].question.answer_type

            if(questionWithOptions[position].question.question_type== QUESTION_TYPE_SELECT_ONE){

                binding.radioGroup.isVisible=true

                binding.etAnswer.isVisible=false
                questionWithOptions[position].options.let { options->



                    options.forEach {option->

                        val radioButton=RadioButton(requireContext())

                        radioButton.id=options.indexOf(option).plus(1)



                        radioButton.text=mainActivityViewModel.strings?.getString(option.displayText)

                        binding.radioGroup.addView(radioButton)

                    }
                }
                setRadioGroupOnClickListener()
            }
            else {

                binding.etAnswer.isVisible=true

                binding.radioGroup.isVisible=false

                if(questionWithOptions[position].question.answer_type== ANSWER_TYPE_FLOAT){
                    binding.etAnswer.inputType=InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
                else if(questionWithOptions[position].question.answer_type== ANSWER_TYPE_SINGLE_LINE_TEXT){
                    binding.etAnswer.inputType= InputType.TYPE_CLASS_TEXT
                }
            }

        })

        setButtonNextOnClickListener()
    }

    private fun setButtonNextOnClickListener() {

        binding.btnNext.setOnClickListener {
            if(verifyInput()){
                onNextClickListener.onClickNext()
            }
        }

    }

    private fun setRadioGroupOnClickListener() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            Log.d(QuestionsFragment::class.simpleName,answer.toString())

           answer= group.findViewById<RadioButton>(checkedId).text.toString()


        }
    }

    fun verifyInput():Boolean{

        if(questionType== QUESTION_TYPE_FREE_TEXT){
            if(binding.etAnswer.text.isEmpty()){
                binding.etAnswer.error=getString(R.string.answer_required)
                return false
            }
            mainActivityViewModel.answers.answerInputs[binding.tvQuestion.text.toString()]=binding.etAnswer.text.toString()
        }
        if(questionType== QUESTION_TYPE_SELECT_ONE){



            if(answer==null){

                Toast.makeText(requireContext(), getString(R.string.make_a_a_selection), Toast.LENGTH_SHORT).show()



                return false
            }
            mainActivityViewModel.answers.answerInputs[binding.tvQuestion.text.toString()]=answer.toString()
        }
        if(answerType== ANSWER_TYPE_FLOAT){
            try {

                val _answer=binding.etAnswer.text.toString().toFloat()

                answer=_answer.toString()

                mainActivityViewModel.answers.answerInputs[binding.tvQuestion.text.toString()]=answer.toString()



            }
            catch (e:NumberFormatException){

                binding.etAnswer.error=getString(R.string.number_required)
                return false
            }


        }


        return true
    }

    companion object{

        const val ARG_POSITION="position"

        const val QUESTION_TYPE_SELECT_ONE="SELECT_ONE"

        const val QUESTION_TYPE_FREE_TEXT="FREE_TEXT"

        const val ANSWER_TYPE_FLOAT="FLOAT"

        const val ANSWER_TYPE_SINGLE_LINE_TEXT="SINGLE_LINE_TEXT"

    }


}

