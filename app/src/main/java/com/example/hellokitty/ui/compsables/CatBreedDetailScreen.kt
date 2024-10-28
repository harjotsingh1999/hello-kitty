package com.example.hellokitty.ui.compsables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hellokitty.R
import com.example.hellokitty.ui.interactor.BreedDetailEvent
import com.example.hellokitty.ui.interactor.CatBreedDetailUiData
import com.example.hellokitty.ui.interactor.CatBreedDetailUiState
import com.example.hellokitty.ui.interactor.CatBreedImages
import com.example.hellokitty.ui.theme.Purple80
import com.example.hellokitty.ui.theme.Spacing
import com.example.hellokitty.ui.theme.Typography
import com.example.hellokitty.ui.viewmodels.CatBreedDetailViewModel

@Composable
fun CatBreedDetailScreen(viewModel: CatBreedDetailViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    when (val uiState = state.value) {
        is CatBreedDetailUiState.Initial -> {}
        is CatBreedDetailUiState.Success -> CatBreedDetail(uiData = uiState.data) {
            viewModel.handleEvent(it)
        }
    }
}

@Composable
fun CatBreedDetail(uiData: CatBreedDetailUiData, onEvent: (BreedDetailEvent) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = uiData.breedDetails.name, style = Typography.headlineLarge)
        Spacer(modifier = Modifier.size(Spacing.medium))
        Text(text = uiData.breedDetails.description, style = Typography.bodyLarge)
        Spacer(modifier = Modifier.size(Spacing.medium))
        uiData.breedDetails.stats.forEach { (key, value) ->
            StatItem(title = key, value = value)
            Spacer(modifier = Modifier.height(Spacing.xSmall))
        }
        Spacer(modifier = Modifier.size(Spacing.medium))
        Text(text = stringResource(R.string.more_images_title), style = Typography.bodyLarge)
        Spacer(modifier = Modifier.size(Spacing.medium))
        MoreImagesSection(images = uiData.images, onEvent = onEvent)
    }
}

@Composable
private fun StatItem(title: String, value: Int) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = Typography.bodySmall, modifier = Modifier.weight(1f))
        Row(modifier = Modifier.weight(1f)) {
            repeat(value) { FilledCircle() }
            repeat(5 - value) { UnfilledCircle() }
        }
    }
}

@Composable
@Preview
fun FilledCircle() {
    Box(
        modifier = Modifier
            .padding(end = Spacing.xxSmall)
            .size(16.dp)
            .background(Purple80, CircleShape)
    )
}

@Composable
@Preview
fun UnfilledCircle() {
    Box(
        modifier = Modifier
            .padding(end = Spacing.xxSmall)
            .size(16.dp)
            .border(2.dp, Purple80, CircleShape)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoreImagesSection(images: CatBreedImages, onEvent: (BreedDetailEvent) -> Unit) {
    when (images) {
        CatBreedImages.Error -> Button(onClick = { onEvent(BreedDetailEvent.OnErrorReloadClick) }) {
            Text(stringResource(R.string.btn_text_reload))
        }

        CatBreedImages.Loading -> CircularProgressIndicator()
        is CatBreedImages.Success -> FlowRow(
            maxItemsInEachRow = 3,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            images.data.forEach { imageUrl ->
                Image(
                    modifier = Modifier
                        .widthIn(max = 100.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}