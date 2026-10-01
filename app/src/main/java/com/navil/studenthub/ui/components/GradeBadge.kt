package com.navil.studenthub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navil.studenthub.model.GpaGrade
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentGreenContainer
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.AccentRoseContainer
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.theme.PrimaryIndigoContainer

@Composable
fun GradeBadge(
    grade: GpaGrade,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when {
        grade.gradePoint >= 3.5 -> PrimaryIndigoContainer to PrimaryIndigo
        grade.gradePoint >= 2.5 -> AccentGreenContainer to AccentGreen
        else -> AccentRoseContainer to AccentRose
    }

    Box(
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = grade.letter,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
