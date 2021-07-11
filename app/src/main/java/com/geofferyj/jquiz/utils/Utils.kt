package com.geofferyj.jquiz.utils

import java.util.ArrayList

class MyIterator<T>(private val listIterator: ListIterator<T>) {
    private var nextWasCalled = false
    private var previousWasCalled = false
    operator fun next(): T {
        nextWasCalled = true
        if (previousWasCalled) {
            previousWasCalled = false
            listIterator.next()
        }
        return listIterator.next()
    }

    fun previous(): T {
        if (nextWasCalled) {
            listIterator.previous()
            nextWasCalled = false
        }
        previousWasCalled = true
        return listIterator.previous()
    }
}

class ListIterator<E>(var list: List<E>) : Iterator<E> {

    private var cursor: Int = 0

    fun replace(newList: ArrayList<E>) {
        list = newList
        cursor = 0
    }

    override fun hasNext(): Boolean {
        return cursor + 1 < list.size
    }

    override fun next(): E {
        cursor++
        return current()
    }

    fun hasPrevious(): Boolean {
        return 0 <= cursor - 1
    }

    fun previous(): E {
        cursor--
        return current()
    }

    fun current(): E {
        return list[cursor]
    }

}

fun <E> ArrayList<E>.customListIterator() = ListIterator(this)
fun <E> List<E>.customListIterator() = ListIterator(this)