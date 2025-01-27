package com.freedrawing.springplus.domain.todomanagement.repository

import com.freedrawing.springplus.domain.todomanagement.entity.TodoManagementHistory
import org.springframework.data.jpa.repository.JpaRepository

interface TodoManagementHistoryRepository : JpaRepository<TodoManagementHistory, Long>