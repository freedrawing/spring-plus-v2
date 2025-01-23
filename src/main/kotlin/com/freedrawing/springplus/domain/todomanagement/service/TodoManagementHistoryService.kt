package com.freedrawing.springplus.domain.todomanagement.service

import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todomanagement.entity.TodoManagementHistory
import com.freedrawing.springplus.domain.todomanagement.repository.TodoManagementHistoryRepository
import com.freedrawing.springplus.domain.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TodoManagementHistoryService(
    private val todoManagementHistoryRepository: TodoManagementHistoryRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 무조건 새로운 트랜잭션
    fun addTodoManagementHistory(
        todo: Todo,
        manager: User,
        todoOwner: User,
        assigner: User,
        isValidAssignment: Boolean
    ) {
        // 상위 트랜잭션이 롤백될 수도 있기 때문에 `response`를 날리는 게 조금 어려울 듯. 이럴 때는 어떻게 해야 할까?
        todoManagementHistoryRepository.save(
            TodoManagementHistory(
                todo = todo,
                assigner = assigner,
                manager = manager,
                todoOwner = todoOwner,
                isSuccess = isValidAssignment
            )
        )

    }
}