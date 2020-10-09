package xyz.goshanchik.prodavayka.data

import xyz.goshanchik.prodavayka.data.dao.CategoryDao

class CategoryRepository(private val dao: CategoryDao) {

    val allCategories = dao.getAllCategories()

    fun getCategoryWithProducts(id: Int) = dao.getCategoryWithProducts(id)
}