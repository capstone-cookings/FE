package com.example.untitled_capstone.domain.use_case.home

data class HomeUseCases(
    val getRecipeItems: GetRecipeItems,
    val getTastePreference: GetTastePreference,
    val setTastePreference: SetTastePreference,
    val addRecipe: AddRecipe,
    val getFirstRecommendation: GetFirstRecommendation,
    val getAnotherRecommendation: GetAnotherRecommendation,
    val getIsFirstSelection: GetIsFirstSelection,
    val setIsFirstSelection: SetIsFirstSelection,
    val getRecipeById: GetRecipeById,
    val toggleLike: ToggleLike,
    val deleteRecipe: DeleteRecipe,
    val modifyRecipe: ModifyRecipe,
    val uploadImage: UploadRecipeImage,
)
