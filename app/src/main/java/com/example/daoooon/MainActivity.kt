package com.example.daoooon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.daoooon.ui.theme.DAOOOONTheme


@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseHelper = DatabaseHelper(applicationContext)

        setContent {
            var students by remember {
                mutableStateOf(
                    databaseHelper?.getStudents() ?: emptyList()
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column {
                    DisplayStudentsCard(students)
                    AddStudentCard(databaseHelper) {
                        students = databaseHelper?.getStudents() ?: emptyList()
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayStudentsCard(students: List<Student>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Text(
            students.joinToString("\n") { it.toString() },
            modifier = Modifier.verticalScroll(ScrollState(0))
        )
    }
}

@Composable
private fun AddStudentCard(
    databaseHelper: DatabaseHelper?, onStudentsUpdated: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column {
            val name = remember { mutableStateOf("") }
            val birthday = remember { mutableStateOf("") }
            val groupId = remember { mutableStateOf("") }

            StudentInfoFields(databaseHelper, name, birthday, groupId, onStudentsUpdated) {
                databaseHelper?.insert(name.value, birthday.value, groupId.value)
                onStudentsUpdated()
            }
        }
    }
}

@Composable
private fun StudentInfoFields(
    databaseHelper: DatabaseHelper?,
    name: MutableState<String>,
    birthday: MutableState<String>,
    groupId: MutableState<String>,
    onStudentsUpdated: () -> Unit,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        FieldButton("Имя", "Иван", name)
        FieldButton("Дата рождения", "01.01.2000", birthday)
        FieldButton("Номер группы", "09-252", groupId)

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            OutlinedButton(
                onClick = { onAddClick() },
            ) {
                Text("Добавить студента")
            }

            Spacer(modifier = Modifier.width(20.dp))

            OutlinedButton(
                onClick = {
                    databaseHelper?.clear()
                    onStudentsUpdated()
                },
            ) {
                Text("Удалить все")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldButton(label: String, placeholder: String, state: MutableState<String>) {

    OutlinedTextField(value = state.value,
        onValueChange = { state.value = it },
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.Transparent),
        label = { Text(label) },
        placeholder = { Text(placeholder) })
}