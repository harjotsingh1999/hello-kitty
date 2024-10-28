package com.example.hellokitty.ui.compsables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hellokitty.R
import com.example.hellokitty.ui.interactor.CatBreedUiModel
import com.example.hellokitty.ui.interactor.CatBreedsListEvent
import com.example.hellokitty.ui.interactor.CatBreedsListSideEffects
import com.example.hellokitty.ui.interactor.CatBreedsListUiData
import com.example.hellokitty.ui.interactor.CatBreedsListUiState
import com.example.hellokitty.ui.theme.Spacing
import com.example.hellokitty.ui.theme.Typography
import com.example.hellokitty.ui.viewmodels.CatBreedsViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CatBreedsListScreen(
    viewModel: CatBreedsViewModel = hiltViewModel(),
    navigateToBreedDetail: (String) -> Unit
) {

    LaunchedEffect(viewModel) {
        viewModel.sideEffects.collectLatest {
            when (it) {
                is CatBreedsListSideEffects.OpenCatBreedDetails -> navigateToBreedDetail(it.catBreedId)
            }
        }
    }

    val state = viewModel.uiState.collectAsState()
    when (val currentState = state.value) {
        is CatBreedsListUiState.Error -> ErrorState(currentState.message) {
            viewModel.handleEvent(
                CatBreedsListEvent.OnErrorReloadClick
            )
        }

        CatBreedsListUiState.Loading -> LoadingState()
        is CatBreedsListUiState.Success -> SuccessState(currentState.data) {
            viewModel.handleEvent(it)
        }
    }
}


@Composable
fun ErrorState(message: String, onReload: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.cat_not_found),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        Text(message, style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(Spacing.small))
        Button(
            shape = ButtonDefaults.elevatedShape,
            onClick = onReload,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text(stringResource(R.string.btn_text_reload)) }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Composable
fun SuccessState(data: CatBreedsListUiData, onEvent: (CatBreedsListEvent) -> Unit) {
    val lazyListState = rememberLazyListState()

    // Track if the list is scrolled to the end
    val isNearEndOfList by remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = lazyListState.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItems - 1
        }
    }

    LaunchedEffect(isNearEndOfList) {
        if (isNearEndOfList) {
            onEvent(CatBreedsListEvent.OnScrollToEnd)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.small),
        verticalArrangement = Arrangement.spacedBy(Spacing.small),
        state = lazyListState
    ) {
        item {
            Text(
                stringResource(R.string.cat_breeds_list_title),
                style = Typography.headlineLarge
            )
        }
        items(data.catBreeds.size) { index ->
            CatBreedListItem(data.catBreeds[index], onEvent)
        }
    }
}

@Composable
fun CatBreedListItem(breed: CatBreedUiModel, onEvent: (CatBreedsListEvent) -> Unit) {
    Card(
        shape = RoundedCornerShape(Spacing.small),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        onClick = { onEvent(CatBreedsListEvent.OnCatBreedClick(breed.data)) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BreedImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(Spacing.xSmall)),
                image = breed.image
            )
            Spacer(modifier = Modifier.width(Spacing.small))
            BreedDetails(modifier = Modifier.weight(1f), breed)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreedDetails(modifier: Modifier = Modifier, breed: CatBreedUiModel) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(breed.name, style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(Spacing.xSmall))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            StatItem(R.drawable.origin, breed.origin, modifier = Modifier.weight(1f))
            StatItem(R.drawable.lifespan, breed.lifespan, modifier = Modifier.weight(1f))
            StatItem(R.drawable.weight, breed.weight, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(Spacing.xSmall))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xSmall)
        ) {
            breed.temperament.forEachIndexed { index, temp ->
                TemperamentItem(
                    modifier = Modifier,
                    temperament = temp,
                    index = index
                )
            }
        }
    }
}

val colors = listOf(
    Color.Cyan,
    Color.Gray,
    Color.Green,
    Color.Magenta,
    Color.Yellow,
)

@Composable
fun TemperamentItem(modifier: Modifier = Modifier, temperament: String, index: Int) {
    val color = remember { colors[index] }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(temperament, style = Typography.labelSmall)
    }
}

@Composable
fun StatItem(@DrawableRes icon: Int, value: String, modifier: Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Image(
            painter = painterResource(icon),
            modifier = Modifier.size(16.dp),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(Spacing.xxSmall))
        Text(
            value,
            style = Typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun BreedImage(image: CatBreedUiModel.CatUiImage, modifier: Modifier) {
    when (image) {
        CatBreedUiModel.CatUiImage.Error -> Image(
            painter = painterResource(R.drawable.cat_not_found),
            contentDescription = null,
            modifier = modifier.clip(RoundedCornerShape(Spacing.xSmall))
        )

        CatBreedUiModel.CatUiImage.Loading -> Box(
            modifier = modifier
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .align(Alignment.Center)
            )
        }

        is CatBreedUiModel.CatUiImage.Success -> Image(
            painter = rememberAsyncImagePainter(image.url),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = modifier.clip(RoundedCornerShape(Spacing.xSmall))
        )
    }
}