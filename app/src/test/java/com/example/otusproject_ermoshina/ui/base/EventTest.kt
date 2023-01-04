package com.example.otusproject_ermoshina.ui.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class EventTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `getValue getOnlyOnce ThenClearValue`(){
        val event = Event("Event")

        val valueOne = event.get()
        val valueTwo = event.get()

        assertEquals(event, valueOne)
        assertNull(valueTwo)
    }

    @Test
    fun `createPublishEvent UseFunShare`() {
        val mutablePrivateEvent = MutableLiveEvent<String>()
        val publicEvent:LiveEvent<String> = mutablePrivateEvent.share()

        mutablePrivateEvent.publishEvent("Event")
        val event = publicEvent.value

        assertEquals("Event", event!!.get())
    }

    @Test
    fun `observeOnListenerEvent GetEventFromObserver`() {
        val mutablePrivateEvent = MutableLiveEvent<String>()
        val listener = mockk<EventListener<String>>(relaxed = true)
        val publicEvent:LiveEvent<String> = mutablePrivateEvent.share()
        val lifecycleOwner = createLifecycleOwner()

        mutablePrivateEvent.publishEvent("Event")
        publicEvent.observeEvent(lifecycleOwner,listener)

        verify(exactly = 1) { listener.invoke("Event") }
        confirmVerified(listener)

    }

    fun createLifecycleOwner():LifecycleOwner {
        val lifecycleOwner = mockk<LifecycleOwner>()
        val lifecycle = mockk<Lifecycle>()
        every { lifecycle.currentState } returns Lifecycle.State.STARTED
        every { lifecycle.addObserver(any()) } answers {
            firstArg<LifecycleEventObserver>().onStateChanged(lifecycleOwner,Lifecycle.Event.ON_START)
        }
        every { lifecycleOwner.lifecycle } returns lifecycle
        return lifecycleOwner
    }
}