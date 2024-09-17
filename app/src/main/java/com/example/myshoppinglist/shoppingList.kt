package com.example.myshoppinglist

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

data class shoppingItem(val id: Int,
                        var name: String,
                        var quantity: Int,
                        var isEditing: Boolean = false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<shoppingItem>()) }
    var ShowDialogue by remember { mutableStateOf(false) }
    var itemName by remember{ mutableStateOf("") }
    var itemQuantity by remember{ mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = { ShowDialogue = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        {
            Text(text = "Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp))
        {
            items(sItems) {
                item ->
                if (item.isEditing){
                    ShoppingItemEditor(item = item , onClickComplete ={
                        editedName,editedQuantity->
                        sItems=sItems.map { it.copy(isEditing = false) }
                        val editeditem=sItems.find { it.id==item.id }
                        editeditem?.let {
                            it.name=editedName
                            it.quantity=editedQuantity
                        }
                    } )
                }
                else{
                    ShoppingListItem(item = item, onClickEdit = {
                        sItems=sItems.map { it.copy(isEditing = it.id==item.id)}
                    },
                        onClickDelete = {
                            sItems=sItems-item
                        })
                }
            }
        }
    }
    if (ShowDialogue) {
        AlertDialog(onDismissRequest = { ShowDialogue=false },
            confirmButton = {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Button(onClick = {
                                    if(itemName.isNotBlank()){
                                        val newItem=shoppingItem(id = sItems.size+1,
                                            name = itemName,
                                            quantity = itemQuantity.toIntOrNull()?:1)
                                        sItems=sItems+newItem
                                        ShowDialogue=false
                                        itemName=""
                                    }
                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = { ShowDialogue=false }) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {
                          itemName=it
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity=it
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            })
    }
}
@Composable
fun ShoppingItemEditor(item: shoppingItem,onClickComplete:(String,Int)->Unit){
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
    var isEditing by remember{ mutableStateOf(item.isEditing) }

    Row(modifier= Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {isEditing = false
        onClickComplete(editedName,editedQuantity.toIntOrNull()?:1)}) {
            Text(text = "Save")
        }
    }
}
@Composable
fun ShoppingListItem(
    item: shoppingItem,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
){
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
        .border(
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = item.name, modifier=Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier=Modifier.padding(8.dp))
        Row {
            IconButton(onClick = onClickEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Item")
            }
            IconButton(onClick = onClickEdit) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}