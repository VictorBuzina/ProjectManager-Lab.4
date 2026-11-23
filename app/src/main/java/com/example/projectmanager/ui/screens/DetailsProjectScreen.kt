package com.example.projectmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmanager.data.ProjectViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsProjectScreen(
    projectId: Int,
    viewModel: ProjectViewModel,
    onBack: () -> Unit
) {
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.id == projectId }

    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Деталі проекту", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1E88E5)
                )
            )
        }
    ) { padding ->
        if (project == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Проект не знайдено")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = project.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = project.description.ifBlank { "Без опису" },
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Прогрес:", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "${(project.progress * 100).roundToInt()}%",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = project.progress,
                        onValueChange = { viewModel.updateProgress(project.id, it) },
                        valueRange = 0f..1f
                    )
                }
            }

            OutlinedButton(
                onClick = { showEditDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редагувати")
            }

            Button(
                onClick = {
                    viewModel.deleteProject(project.id)
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Видалити", color = Color.White)
            }
        }

        if (showEditDialog) {
            var editTitle by remember { mutableStateOf(project.title) }
            var editDesc by remember { mutableStateOf(project.description) }

            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Редагувати проект") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Назва") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editDesc,
                            onValueChange = { editDesc = it },
                            label = { Text("Опис") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.updateProject(project.id, editTitle, editDesc)
                            showEditDialog = false
                        }
                    ) { Text("Зберегти") }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("Скасувати") }
                }
            )
        }
    }
}