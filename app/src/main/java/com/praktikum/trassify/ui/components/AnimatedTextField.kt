package com.praktikum.trassify.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.ui.theme.LightGray
import com.praktikum.trassify.ui.theme.MainPurple

@Composable
fun AnimatedTextField(
    field: String,
    image: Int? = null,
    onNext: () -> Unit = {},
    onDone: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester.Default,
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MainPurple else LightGray
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val lineOffset = 6.dp.toPx()
                val y = size.height - strokeWidth / 2 + lineOffset
                drawLine(
                    color = borderColor,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    ){
        image?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(24.dp)
            )
        }

        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            textStyle = androidx.compose.ui.text.TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .focusRequester(focusRequester),
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                if (textState.text.isEmpty() && !isFocused) {
                    Text(
                        text = field,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = if (field == "Email") ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {onNext()},
                onDone = {onDone()}
            )
        )
    }
}
