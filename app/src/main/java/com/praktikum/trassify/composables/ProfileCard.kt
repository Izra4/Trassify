package com.praktikum.trassify.composables

import com.praktikum.trassify.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.praktikum.trassify.ui.theme.Primary10
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White

@Composable()
fun ProfileCard(
    username : String,
    point : Int,
    image : String,
){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
            .background(color = White),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.border_card),
                contentDescription = "border card",
                modifier = Modifier
                    .align(Alignment.BottomStart)
            )
            Image(
                painter = painterResource(id = R.drawable.ellipse_card),
                contentDescription = "circle card",
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
        }
        Row (modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)){
            Image(
                painter = rememberAsyncImagePainter(model = image) , contentDescription = "image profile user", modifier = Modifier
                    .size(64.dp)
                    .clip(
                        CircleShape
                    )
                    .border(width = 2.dp, color = Primary10, shape = CircleShape))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = username, style = TextType.text19Md)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Kamu punya $point poin!", style = TextType.text12Md)

                Spacer(modifier = Modifier.height(12.dp))

                Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End){
                    MiniButton(onClick = { /*TODO*/ }, label = "Lihat Profile")
                    Spacer(modifier = Modifier.width(4.dp))
                    MiniButton(onClick = { /*TODO*/ }, label = "Lihat Profile")
                }
            }
        }
    }
    }
}

@Preview(showBackground = true)
@Composable()
fun ProfileCardPreview(){
    ProfileCard(
        username = "testing",
        image = "hh",
        point = 100
    )
}
