package com.example.projectmanager.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository

    val projects: StateFlow<List<Project>>

    init {
        val dao = AppDatabase.getDatabase(application).projectDao()
        repository = ProjectRepository(dao)
        projects = repository.allProjects.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addProject(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insert(
                Project(
                    title = title.trim(),
                    description = description.trim(),
                    progress = 0f
                )
            )
        }
    }

    fun updateProgress(id: Int, newProgress: Float) {
        viewModelScope.launch {
            val current = repository.getProjectById(id) ?: return@launch
            repository.update(current.copy(progress = newProgress))
        }
    }

    fun updateProject(id: Int, newTitle: String, newDescription: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            val current = repository.getProjectById(id) ?: return@launch
            repository.update(
                current.copy(
                    title = newTitle.trim(),
                    description = newDescription.trim()
                )
            )
        }
    }

    fun deleteProject(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}