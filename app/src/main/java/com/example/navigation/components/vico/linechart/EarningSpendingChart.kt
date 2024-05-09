package com.example.navigation.components.vico.linechart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.navigation.data.calculateMonthlyEarnings
import com.example.navigation.data.calculateMonthlySpending
import com.example.navigation.data.earnings.MonthlyEarning
import com.example.navigation.data.earnings.earnings
import com.example.navigation.data.spending.MonthlySpending
import com.example.navigation.data.spending.spending
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.common.shader.DynamicShader

@Composable
fun EarningSpendingChart(earnings: List<MonthlyEarning>, spending: List<MonthlySpending>) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.Black.copy(alpha = 0.1f),
    ) {
        val red = Color(0xFFFF5252)
        val green = Color(0xFF4CAF50)


        val totalAccountValue = maxOf(earnings.maxOfOrNull { it.amount } ?: 0f,
            spending.maxOfOrNull { it.amount } ?: 0f)

        // Find months where both earnings and spending exist
        val commonMonths = earnings.map { it.month }.intersect(spending.map { it.month }.toSet())
        val months = earnings.map { it.month }.union(spending.map { it.month })

        val earningValues = earnings.filter { it.month in commonMonths }.map { it.amount }
        val earningLabels = commonMonths.map { it.toString() }

        val spendingValues = spending.filter { it.month in commonMonths }.map { it.amount }
        val spendingLabels = commonMonths.map { it.toString() }

        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    listOf(
                        rememberLineSpec(
                            shader = DynamicShader.color(green),
                            backgroundShader = DynamicShader.verticalGradient(
                                arrayOf(green.copy(alpha = 0.5f), green.copy(alpha = 0f)),
                            ),
                        ),
                        rememberLineSpec(
                            shader = DynamicShader.color(red),
                            backgroundShader = DynamicShader.verticalGradient(
                                arrayOf(red.copy(alpha = 0.5f), red.copy(alpha = 0.1f)),
                            ),
                        ),
                    ),
                    axisValueOverrider = AxisValueOverrider.fixed(maxY = totalAccountValue),
                ),
               // bottomAxis = rememberBottomAxis(commonMonths.map { it.toString() }),
            ), model = CartesianChartModel(LineCartesianLayerModel.build {
                series(earningValues)
                series(spendingValues)
            }), horizontalLayout = HorizontalLayout.fullWidth()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun main(){
    val monthlyEarnings = calculateMonthlyEarnings(earnings)
    val monthlySpending = calculateMonthlySpending(spending)
    println(monthlyEarnings)
    println(monthlySpending)
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    "Monthly Transaction Line Chart", widthDp = 200, showBackground = false, showSystemUi = false
)
@Composable
fun PreviewMonthlyTransactionLineChart() {
    val monthlyEarnings = calculateMonthlyEarnings(earnings)
    val monthlySpending = calculateMonthlySpending(spending)
    EarningSpendingChart(earnings = monthlyEarnings, spending = monthlySpending)
}
