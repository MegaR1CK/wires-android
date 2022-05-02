package com.wires.app.extensions

/**
 * Если список содержит элемент, удаляем его, иначе добавляем
 */
fun <T> MutableList<T>.addOrRemove(item: T) {
    if (contains(item)) remove(item) else add(item)
}
