//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import coil3.compose.rememberAsyncImagePainter
//import coil3.request.ImageRequest
//import coil3.size.Size
//
//@Composable
//fun ImagePickerExample() {
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//
//    // Set up launcher for picking media
//    val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
//        selectedImageUri = uri // Update the selected image URI
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Display the selected image if available
//        selectedImageUri?.let { uri ->
//            Image(
//                painter = rememberAsyncImagePainter(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(uri)
//                        .size(Size.ORIGINAL) // Load original size
//                        .build()
//                ),
//                contentDescription = "Selected Image",
//                modifier = Modifier
//                    .size(200.dp)
//                    .padding(16.dp),
//                contentScale = ContentScale.Crop
//            )
//        }
//
//        // Button to open image picker
//        Button(onClick = { launcher.launch(PickVisualMedia.ImageOnly) }) {
//            Text("Pick an Image")
//        }
//    }
//}