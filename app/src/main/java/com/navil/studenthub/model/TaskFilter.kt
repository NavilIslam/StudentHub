package com.navil.studenthub.model

enum class TaskFilter(val title: String) {
    ALL("All"),
    TODAY("Today"),
    UPCOMING("Upcoming"),
    COMPLETED("Completed")
}

enum class TaskSortOrder(val title: String) {
    DUE_DATE("Due Date"),
    PRIORITY("Priority"),
    COURSE("Course")
}
