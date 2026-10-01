package com.navil.studenthub.model

enum class GpaGrade(val letter: String, val gradePoint: Double) {
    A_PLUS("A+", 4.00),
    A("A", 4.00),
    A_MINUS("A-", 3.70),
    B_PLUS("B+", 3.30),
    B("B", 3.00),
    B_MINUS("B-", 2.70),
    C_PLUS("C+", 2.30),
    C("C", 2.00),
    C_MINUS("C-", 1.70),
    D_PLUS("D+", 1.30),
    D("D", 1.00),
    D_MINUS("D-", 0.70),
    F("F", 0.00);

    companion object {
        fun fromLetter(letter: String): GpaGrade {
            return entries.firstOrNull { it.letter.equals(letter, ignoreCase = true) } ?: A
        }
    }
}
