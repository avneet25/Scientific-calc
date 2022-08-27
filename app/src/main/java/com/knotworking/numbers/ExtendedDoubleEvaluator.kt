package com.knotworking.numbers

import com.fathzer.soft.javaluator.DoubleEvaluator
import com.fathzer.soft.javaluator.Function
import com.fathzer.soft.javaluator.Parameters

class ExtendedDoubleEvaluator : DoubleEvaluator(PARAMS) {
    companion object {
        private val SQRT = Function("sqrt", 1)
        private var PARAMS: Parameters? = null

        init {
            PARAMS = getDefaultParameters()
            PARAMS!!.add(SQRT)
        }
    }

    public override fun evaluate(function: Function, arguments: Iterator<Double>, evaluationContext: Any): Double {
        return if (function === SQRT) {
            Math.sqrt(arguments.next())
        } else {
            super.evaluate(function, arguments, evaluationContext)
        }
    }
}